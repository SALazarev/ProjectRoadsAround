package ru.salazarev.roadsaround.presentation.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.salazarev.roadsaround.domain.user.UserInteractor
import ru.salazarev.roadsaround.models.domain.User
import ru.salazarev.roadsaround.models.presentation.UserPresentation
import ru.salazarev.roadsaround.util.ImageConverter
import javax.inject.Inject

class ProfileViewModel @Inject constructor(
    private val interactor: UserInteractor,
    private val imageConverter: ImageConverter
) : ViewModel() {

    val user = MutableLiveData<UserPresentation?>()

    val progress = MutableLiveData<Boolean>()

    init {
        loadQuotationList()
    }

    private fun loadQuotationList() {
        interactor.getUserData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { progress.value = false }
            .doOnSubscribe { progress.value = true }
            .subscribe(
                { user.value = getMembersInfo(it) },
                { user.value = null })
    }

    private fun getMembersInfo(user: User): UserPresentation {
        val memberName =
            if (user.lastName == "") user.firstName else "${user.firstName} ${user.lastName}"
        val image = if (user.image != null) imageConverter.convert(user.image) else null
        return UserPresentation(user.id, memberName, image)
    }
}