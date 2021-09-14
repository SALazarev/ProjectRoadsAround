package ru.salazarev.roadsaround.presentation.editevent

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import ru.salazarev.roadsaround.domain.event.EventInteractor

class EditEventViewModel(
    private val interactor: EventInteractor,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    companion object{
        const val ROUTE_KEY = "ROUTE_KEY"
        const val TIME_KEY = "TIME_KEY"
    }
    fun setRoute(route: String?) {
        savedStateHandle[ROUTE_KEY] = route
    }

    fun setTime(time: Long?) {
        savedStateHandle[TIME_KEY] = time
    }

    fun getTime(): Long? = savedStateHandle.get(TIME_KEY)
    fun getRoute(): String? = savedStateHandle.get(ROUTE_KEY)
}