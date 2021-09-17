package ru.salazarev.roadsaround.models.domain

/**
 * Модель события уровня бизнес-логики.
 * @property id - идентификатор события.
 * @property authorId - идентификатор пользователя.
 * @property name - название события.
 * @property description - описание события.
 * @property motionType - тип перемещения по маршруту.
 * @property time - назначенное время события.
 * @property route - маршрут.
 * @property members - участники события.
 */
data class Event(
    var id: String,
    var authorId: String,
    var name: String,
    var description: String,
    var motionType: String,
    var time: Long,
    var route: String,
    var members: List<User>
)