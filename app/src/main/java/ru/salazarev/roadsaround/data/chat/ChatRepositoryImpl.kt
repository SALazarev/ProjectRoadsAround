package ru.salazarev.roadsaround.data.chat

import android.util.Log
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.rxjava3.subjects.PublishSubject
import ru.salazarev.roadsaround.domain.chat.ChatRepository
import ru.salazarev.roadsaround.models.data.MessageData
import javax.inject.Inject
import javax.inject.Named

class ChatRepositoryImpl @Inject constructor(
    private val database: FirebaseFirestore,
    private val databaseModel: MessagesCollectionModel
) : ChatRepository {
    override fun sendMessage(eventId: String, authorId: String, textMessage: String) {
        val ref =
            database.collection("chats").document("chat_$eventId").collection("messages").document()
        val message = hashMapOf(
            databaseModel.getMessage().getColumns().id to ref.id,
            databaseModel.getMessage().getColumns().authorId to authorId,
            databaseModel.getMessage().getColumns().text to textMessage,
            databaseModel.getMessage().getColumns().time to FieldValue.serverTimestamp()
        )
        ref.set(message)
    }

    override fun subscribeOnChatMessages(
        eventId: String,
        callback: PublishSubject<List<MessageData>>
    ) {
        val ref = database.collection("chats").document("chat_$eventId").collection("messages")

        ref.addSnapshotListener { snapshot, error ->
            if (snapshot != null && !snapshot.metadata.hasPendingWrites()) {
                val data: List<MessageData> = snapshot.toObjects(MessageData::class.java)
                callback.onNext(data)
            }
        }
    }
}