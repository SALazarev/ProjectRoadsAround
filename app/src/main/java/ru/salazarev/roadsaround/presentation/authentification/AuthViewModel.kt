package ru.salazarev.roadsaround.presentation.authentification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.salazarev.roadsaround.domain.user.UserInteractor
import ru.salazarev.roadsaround.presentation.BaseViewModel
import ru.salazarev.roadsaround.util.addTo
import javax.inject.Inject

class AuthViewModel @Inject constructor(private val interactor: UserInteractor) : BaseViewModel() {

    private val _authStatus = MutableLiveData<Boolean>()
    val authStatus: LiveData<Boolean> = _authStatus


    private val _resetPassStatus = MutableLiveData<Boolean>()
    val resetPassStatus: LiveData<Boolean> = _resetPassStatus


    private val _progress = MutableLiveData<Boolean>()
    val progress: LiveData<Boolean> = _progress


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

    fun resetPassword(email: String) {
        interactor.resetUserPassword(email)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { _progress.value = false }
            .doOnSubscribe { _progress.value = true }
            .subscribe(_resetPassStatus::setValue, {_resetPassStatus.value = false })
            .addTo(compositeDisposable)
    }

}

