package ru.salazarev.roadsaround.presentation.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.salazarev.roadsaround.domain.user.UserInteractor
import ru.salazarev.roadsaround.models.presentation.UserChat
import ru.salazarev.roadsaround.util.ImageConverter
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val interactor: UserInteractor,
    private val imageConverter: ImageConverter
) : ViewModel() {

    val userLiveData = MutableLiveData<UserChat>()

    val progress = MutableLiveData<Boolean>()

    var errors = MutableLiveData<Throwable>()

    init {
        loadQuotationList()
    }

    private fun loadQuotationList() {
        val user: Single<UserChat?> = Single.fromCallable {
            return@fromCallable interactor.getUserData()
        }.map { user ->
            UserChat(
                user!!.firstName,
                user.lastName,
                imageConverter.convert(user.image)
            )
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { progress.value = false }
            .doOnSubscribe { progress.value = true }

        user.subscribe(userLiveData::setValue, errors::setValue)
    }
}