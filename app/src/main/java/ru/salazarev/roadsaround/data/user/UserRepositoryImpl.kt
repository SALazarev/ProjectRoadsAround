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

    override fun setUserData(user: User) {
        val path = if (user.image != null) {
            val snapshot = Tasks.await(saveImage(user.image))
            val uri = snapshot.metadata!!.reference!!.downloadUrl
            uri.toString()
        } else ""
        val id = firebaseAuth.uid!!
        saveData(UserData(id, user.firstName, user.lastName, path))
    }

    override fun getUserData(): User {
        val docRef = database.collection(databaseModel.getUsers().collectionName)
            .document(firebaseAuth.uid!!)
        val snapshot = Tasks.await(docRef.get())
        val userData: UserData = snapshot.toObject<UserData>()!!

        val image = if (userData.image != "") {
            val path: String =
                imageHelper.let { "${it.folder}/${it.getFileName(firebaseAuth.uid!!)}.${it.jpegFileFormat}" }
            val islandRef = storage.child(path)
            val arrayBytes = Tasks.await(islandRef.getBytes(imageHelper.imageBuffer))
            arrayBytes
        } else null

        return User(firebaseAuth.uid!!, userData.firstName, userData.lastName, image)
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
    }
}