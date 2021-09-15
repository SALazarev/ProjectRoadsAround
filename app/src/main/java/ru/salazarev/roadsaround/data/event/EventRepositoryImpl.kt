package ru.salazarev.roadsaround.data.event

import android.util.Log
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings

import ru.salazarev.roadsaround.domain.event.EventRepository
import ru.salazarev.roadsaround.models.data.EventData
import ru.salazarev.roadsaround.models.data.MessageData
import javax.inject.Inject

class EventRepositoryImpl @Inject constructor(
    private val database: FirebaseFirestore,
    private val databaseModel: EventsCollectionModel
) : EventRepository {
    override fun sendEvent(eventData: EventData) {
        val settings = FirebaseFirestoreSettings.Builder()
            .setPersistenceEnabled(false)
            .build()

        database.firestoreSettings = settings
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

    override fun getUserEvents(id: String): List<EventData> {
        val ref= database.collection(databaseModel.getEvent().collectionName)
            .whereArrayContains( databaseModel.getEvent().getColumns().members, id)
        return Tasks.await(ref.get()).toObjects(EventData::class.java)
    }

    override fun getAllEvents(): List<EventData> {
        val ref= database.collection(databaseModel.getEvent().collectionName)
        return Tasks.await(ref.get()).toObjects(EventData::class.java)
    }
}