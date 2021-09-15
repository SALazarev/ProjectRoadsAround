package ru.salazarev.roadsaround.presentation.eventinformation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.salazarev.roadsaround.domain.event.EventInteractor
import ru.salazarev.roadsaround.util.ImageConverter
import javax.inject.Inject

class EventInformationViewModelFactory @Inject constructor(
    private val interactor: EventInteractor,
    private val imageConverter: ImageConverter
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(aClass: Class<T>): T {
        return EventInformationViewModel(interactor,imageConverter) as T
    }
}