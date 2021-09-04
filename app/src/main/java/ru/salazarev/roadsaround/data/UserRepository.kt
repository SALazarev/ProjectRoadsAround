package ru.salazarev.roadsaround.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import ru.salazarev.roadsaround.domain.UserRepository
import ru.salazarev.roadsaround.models.User
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val database: FirebaseFirestore,
    private val storage: StorageReference
) : UserRepository {
    override fun getUserData(): User {
        TODO()
    }

    override fun setUserData(user: User) {
        saveImage(user.image).addOnSuccessListener {
            saveData(user)
        }

    }

    private fun saveImage(image: ByteArray): UploadTask {
        return storage.child("images/user.jpg").putBytes(image)
    }
    fun saveData(user: User){
        // Create a new user with a first and last name
        val user = hashMapOf(
            "first" to "Ada",
            "last" to "Lovelace",
            "born" to 1815

        )

// Add a new document with a generated ID
        database.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d("TAG", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("TAG", "Error adding document", e)
            }
    }

}