package ru.salazarev.roadsaround.presentation.members

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.salazarev.roadsaround.domain.event.EventInteractor
import ru.salazarev.roadsaround.presentation.eventinformation.EventInformationFragment
import ru.salazarev.roadsaround.util.ImageConverter
import javax.inject.Inject

/** Фабрика ViewModel для фрагмента [MembersViewModel].
 * @param interactor - объект управления информацией о событиях.
 * @param imageConverter - конвертер изображений.
 */
class MembersViewModelFactory @Inject constructor(
    private val interactor: EventInteractor,
    private val imageConverter: ImageConverter
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(aClass: Class<T>): T {
        return MembersViewModel(interactor, imageConverter) as T
    }
}