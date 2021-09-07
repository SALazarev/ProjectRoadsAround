package ru.salazarev.roadsaround.data.user

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import ru.salazarev.roadsaround.domain.user.UserRepository
import ru.salazarev.roadsaround.models.data.UserData
import ru.salazarev.roadsaround.models.presentation.User
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val database: FirebaseFirestore,
    private val storage: StorageReference,
    private val firebaseAuth: FirebaseAuth,
    private val userLiveData: MutableLiveData<User>,
    private val workStatus: MutableLiveData<WorkStatus>,
    private val imageHelper: ImageStorageHelper,
    private val databaseModel: UsersCollectionModel
) : UserRepository {

    override fun getWorkStatusData(): LiveData<WorkStatus> = workStatus

    override fun getUserData(): LiveData<User> {
        workStatus.value = WorkStatus.LOADING
        getUserDataWork()
        return userLiveData
    }

    override fun setUserData(user: User) {

        saveImage(user.image).addOnSuccessListener {
            try {
                it.metadata!!.reference!!.downloadUrl.addOnSuccessListener { uri ->
                    try {
                        val id = firebaseAuth.uid!!
                        val path = uri.toString()
                        saveData(UserData(id, user.firstName, user.lastName, path))
                        workStatus.value = WorkStatus.LOADING
                    } catch (e: java.lang.Exception) {
                        workStatus.value = WorkStatus.FAIL
                    }
                }
            } catch (e: java.lang.Exception) {
                workStatus.value = WorkStatus.FAIL
            }
        }.addOnFailureListener { workStatus.value = WorkStatus.FAIL }

    }

    private fun getUserDataWork() {
        try {
            val docRef =
                database.collection(databaseModel.getUsers().collectionName)
                    .document(firebaseAuth.uid!!)
            docRef.get()
                .addOnSuccessListener { snapshot ->
                    try {
                        val userData: UserData = snapshot.toObject<UserData>()!!
                        val path: String =
                            imageHelper.let { "${it.folder}/${it.getFileName(firebaseAuth.uid!!)}.${it.jpegFileFormat}" }
                        val islandRef = storage.child(path)
                        islandRef.getBytes(imageHelper.imageBuffer)
                            .addOnSuccessListener { image ->
                                val user = User(userData.firstName, userData.lastName, image)
                                userLiveData.value = user
                                workStatus.value = WorkStatus.LOADED
                            }
                            .addOnFailureListener { workStatus.value = WorkStatus.FAIL }
                    } catch (e: Exception) {
                        workStatus.value = WorkStatus.FAIL
                    }
                }
                .addOnFailureListener { workStatus.value = WorkStatus.FAIL }
        } catch (e: Exception) {
            workStatus.value = WorkStatus.FAIL
        }
    }

    private fun saveImage(image: ByteArray): UploadTask {
        val path: String = imageHelper
            .let { "${it.folder}/${it.getFileName(firebaseAuth.uid!!)}.${it.jpegFileFormat}" }
        return storage.child(path).putBytes(image)
    }

    private fun saveData(userData: UserData) {
        val user = hashMapOf(
            databaseModel.getUsers().getColumns().id to userData.id,
            databaseModel.getUsers().getColumns().firstName to userData.firstName,
            databaseModel.getUsers().getColumns().lastName to userData.lastName,
            databaseModel.getUsers().getColumns().image to userData.image
        )
        database.collection(databaseModel.getUsers().collectionName)
            .document(firebaseAuth.uid!!)
            .set(user)
            .addOnFailureListener { workStatus.value = WorkStatus.FAIL }
    }

    enum class WorkStatus {
        LOADING,
        LOADED,
        FAIL,
        NONE
    }

}