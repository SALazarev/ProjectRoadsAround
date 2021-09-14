package ru.salazarev.roadsaround.presentation.editevent

import androidx.lifecycle.ViewModel
import ru.salazarev.roadsaround.domain.event.EventInteractor

class EditEventViewModel(private val interactor: EventInteractor): ViewModel() {
    var name = ""
    var note = ""
    var time: Long = 0
    var route = ""
    var motionType = ""
}