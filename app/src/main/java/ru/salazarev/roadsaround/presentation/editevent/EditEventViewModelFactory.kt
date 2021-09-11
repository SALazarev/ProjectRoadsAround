package ru.salazarev.roadsaround.presentation.editevent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.salazarev.roadsaround.domain.event.EventInteractor
import javax.inject.Inject

class EditEventViewModelFactory @Inject constructor(
    private val interactor: EventInteractor
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(aClass: Class<T>): T {
        return EditEventViewModel(interactor) as T
    }
}