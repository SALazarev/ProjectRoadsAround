package ru.salazarev.roadsaround.presentation.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.salazarev.roadsaround.domain.UserInteractor
import javax.inject.Inject
import javax.inject.Named

class RegViewModelFactory @Inject constructor(
private val interactor: UserInteractor
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(aClass: Class<T>): T {
        return RegViewModel(interactor) as T
    }
}