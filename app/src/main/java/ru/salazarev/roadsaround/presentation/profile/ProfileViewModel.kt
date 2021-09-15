package ru.salazarev.roadsaround.presentation.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.salazarev.roadsaround.domain.user.UserInteractor
import ru.salazarev.roadsaround.models.presentation.UserPresentation
import ru.salazarev.roadsaround.util.ImageConverter
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val interactor: UserInteractor,
    private val imageConverter: ImageConverter
) : ViewModel() {

    val userLiveData = MutableLiveData<UserPresentation?>()

    val progress = MutableLiveData<Boolean>()

    init {
        loadQuotationList()
    }

    private fun loadQuotationList() {
        val single: Single<UserPresentation?> = Single.fromCallable {
            return@fromCallable interactor.getUserData()
        }.map { user ->
            UserPresentation(
                user.id,
                user.firstName,
                user.lastName,
                if (user.image != null) imageConverter.convert(user.image) else null
            )
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { progress.value = false }
            .doOnSubscribe { progress.value = true }

        single.subscribe(userLiveData::setValue) { userLiveData.value = null }
    }
}