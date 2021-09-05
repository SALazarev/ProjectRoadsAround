package ru.salazarev.roadsaround.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import ru.salazarev.roadsaround.domain.UserRepository
import ru.salazarev.roadsaround.models.data.UserData
import ru.salazarev.roadsaround.models.presentation.User
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val database: FirebaseFirestore,
    private val storage: StorageReference,
    private val firebaseAuth: FirebaseAuth,
    private val userLiveData: MutableLiveData<User>,
    private val messageWorkStatus: MutableLiveData<Boolean>,
    private val loadStatus: MutableLiveData<Boolean>,
    private val imageHelper: ImageStorageHelper,
    private val databaseModel: DataCollectionsModel
) : UserRepository {

    override fun getMessageWorkStatus(): LiveData<Boolean> = messageWorkStatus

    override fun getLoadStatus(): LiveData<Boolean> = loadStatus

    override fun getUserLiveData(): LiveData<User> {
        getUserData()
        return userLiveData
    }

    private fun getUserData() {
        try {
            val docRef =
                database.collection(databaseModel.getUsers().collectionName)
                    .document(firebaseAuth.uid!!)
            docRef.get().addOnSuccessListener { snapshot ->
                val userData: UserData = snapshot.toObject<UserData>()!!
                val path: String =
                    imageHelper.let { "${it.folder}/${it.getFileName(firebaseAuth.uid!!)}.${it.jpegFileFormat}" }
                val islandRef = storage.child(path)
                islandRef.getBytes(imageHelper.imageBuffer).addOnSuccessListener { image ->
                    val user = User(userData.firstName, userData.lastName, image)
                    userLiveData.value = user
                }.addOnFailureListener { getFail() }
            }.addOnFailureListener { getFail() }
        } catch (e: Exception) {
            getFail()
        }
    }

    private fun getFail() {
        messageWorkStatus.value = true
    }

    override fun setUserData(user: User) {
        try {
            saveImage(user.image).addOnSuccessListener {
                it.metadata?.reference?.downloadUrl?.addOnSuccessListener { uri ->
                    val id = firebaseAuth.uid!!
                    val path = uri.toString()
                    saveData(UserData(id, user.firstName, user.lastName, path))
                }
            }.addOnFailureListener { getFail() }
        } catch (e: java.lang.Exception) {
            getFail()
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
            .addOnFailureListener { getFail() }
    }

}