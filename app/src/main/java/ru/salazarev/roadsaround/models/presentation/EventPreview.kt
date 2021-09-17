package ru.salazarev.roadsaround.models.presentation

import ru.salazarev.roadsaround.domain.event.EventInteractor

/**
 * Модель события уровня представления.
 * @property id - идентификатор события.
 * @property authorName - имя организатора события.
 * @property motionType - тип перемещения по маршруту.
 * @property nameEvent - название события.
 * @property time - назначенное время события.
 * @property typeWorkWithEvent - статус пользователя в событии.
 */
class EventPreview(
    val id: String,
    val authorName: String,
    val motionType: String,
    val nameEvent: String,
    val time: String,
    val typeWorkWithEvent: EventInteractor.Companion.TypeWorkWithEvent
)