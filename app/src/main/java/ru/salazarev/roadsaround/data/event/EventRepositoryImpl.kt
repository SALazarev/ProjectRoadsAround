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

    override fun sendEvent(event: EventData) {
        if (event.id.isEmpty()) createEvent(event)
        else updateEvent(event)
    }

    private fun createEvent(eventData: EventData) {
        val ref = database.collection(databaseModel.getEvent().collectionName).document()
        val event = hashMapOf(
            databaseModel.getEvent().getColumns().id to ref.id,
            databaseModel.getEvent().getColumns().authorId to eventData.authorId,
            databaseModel.getEvent().getColumns().name to eventData.name,
            databaseModel.getEvent().getColumns().note to eventData.note,
            databaseModel.getEvent().getColumns().time to eventData.time,
            databaseModel.getEvent().getColumns().route to eventData.route,
            databaseModel.getEvent().getColumns().motionType to eventData.motionType,
            databaseModel.getEvent().getColumns().members to eventData.members
        )
        ref.set(event)
    }

    private fun updateEvent(eventData: EventData) {
        val ref =
            database.collection(databaseModel.getEvent().collectionName).document(eventData.id)
        val event = hashMapOf(
            databaseModel.getEvent().getColumns().id to eventData.id,
            databaseModel.getEvent().getColumns().authorId to eventData.authorId,
            databaseModel.getEvent().getColumns().name to eventData.name,
            databaseModel.getEvent().getColumns().note to eventData.note,
            databaseModel.getEvent().getColumns().time to eventData.time,
            databaseModel.getEvent().getColumns().route to eventData.route,
            databaseModel.getEvent().getColumns().motionType to eventData.motionType,
            databaseModel.getEvent().getColumns().members to eventData.members
        )
        ref.update(event)
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