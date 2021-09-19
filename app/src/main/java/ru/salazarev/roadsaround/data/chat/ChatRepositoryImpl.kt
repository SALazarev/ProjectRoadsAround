package ru.salazarev.roadsaround.data.chat

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.subjects.PublishSubject
import ru.salazarev.roadsaround.domain.chat.ChatRepository
import ru.salazarev.roadsaround.models.data.MessageData
import javax.inject.Inject

/**
 * Класс репозитория, реализующий интерфейс бизнес-логики и позволяющий обращаться к информации
 * о сообщениях в чатах событий.
 *
 * @property database - модель общения с базой данных Firebase Cloud Store.
 * @property databaseModel - модель преобразования данных уровня хранения в формат базы данных.
 */
class ChatRepositoryImpl @Inject constructor(
    private val database: FirebaseFirestore,
    private val databaseModel: MessagesCollectionModel
) : ChatRepository {

    override fun sendMessage(eventId: String, authorId: String, textMessage: String): Completable = Completable.fromCallable {
        val ref =
            database.collection("chats").document("chat_$eventId")
                .collection("messages").document()
        val message = hashMapOf(
            databaseModel.getMessage().getColumns().id to ref.id,
            databaseModel.getMessage().getColumns().authorId to authorId,
            databaseModel.getMessage().getColumns().text to textMessage,
            databaseModel.getMessage().getColumns().time to FieldValue.serverTimestamp()
        )
        ref.set(message)
    }

    override fun subscribeOnChatMessages(
        eventId: String
    ): PublishSubject<List<MessageData>> {
        val callback = PublishSubject.create<List<MessageData>>()
        val ref = database.collection("chats").document("chat_$eventId")
            .collection("messages")
        ref.addSnapshotListener { snapshot, _ ->
            if (snapshot != null && !snapshot.metadata.hasPendingWrites()) {
                val data: List<MessageData> = snapshot.toObjects(MessageData::class.java)
                callback.onNext(data)
            }
        }
        return callback
    }
}