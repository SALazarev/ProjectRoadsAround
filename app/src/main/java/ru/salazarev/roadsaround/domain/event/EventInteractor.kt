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

    companion object {
        enum class TypeWorkWithEvent {
            GUEST,
            AUTHOR,
            MEMBER
        }
    }

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
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ROOT)
            val listEventData = eventRepository.getUserEvents(userId)

            listEventData.map { event ->
                val author = userRepository.getUserData(event.authorId)
                val authorName =
                    if (author.lastName == "") author.firstName else "${author.firstName} ${author.lastName}"
                calendar.time = Date(event.time)
                EventPreview(
                    event.id,
                    authorName,
                    event.motionType,
                    event.name,
                    dateFormat.format(calendar.time),
                    checkEventStatus(userId, event)
                )
            }
        }
    }

    private fun checkEventStatus(
        userId: String,
        event: EventData
    ): TypeWorkWithEvent {
        return when {
            userId == event.authorId -> TypeWorkWithEvent.AUTHOR
            event.members.contains(userId) -> TypeWorkWithEvent.MEMBER
            else -> TypeWorkWithEvent.GUEST
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

        val userId = authentication.getUserId()

        return data.map { event ->
            val user: User = users.getValue(event.authorId)
            calendar.time = Date(event.time)
            val authorName =
                if (user.lastName == "") user.firstName else "${user.firstName} ${user.lastName}"

            EventPreview(
                event.id,
                authorName,
                event.motionType,
                event.name,
                dateFormat.format(calendar.time),
                checkEventStatus(userId, event)
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

    fun addUserInEvent(eventId: String): Completable {
        return Completable.fromCallable {
            val userId = authentication.getUserId()
            eventRepository.addUserInEvent(userId, eventId)
        }
    }

    fun leaveUserFromEvent(eventId: String): Completable {
        return Completable.fromCallable {
            val userId = authentication.getUserId()
            eventRepository.leaveUserFromEvent(userId, eventId)
        }
    }


}