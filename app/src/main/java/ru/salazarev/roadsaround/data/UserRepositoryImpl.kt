package ru.salazarev.roadsaround.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import ru.salazarev.roadsaround.domain.UserRepository
import ru.salazarev.roadsaround.models.data.UserData
import ru.salazarev.roadsaround.models.presentation.User
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val database: FirebaseFirestore,
    private val storage: StorageReference,
    private val firebaseAuth: FirebaseAuth
) : UserRepository {

    override fun getUserData(): User {
        TODO()
    }

    override fun setUserData(user: User) {
        saveImage(user.image).addOnSuccessListener {
            it.metadata?.reference?.downloadUrl?.addOnSuccessListener { uri ->
                val id = firebaseAuth.uid!!
                val path = uri.toString()
                saveData(UserData(id, user.firstName, user.lastName, path))
            }
        }

    }

    private fun saveImage(image: ByteArray): UploadTask {
        val folder = "UsersAvatar"
        val fileName = "avatar_${firebaseAuth.uid!!}"
        val fileFormat = "jpg"
        return storage.child("${folder}/${fileName}.${fileFormat}").putBytes(image)
    }

    private fun saveData(userData: UserData) {
        val user = hashMapOf(
            DataCollections.Users.Columns.id to userData.id,
            DataCollections.Users.Columns.firstName to userData.firstName,
            DataCollections.Users.Columns.lastName to userData.lastName,
            DataCollections.Users.Columns.image to userData.image
        )
        database.collection(DataCollections.Users.collectionName)
            .document(firebaseAuth.uid!!)
            .set(user)
            .addOnSuccessListener { documentReference ->
                Log.d("TAG", "DocumentSnapshot added")
            }
            .addOnFailureListener { e ->
                Log.w("TAG", "Error adding document", e)
            }
    }

}