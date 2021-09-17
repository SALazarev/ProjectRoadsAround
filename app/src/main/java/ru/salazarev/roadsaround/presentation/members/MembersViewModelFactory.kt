package ru.salazarev.roadsaround.presentation.members

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.salazarev.roadsaround.domain.event.EventInteractor
import ru.salazarev.roadsaround.util.ImageConverter
import javax.inject.Inject

class MembersViewModelFactory @Inject constructor(
    private val interactor: EventInteractor,
    private val imageConverter: ImageConverter
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(aClass: Class<T>): T {
        return MembersViewModel(interactor, imageConverter) as T
    }
}