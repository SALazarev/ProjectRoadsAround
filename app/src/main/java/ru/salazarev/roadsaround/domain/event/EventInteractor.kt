package ru.salazarev.roadsaround.domain.event

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import ru.salazarev.roadsaround.network.Authentication
import ru.salazarev.roadsaround.domain.user.UserRepository
import ru.salazarev.roadsaround.models.data.EventData
import ru.salazarev.roadsaround.models.domain.Event
import ru.salazarev.roadsaround.models.domain.User
import ru.salazarev.roadsaround.models.presentation.EventPreview
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Класс, устанавливающий взаимодействие между уровнями хранения данных и представления.
 *  Предназначен для работы с событиями.
 *
 *  @property eventRepository - источник данных событий.
 *  @property userRepository - интерактор работы с информацией о пользователях.
 *  @property authentication - предоставляет информацию о статусе авторизации
 *  и об авторизованном пользователе.
 */
class EventInteractor @Inject constructor(
    private val eventRepository: EventRepository,
    private val userRepository: UserRepository,
    private val authentication: Authentication
) {

    companion object {
        /** Класс с типами участия пользователя в событии */
        enum class TypeWorkWithEvent {
            GUEST,
            AUTHOR,
            MEMBER
        }

        private const val FORMAT_DATE = "HH:mm dd/MM/yyyy"
    }

    /**
     * Создание события и сохранение его в хранилище.
     * @param id - идентификатор события.
     * @param name - название события.
     * @param note - описание события.
     * @param motionType - средство движения по маршруту.
     * @param time - назначенное время события.
     * @param route - маршрут.
     * @param _members - список участников.
     * @return объект прослушивания состояния создания события
     */
    fun createEvent(
        id: String,
        name: String,
        note: String,
        motionType: String,
        time: Long,
        route: String,
        _members: List<String>
    ): Completable {
        return Completable.fromCallable {
            val authorId = authentication.getUserId()
            val members = if (_members.isNullOrEmpty()) listOf(authorId) else _members
            eventRepository.sendEvent(id, authorId, name, note, motionType, time, route, members)
        }
    }

    /**
     * Получение списка событий пользователя в скоращённом информационном формате.
     * @return объект прослушивания состояния получения списка превью-событий.
     */
    fun getUserEventPreviews(): Single<List<EventPreview>> {
        return Single.fromCallable {
            val userId = authentication.getUserId()
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat(FORMAT_DATE, Locale.ROOT)
            val listEventData = eventRepository.getUserEvents(userId)

            listEventData.map { event ->
                val author = userRepository.getUserData(event.authorId)
                val authorName =
                    if (author.lastName.isEmpty()) author.firstName else "${author.firstName} ${author.lastName}"
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

    /**
     * Получение списка событий, в которых не участвует пользователь,
     * в скоращённом информационном формате.
     * @return объект прослушивания состояния получения списка превью-событий.
     */
    fun getUsersEventPreviews(): Single<List<EventPreview>> {
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
        val dateFormat = SimpleDateFormat(FORMAT_DATE, Locale.ROOT)

        val userId = authentication.getUserId()

        return data.map { event ->
            val user: User = users.getValue(event.authorId)
            calendar.time = Date(event.time)
            val authorName =
                if (user.lastName.isEmpty()) user.firstName else "${user.firstName} ${user.lastName}"

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

    /**
     * Получение информации о событии.
     * @return объект прослушивания состояния получения события.
     */
    fun getEvent(eventId: String): Single<Event> {
        return Single.fromCallable {
            val eventData = eventRepository.getEvent(eventId)
            val usersData = userRepository.getUsersData(eventData.members)

            Event(
                eventData.id,
                eventData.authorId,
                eventData.name,
                eventData.note,
                eventData.motionType,
                eventData.time,
                eventData.route,
                usersData
            )
        }
    }

    /**
     * Добавление пользователя в событие.
     * @return объект прослушивания состояния добавления пользователя в событие.
     */
    fun addUserInEvent(eventId: String): Completable {
        return Completable.fromCallable {
            val userId = authentication.getUserId()
            eventRepository.addUserInEvent(userId, eventId)
        }
    }

    /**
     * Выход пользователя из события.
     * @return объект прослушивания состояния выхода пользователя из события.
     */
    fun leaveUserFromEvent(eventId: String): Completable {
        return Completable.fromCallable {
            val userId = authentication.getUserId()
            eventRepository.leaveUserFromEvent(userId, eventId)
        }
    }

    /**
     * Получение списка пользователей, участвующих в событии.
     * @param eventId - идентификатор события.
     * @return объект прослушивания статуса получения списка пользователей.
     */
    fun getMembersEvent(eventId: String): Single<List<User>> {
        return Single.fromCallable {
            val eventData = eventRepository.getEvent(eventId)
            userRepository.getUsersData(eventData.members)
        }
    }


}