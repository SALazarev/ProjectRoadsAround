package ru.salazarev.roadsaround.domain.event

import ru.salazarev.roadsaround.models.data.EventData

interface EventRepository {
    fun sendEvent(event: EventData)
    fun getUserEvents(id: String): List<EventData>
    fun getAllEvents():List<EventData>
    fun getEvent(eventId: String): EventData
    fun addUserInEvent(userId: String, eventId: String)
    fun leaveUserFromEvent(userId: String, eventId: String)
}