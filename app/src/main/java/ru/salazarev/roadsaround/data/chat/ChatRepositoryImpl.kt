package ru.salazarev.roadsaround.data.chat

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableEmitter
import io.reactivex.rxjava3.core.ObservableOnSubscribe
import io.reactivex.rxjava3.subjects.PublishSubject
import ru.salazarev.roadsaround.domain.chat.ChatRepoListener
import ru.salazarev.roadsaround.domain.chat.ChatRepository
import ru.salazarev.roadsaround.models.data.MessageData
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class ChatRepositoryImpl @Inject constructor(
    private val database: FirebaseFirestore,
    private val databaseModel: MessagesCollectionModel
) : ChatRepository {
    private val docRef = database
        .collection("chats").document("test_chat")
        .collection("messages")

    override fun sendMessage(authorId: String, textMessage: String) {

        val ref = docRef.document()

        val message = hashMapOf(
            databaseModel.getMessage().getColumns().id to docRef.id,
            databaseModel.getMessage().getColumns().authorId to authorId,
            databaseModel.getMessage().getColumns().text to textMessage,
            databaseModel.getMessage().getColumns().time to FieldValue.serverTimestamp()
        )

        ref.set(message)
    }

    override fun getChatMessages(listener: ChatRepoListener): Observable<List<MessageData>> {

        docRef.addSnapshotListener { snapshot, error ->
            if (snapshot != null && snapshot.metadata.hasPendingWrites()) {
                //local
            } else {
                val data: List<MessageData> = snapshot!!.toObjects(MessageData::class.java)
                listener.getData(data)
            }
        }

        val handler: ObservableOnSubscribe<List<MessageData>> =
            object : ObservableOnSubscribe<List<MessageData>> {
                override fun subscribe(emitter: ObservableEmitter<List<MessageData>>?) {
                    TODO("Not yet implemented")
                }

            }
        return Observable.create(handler)
    }

    override fun test(obser: PublishSubject<String>) {
        docRef.addSnapshotListener { snapshot, error ->
            if (snapshot != null && snapshot.metadata.hasPendingWrites()) {
            } else {
                val data: List<MessageData> = snapshot!!.toObjects(MessageData::class.java)
                obser.onNext("Сообщение отправлено")
            }
        }
    }

}