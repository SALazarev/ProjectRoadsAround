package ru.salazarev.roadsaround.data.chat

import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Tasks
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.StorageReference
import ru.salazarev.roadsaround.data.user.ImageStorageHelper
import ru.salazarev.roadsaround.data.user.UserRepositoryImpl
import ru.salazarev.roadsaround.data.user.UsersCollectionModel
import ru.salazarev.roadsaround.domain.chat.ChatRepository
import ru.salazarev.roadsaround.models.data.UserData
import ru.salazarev.roadsaround.models.domain.Message
import ru.salazarev.roadsaround.models.domain.User
import ru.salazarev.roadsaround.util.ImageConverter
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val database: FirebaseFirestore,
    private val storage: StorageReference,
    private val firebaseAuth: FirebaseAuth,
    private val userLiveData: MutableLiveData<User>,
    private val imageHelper: ImageStorageHelper,
    private val databaseModel: UsersCollectionModel
) : ChatRepository {
    override fun sendMessage(message: Message) {
        TODO("Not yet implemented")
    }

    override fun getChatMessages() {
        TODO("Not yet implemented")
    }

    override fun getMessageWorkStatus() {
        TODO("Not yet implemented")
    }
}