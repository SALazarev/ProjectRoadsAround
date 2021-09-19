package ru.salazarev.roadsaround.data.event

import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.google.firebase.firestore.ktx.toObject
import ru.salazarev.roadsaround.domain.event.EventRepository
import ru.salazarev.roadsaround.models.data.EventData
import javax.inject.Inject


/**
 * Класс репозитория, реализующий интерфейс бизнес-логики и позволяющий обращаться к информации
 * о событиях.
 *
 * @property database - модель общения с базой данных Firebase Cloud Store.
 * @property databaseModel - модель преобразования данных уровня хранения в формат базы данных.
 */
class EventRepositoryImpl @Inject constructor(
    private val database: FirebaseFirestore,
    private val databaseModel: EventsCollectionModel
) : EventRepository {

    private fun createEvent(
        authorId: String, name: String, note: String, motionType: String,
        time: Long, route: String, members: List<String>
    ) {
        val ref = database.collection(databaseModel.getEvent().collectionName).document()
        val event = hashMapOf(
            databaseModel.getEvent().getColumns().id to ref.id,
            databaseModel.getEvent().getColumns().authorId to authorId,
            databaseModel.getEvent().getColumns().name to name,
            databaseModel.getEvent().getColumns().note to note,
            databaseModel.getEvent().getColumns().time to time,
            databaseModel.getEvent().getColumns().route to route,
            databaseModel.getEvent().getColumns().motionType to motionType,
            databaseModel.getEvent().getColumns().members to members
        )
        ref.set(event)
    }

    private fun updateEvent(
        id: String, authorId: String, name: String, note: String, motionType: String,
        time: Long, route: String, members: List<String>
    ) {
        val ref =
            database.collection(databaseModel.getEvent().collectionName).document(id)
        val event = hashMapOf(
            databaseModel.getEvent().getColumns().id to id,
            databaseModel.getEvent().getColumns().authorId to authorId,
            databaseModel.getEvent().getColumns().name to name,
            databaseModel.getEvent().getColumns().note to note,
            databaseModel.getEvent().getColumns().time to time,
            databaseModel.getEvent().getColumns().route to route,
            databaseModel.getEvent().getColumns().motionType to motionType,
            databaseModel.getEvent().getColumns().members to members
        )
        ref.update(event)
    }

    override fun sendEvent(
        id: String, authorId: String, name: String, note: String, motionType: String,
        time: Long, route: String, members: List<String>
    ) {
        if (id.isEmpty()) createEvent(authorId, name, note, motionType, time, route, members)
        else updateEvent(id, authorId, name, note, motionType, time, route, members)
    }

    override fun getUserEvents(id: String): List<EventData> {
        val ref = database.collection(databaseModel.getEvent().collectionName)
            .whereArrayContains(databaseModel.getEvent().getColumns().members, id)
        return Tasks.await(ref.get(Source.SERVER)).toObjects(EventData::class.java)
    }

    override fun getAllEvents(): List<EventData> {
        val ref = database.collection(databaseModel.getEvent().collectionName)
        return Tasks.await(ref.get(Source.SERVER)).toObjects(EventData::class.java)
    }

    override fun getEvent(eventId: String): EventData {
        val ref = database.collection(databaseModel.getEvent().collectionName).document(eventId)
        return Tasks.await(ref.get(Source.SERVER)).toObject<EventData>()!!
    }

    override fun addUserInEvent(userId: String, eventId: String) {
        val ref = database.collection(databaseModel.getEvent().collectionName).document(eventId)
        ref.update(databaseModel.getEvent().getColumns().members, FieldValue.arrayUnion(userId))
    }

    override fun leaveUserFromEvent(userId: String, eventId: String) {
        val ref = database.collection(databaseModel.getEvent().collectionName).document(eventId)
        ref.update(databaseModel.getEvent().getColumns().members, FieldValue.arrayRemove(userId))
    }
}