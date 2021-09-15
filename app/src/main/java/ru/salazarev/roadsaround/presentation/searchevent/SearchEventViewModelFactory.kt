package ru.salazarev.roadsaround.presentation.searchevent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.salazarev.roadsaround.domain.event.EventInteractor
import javax.inject.Inject

class SearchEventViewModelFactory @Inject constructor(
    private val interactor: EventInteractor
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(aClass: Class<T>): T {
        return SearchEventViewModel(interactor) as T
    }
}