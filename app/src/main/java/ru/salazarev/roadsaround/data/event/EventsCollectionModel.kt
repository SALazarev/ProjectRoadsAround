package ru.salazarev.roadsaround.data.event

object EventsCollectionModel {
    fun getEvent() = Event
}

object Event {
    const val collectionName = "event"
    fun getColumns() = Columns
}

object Columns {
    const val id = "id"
    const val authorId = "author_id"
    const val name = "name"
    const val note = "note"
    const val motionType = "motion_type"
    const val time = "time"
    const val route = "route"
    const val members = "members"
}
