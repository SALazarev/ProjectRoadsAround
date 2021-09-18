package ru.salazarev.roadsaround.presentation.authentification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.salazarev.roadsaround.domain.user.UserInteractor
import ru.salazarev.roadsaround.presentation.BaseViewModel
import ru.salazarev.roadsaround.util.addTo
import javax.inject.Inject

/** ViewModel для фрагмента [AuthFragment].
 * @param interactor - объект управления информацией о пользователях
 */
class AuthViewModel @Inject constructor(private val interactor: UserInteractor) : BaseViewModel() {

    private val _authStatus = MutableLiveData<Boolean>()

    /** Прослушивание статуса авторизации. */
    val authStatus: LiveData<Boolean> = _authStatus

    private val _resetPassStatus = MutableLiveData<Boolean>()

    /** Прослушивание статуса сброса пароля. */
    val resetPassStatus: LiveData<Boolean> = _resetPassStatus

    private val _progress = MutableLiveData<Boolean>()

    /** Прослушивание статуса загрузки. */
    val progress: LiveData<Boolean> = _progress

    /** Аутентификация пользователя.
     * @param email - почта пользователя.
     * @param password - пароль пользователя.
     */
    fun authenticationUser(email: String, password: String) {
        interactor.userAuthentication(email, password)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _progress.value = true }
            .doFinally { _progress.value = false }
            .subscribe(
                { _authStatus.value = true },
                { _authStatus.value = false }
            )
            .addTo(compositeDisposable)
    }

    /** Сброс пароля.
     * @param email - почта пользователя.
     */
    fun resetPassword(email: String) {
        interactor.resetUserPassword(email)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { _progress.value = false }
            .doOnSubscribe { _progress.value = true }
            .subscribe(
                { _resetPassStatus.value = true },
                { _resetPassStatus.value = false })
            .addTo(compositeDisposable)
    }

}

