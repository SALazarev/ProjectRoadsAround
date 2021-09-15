package ru.salazarev.roadsaround.models.data

import com.google.firebase.firestore.PropertyName
import ru.salazarev.roadsaround.data.event.EventsCollectionModel

class EventData(
    @get:PropertyName(EventsCollectionModel.getEvent().getColumns().id)
    @set:PropertyName(EventsCollectionModel.getEvent().getColumns().id)
    var id: String = "",
    @get:PropertyName(EventsCollectionModel.getEvent().getColumns().authorId)
    @set:PropertyName(EventsCollectionModel.getEvent().getColumns().authorId)
    var authorId: String = "",
    @get:PropertyName(EventsCollectionModel.getEvent().getColumns().name)
    @set:PropertyName(EventsCollectionModel.getEvent().getColumns().name)
    var name: String = "",
    @get:PropertyName(EventsCollectionModel.getEvent().getColumns().note)
    @set:PropertyName(EventsCollectionModel.getEvent().getColumns().note)
    var note: String = "",
    @get:PropertyName(EventsCollectionModel.getEvent().getColumns().motionType)
    @set:PropertyName(EventsCollectionModel.getEvent().getColumns().motionType)
    var motionType: String = "",
    @get:PropertyName(EventsCollectionModel.getEvent().getColumns().time)
    @set:PropertyName(EventsCollectionModel.getEvent().getColumns().time)
    var time: Long = 0,
    @get:PropertyName(EventsCollectionModel.getEvent().getColumns().route)
    @set:PropertyName(EventsCollectionModel.getEvent().getColumns().route)
    var route: String = "",
    @get:PropertyName(EventsCollectionModel.getEvent().getColumns().members)
    @set:PropertyName(EventsCollectionModel.getEvent().getColumns().members)
    var members: List<String> = emptyList()
)
