package ru.salazarev.roadsaround.presentation.main.eventlist

import ru.salazarev.roadsaround.domain.event.EventInteractor

interface ClickItemCallback {
    fun onClick(
        id: String,
        name: String,
        typeWorkWithEvent: EventInteractor.Companion.TypeWorkWithEvent
    )
}