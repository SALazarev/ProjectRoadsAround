package ru.salazarev.roadsaround.data.user

import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import ru.salazarev.roadsaround.domain.user.UserRepository
import ru.salazarev.roadsaround.models.data.UserData
import ru.salazarev.roadsaround.models.domain.User
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val database: FirebaseFirestore,
    private val storage: StorageReference,
    private val firebaseAuth: FirebaseAuth,
    private val imageHelper: ImageStorageHelper,
    private val databaseModel: UsersCollectionModel
) : UserRepository {

    override fun setUserData(user: User): Boolean {
        return try {
            val snapshot = Tasks.await(saveImage(user.image))
            val uri = snapshot.metadata!!.reference!!.downloadUrl
            val id = firebaseAuth.uid!!
            val path = uri.toString()
            saveData(UserData(id, user.firstName, user.lastName, path))
        } catch (e: Exception) {
            false
        }
    }

    override fun getUserData(): User? {
        return try {
            val docRef = database.collection(databaseModel.getUsers().collectionName)
                .document(firebaseAuth.uid!!)
            val snapshot = Tasks.await(docRef.get())
            val userData: UserData = snapshot.toObject<UserData>()!!
            val path: String =
                imageHelper.let { "${it.folder}/${it.getFileName(firebaseAuth.uid!!)}.${it.jpegFileFormat}" }
            val islandRef = storage.child(path)
            val image = Tasks.await(islandRef.getBytes(imageHelper.imageBuffer))
            val user = User(userData.firstName, userData.lastName, image)
            user
        } catch (e: Exception) {
            null
        }
    }

    private fun saveImage(image: ByteArray): UploadTask {
        val path: String = imageHelper
            .let { "${it.folder}/${it.getFileName(firebaseAuth.uid!!)}.${it.jpegFileFormat}" }
        return storage.child(path).putBytes(image)
    }

    private fun saveData(userData: UserData): Boolean {
        return try {
            val user = hashMapOf(
                databaseModel.getUsers().getColumns().id to userData.id,
                databaseModel.getUsers().getColumns().firstName to userData.firstName,
                databaseModel.getUsers().getColumns().lastName to userData.lastName,
                databaseModel.getUsers().getColumns().image to userData.image
            )
            val test = database.collection(databaseModel.getUsers().collectionName)
                .document(firebaseAuth.uid!!)
                .set(user)
            Tasks.await(test)
            true
        } catch (e: Exception) {
            false
        }
    }

    enum class WorkStatus {
        LOADING,
        LOADED,
        FAIL,
        NONE
    }

}