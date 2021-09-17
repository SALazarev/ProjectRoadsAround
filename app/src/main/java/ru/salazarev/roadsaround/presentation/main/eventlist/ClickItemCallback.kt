package ru.salazarev.roadsaround.presentation.main.eventlist

import ru.salazarev.roadsaround.domain.event.EventInteractor

/** Интерфейс слушателя нажатия на представление события. */
interface ClickItemCallback {
    /** Нажатие на представление события
     * @param id - идентификатор события.
     * @param name - название события.
     * @param typeWorkWithEvent - тип участия пользователя в событии.
     */
    fun onClick(
        id: String,
        name: String,
        typeWorkWithEvent: EventInteractor.Companion.TypeWorkWithEvent
    )
}