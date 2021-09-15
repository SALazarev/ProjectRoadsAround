package ru.salazarev.roadsaround.domain.event

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import ru.salazarev.roadsaround.domain.user.Authentication
import ru.salazarev.roadsaround.domain.user.UserRepository
import ru.salazarev.roadsaround.models.data.EventData
import ru.salazarev.roadsaround.models.domain.Event
import ru.salazarev.roadsaround.models.domain.User
import ru.salazarev.roadsaround.models.presentation.EventPreview
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class EventInteractor @Inject constructor(
    private val eventRepository: EventRepository,
    private val userRepository: UserRepository,
    private val authentication: Authentication
) {
    fun createEvent(
        name: String, note: String, motionType: String, time: Long, route: String
    ): Completable {
        return Completable.fromCallable {
            val authorId = authentication.getUserId()
            eventRepository.sendEvent(
                EventData(
                    authorId = authorId,
                    name = name,
                    note = note,
                    motionType = motionType,
                    time = time,
                    route = route,
                    members = listOf(authorId)
                )
            )
        }
    }

    fun getUserEventsPreview(): Single<List<EventPreview>> {
        return Single.fromCallable {
            val userId = authentication.getUserId()
            val user = userRepository.getUserData(userId)
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ROOT)
            val nameAuthor =
                if (user.lastName == "") user.firstName else "${user.firstName} ${user.lastName}"
            val listEventData = eventRepository.getUserEvents(userId)
            listEventData.map {
                calendar.time = Date(it.time)
                EventPreview(
                    it.id,
                    nameAuthor,
                    it.motionType,
                    it.name,
                    dateFormat.format(calendar.time)
                )
            }
        }
    }

    fun getUsersEventsPreview(): Single<List<EventPreview>> {
        return Single.fromCallable {
            val listEventData = eventRepository.getAllEvents()
            val usersId = listEventData.map { it.authorId }.distinct()
            val usersData = userRepository.getUsersData(usersId)

            val userId = authentication.getUserId()
            getEvents(listEventData.sortedBy {
                it.time
            }.filter { it.authorId != userId && !it.members.contains(userId) },
                usersData.map { it.id to it }.toMap()
            )
        }
    }

    private fun getEvents(data: List<EventData>, users: Map<String, User>): List<EventPreview> {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ROOT)

        return data.map {
            val user: User = users.getValue(it.authorId)
            calendar.time = Date(it.time)
            val authorName =
                if (user.lastName == "") user.firstName else "${user.firstName} ${user.lastName}"
            EventPreview(
                it.id,
                authorName,
                it.motionType,
                it.name,
                dateFormat.format(calendar.time)
            )
        }
    }

    fun getEvent(eventId: String): Single<Event> {
        return Single.fromCallable {
            val eventData = eventRepository.getEvent(eventId)
            val usersData = userRepository.getUsersData(eventData.members)

            val calendar = Calendar.getInstance()
            calendar.time = Date(eventData.time)
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ROOT)

            Event(
                eventData.id,
                eventData.authorId,
                eventData.name,
                eventData.note,
                eventData.motionType,
                dateFormat.format(calendar.time),
                eventData.route,
                usersData
            )
        }
    }


}