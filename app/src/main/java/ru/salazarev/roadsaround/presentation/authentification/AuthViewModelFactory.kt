package ru.salazarev.roadsaround.presentation.authentification

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.salazarev.roadsaround.domain.user.UserInteractor
import javax.inject.Inject

/** Фабрика ViewModel для фрагмента [AuthFragment].
 * @param interactor - объект управления информацией о пользователях
 */
class AuthViewModelFactory @Inject constructor(
    private val interactor: UserInteractor
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(aClass: Class<T>): T {
        return AuthViewModel(interactor) as T
    }
}