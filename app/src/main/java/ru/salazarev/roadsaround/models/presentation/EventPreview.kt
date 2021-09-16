package ru.salazarev.roadsaround.models.presentation

import ru.salazarev.roadsaround.domain.event.EventInteractor

class EventPreview(
    val id: String,
    val authorName: String,
    val motionType: String,
    val nameEvent: String,
    val time: String,
    val typeWorkWithEvent: EventInteractor.Companion.TypeWorkWithEvent
)