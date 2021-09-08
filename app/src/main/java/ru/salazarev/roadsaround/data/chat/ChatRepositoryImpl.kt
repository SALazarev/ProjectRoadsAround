package ru.salazarev.roadsaround.data.chat

import com.google.firebase.firestore.FirebaseFirestore

import ru.salazarev.roadsaround.domain.chat.ChatRepository

import java.util.*
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val database: FirebaseFirestore,
    private val databaseModel: MessagesCollectionModel
) : ChatRepository {
    override fun sendMessage(authorId: String, textMessage: String) {
        val messageId = UUID.randomUUID().toString()
        val message = hashMapOf(
            databaseModel.getMessage().getColumns().id to messageId,
            databaseModel.getMessage().getColumns().authorId to authorId,
            databaseModel.getMessage().getColumns().text to textMessage,
            databaseModel.getMessage().getColumns().time to "4:51"
        )
        database
            .collection("chats").document("test_chat")
            .collection("messages").add(message)
    }

    override fun getChatMessages() {
        TODO("Not yet implemented")
    }
}