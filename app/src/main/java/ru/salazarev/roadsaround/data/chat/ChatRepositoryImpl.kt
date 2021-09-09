package ru.salazarev.roadsaround.data.chat

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.rxjava3.annotations.NonNull
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import ru.salazarev.roadsaround.domain.chat.ChatRepoListener

import ru.salazarev.roadsaround.domain.chat.ChatRepository
import ru.salazarev.roadsaround.models.data.MessageData

import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val database: FirebaseFirestore,
    private val databaseModel: MessagesCollectionModel
) : ChatRepository {
    override fun sendMessage(authorId: String, textMessage: String) {

        val docRef = database
            .collection("chats").document("test_chat")
            .collection("messages").document()

        val message = hashMapOf(
            databaseModel.getMessage().getColumns().id to docRef.id,
            databaseModel.getMessage().getColumns().authorId to authorId,
            databaseModel.getMessage().getColumns().text to textMessage,
            databaseModel.getMessage().getColumns().time to FieldValue.serverTimestamp()
        )

        docRef.set(message)
    }

    override fun getChatMessages(listener: ChatRepoListener): Observable<List<MessageData>> {
        val docRef = database
            .collection("chats").document("test_chat")
            .collection("messages")

        docRef.addSnapshotListener { snapshot, error ->
            if (snapshot != null && snapshot.metadata.hasPendingWrites()) {
                //local
            } else {
                val data: List<MessageData> = snapshot!!.toObjects(MessageData::class.java)
                listener.getData(data)
            }
        }

        val handler: ObservableOnSubscribe<List<MessageData>> = object: ObservableOnSubscribe<List<MessageData>>{
            override fun subscribe(emitter: ObservableEmitter<List<MessageData>>?) {
                TODO("Not yet implemented")
            }

        }
        return Observable.create(handler)
    }

}