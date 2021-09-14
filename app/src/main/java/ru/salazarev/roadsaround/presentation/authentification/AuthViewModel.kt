package ru.salazarev.roadsaround.presentation.authentification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.salazarev.roadsaround.domain.user.UserInteractor
import javax.inject.Inject

class AuthViewModel @Inject constructor(private val interactor: UserInteractor) : ViewModel() {

    val authStatus = MutableLiveData<Boolean>()


    private val _resetPassStatus = MutableLiveData<Boolean>()
    val resetPassStatus: LiveData<Boolean> = _resetPassStatus


    val progress = MutableLiveData<Boolean>()

    fun authenticationUser(email: String, password: String) {
        val user = Completable.fromCallable {
            return@fromCallable interactor.userAuthentication(email, password)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { progress.value = false }
            .doOnSubscribe { progress.value = true }

        user.subscribe(
            { authStatus.value = true })
        { authStatus.value = false }
    }

    fun resetPassword(email: String) {
        val single: Single<Boolean> = Single.fromCallable {
            return@fromCallable interactor.resetUserPassword(email)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { progress.value = false }
            .doOnSubscribe { progress.value = true }

        single.subscribe(resetPassStatus::setValue) { resetPassStatus.value = false }
    }
}