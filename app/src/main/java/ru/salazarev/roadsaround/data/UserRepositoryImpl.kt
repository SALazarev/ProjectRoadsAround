package ru.salazarev.roadsaround.data

import android.util.Log
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
    private val userLiveData: MutableLiveData<User>
) : UserRepository {


    override fun getUserData(): LiveData<User> {
        val docRef =
            database.collection(DataCollections.Users.collectionName).document(firebaseAuth.uid!!)
        docRef.get().addOnSuccessListener {
            val userData: UserData = it.toObject<UserData>()!!

            val folder = "UsersAvatar"
            val fileName = "avatar_${firebaseAuth.uid!!}"
            val fileFormat = "jpg"
            var islandRef = storage.child("${folder}/${fileName}.${fileFormat}")

            val buffer: Long = 250 * 250
            islandRef.getBytes(buffer).addOnSuccessListener { image ->
                val user = User(userData.firstName, userData.lastName, image)
                userLiveData.value = user
            }.addOnFailureListener {
                val i = 0
            }


        }
        return userLiveData
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