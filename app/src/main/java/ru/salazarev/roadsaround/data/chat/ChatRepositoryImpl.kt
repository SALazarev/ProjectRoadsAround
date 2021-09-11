package ru.salazarev.roadsaround.data.chat

import android.util.Log
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FieldValue
import io.reactivex.rxjava3.subjects.PublishSubject
import ru.salazarev.roadsaround.domain.chat.ChatRepository
import ru.salazarev.roadsaround.models.data.MessageData
import javax.inject.Inject
import javax.inject.Named

class ChatRepositoryImpl @Inject constructor(
    @Named(MessagesCollectionModel.getMessage().collectionName) private val collectionRef: CollectionReference,
    private val databaseModel: MessagesCollectionModel
) : ChatRepository {
    override fun sendMessage(authorId: String, textMessage: String) {
        val ref = collectionRef.document()
        val message = hashMapOf(
            databaseModel.getMessage().getColumns().id to ref.id,
            databaseModel.getMessage().getColumns().authorId to authorId,
            databaseModel.getMessage().getColumns().text to textMessage,
            databaseModel.getMessage().getColumns().time to FieldValue.serverTimestamp()
        )
            ref.set(message).addOnSuccessListener {  Log.d("TAG", "OK") }.addOnFailureListener {  Log.d("TAG", "FAIL") }.addOnCanceledListener {  Log.d("TAG", "SHIT") }.addOnCompleteListener {  Log.d("TAG", "YEAH!") }
    }

    override fun getChatMessages(callback: PublishSubject<List<MessageData>>) {
        collectionRef.addSnapshotListener { snapshot, error ->
            if (snapshot == null || !snapshot.metadata.hasPendingWrites()) {
                val data: List<MessageData> = snapshot!!.toObjects(MessageData::class.java)
                callback.onNext(data)
            }
        }
    }
}