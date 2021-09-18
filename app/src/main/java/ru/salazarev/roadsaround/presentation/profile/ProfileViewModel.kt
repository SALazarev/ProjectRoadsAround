package ru.salazarev.roadsaround.presentation.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.salazarev.roadsaround.domain.user.UserInteractor
import ru.salazarev.roadsaround.models.domain.User
import ru.salazarev.roadsaround.models.presentation.UserPresentation
import ru.salazarev.roadsaround.presentation.BaseViewModel
import ru.salazarev.roadsaround.presentation.members.MembersViewModel
import ru.salazarev.roadsaround.util.ImageConverter
import ru.salazarev.roadsaround.util.addTo
import javax.inject.Inject

/** ViewModel для фрагмента [MembersViewModel].
 * @param interactor - объект управления информацией о пользователях.
 * @param imageConverter - конвертер изображений.
 */
class ProfileViewModel @Inject constructor(
    private val interactor: UserInteractor,
    private val imageConverter: ImageConverter
) : BaseViewModel() {

    /** Прослушивание статуса загрузки информации о пользователе. */
    val user = MutableLiveData<UserPresentation?>()

    /** Прослушивание статуса загрузки. */
    val progress = MutableLiveData<Boolean>()

    /** Предоставление инфомрации о пользователе.*/
    fun loadUserData() {
        interactor.getUserData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { progress.value = false }
            .doOnSubscribe { progress.value = true }
            .subscribe(
                { user.value = getMembersInfo(it) },
                { user.value = null })
            .addTo(compositeDisposable)
    }

    private fun getMembersInfo(user: User): UserPresentation {
        val memberName =
            if (user.lastName.isEmpty()) user.firstName else "${user.firstName} ${user.lastName}"
        val image = if (user.image != null) imageConverter.convert(user.image) else null
        return UserPresentation(user.id, memberName, image)
    }
}