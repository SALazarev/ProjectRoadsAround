package ru.salazarev.roadsaround.models.presentation

/**
 * Модель события уровня представления.
 * @property id - идентификатор события.
 * @property authorId - идентификатор пользователя.
 * @property name - название события.
 * @property note - описание события.
 * @property motionType - тип перемещения по маршруту.
 * @property time - назначенное время события.
 * @property route - маршрут.
 * @property members - участники события.
 */
class EventPresentation(
    var id: String,
    var authorId: String,
    var name: String,
    var note: String,
    var motionType: String,
    var time: String,
    var route: String,
    var members: List<UserPresentation>
)