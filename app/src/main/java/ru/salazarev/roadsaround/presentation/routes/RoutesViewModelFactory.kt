package ru.salazarev.roadsaround.presentation.routes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.salazarev.roadsaround.domain.event.EventInteractor
import ru.salazarev.roadsaround.presentation.registration.RegViewModel
import javax.inject.Inject

/** Фабрика ViewModel для фрагмента [RoutesViewModel].
 * @param interactor - объект управления информацией о событиях.
 */
class RoutesViewModelFactory @Inject constructor(
    private val interactor: EventInteractor
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(aClass: Class<T>): T {
        return RoutesViewModel(interactor) as T
    }
}