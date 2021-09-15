package ru.salazarev.roadsaround.presentation.routes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.salazarev.roadsaround.domain.event.EventInteractor
import ru.salazarev.roadsaround.domain.user.UserInteractor
import ru.salazarev.roadsaround.util.ImageConverter
import javax.inject.Inject

class RoutesViewModelFactory @Inject constructor(
    private val interactor: EventInteractor
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(aClass: Class<T>): T {
        return RoutesViewModel(interactor) as T
    }
}