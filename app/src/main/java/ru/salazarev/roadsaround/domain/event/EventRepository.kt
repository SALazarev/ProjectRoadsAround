package ru.salazarev.roadsaround.domain.event

import ru.salazarev.roadsaround.models.data.EventData

/** Интерфейс хранилища данных о событиях. */
interface EventRepository {
    /**
     * Отправка информации о событии в хранилище.
     * @param id - идентификатор события.
     * @param id - идентификатор организатора события.
     * @param name - название события.
     * @param note - описание события.
     * @param motionType - средство движения по маршруту.
     * @param time - назначенное время события.
     * @param route - маршрут.
     * @param members - список участников.
     */
    fun sendEvent( id: String,
                   authorId: String,
                   name: String,
                   note: String,
                   motionType: String,
                   time: Long,
                   route: String,
                   members: List<String>)

    /**
     * Получение списка событий, в которых участвует пользователь.
     * @param id - идентификатор пользователя.
     * @return список событий пользователя.
     */
    fun getUserEvents(id: String): List<EventData>

    /**
     * Получение списка всех событий.
     * @return список событий.
     */
    fun getAllEvents(): List<EventData>

    /**
     * Получение информации о событии.
     * @param eventId - идентификатор события.
     * @return информацию о событии.
     */
    fun getEvent(eventId: String): EventData

    /**
     * Добавление участника в событие.
     * @param userId - идентификатор участника.
     * @param eventId - идентификатор события.
     */
    fun addUserInEvent(userId: String, eventId: String)

    /**
     * Выход участника из события.
     * @param userId - идентификатор участника.
     * @param eventId - идентификатор события.
     */
    fun leaveUserFromEvent(userId: String, eventId: String)
}