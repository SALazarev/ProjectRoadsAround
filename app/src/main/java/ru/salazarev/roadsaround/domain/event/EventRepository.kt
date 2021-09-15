package ru.salazarev.roadsaround.domain.event

import ru.salazarev.roadsaround.models.data.EventData

interface EventRepository {
    fun sendEvent(event: EventData)
    fun getUserEvents(id: String): List<EventData>
    fun getAllEvents():List<EventData>
}