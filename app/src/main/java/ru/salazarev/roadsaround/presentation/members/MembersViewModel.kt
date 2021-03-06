package ru.salazarev.roadsaround.presentation.members

import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.salazarev.roadsaround.domain.event.EventInteractor
import ru.salazarev.roadsaround.models.domain.User
import ru.salazarev.roadsaround.models.presentation.UserPresentation
import ru.salazarev.roadsaround.presentation.BaseViewModel
import ru.salazarev.roadsaround.presentation.eventinformation.EventInformationFragment
import ru.salazarev.roadsaround.util.ImageConverter
import ru.salazarev.roadsaround.util.addTo
import javax.inject.Inject


/** ViewModel для фрагмента [EventInformationFragment].
 * @param interactor - объект управления информацией о событиях.
 * @param imageConverter - конвертер изображений.
 */
class MembersViewModel @Inject constructor(
    private val interactor: EventInteractor,
    private val imageConverter: ImageConverter
) : BaseViewModel() {

    /** Прослушивание статуса загрузки участников события. */
    val members = MutableLiveData<List<UserPresentation>>()

    /** Прослушивание статуса загрузки. */
    val progress = MutableLiveData<Boolean>()

    /** Предоставляет информацию об участниках в событии.
     * @param eventId - идентификатор события.
     */
    fun getMembers(eventId: String) {
        interactor.getMembersEvent(eventId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { progress.value = false }
            .doOnSubscribe { progress.value = true }
            .subscribe(
                { members.value = getMembersInfo(it) },
                { members.value = null }
            )
            .addTo(compositeDisposable)
    }

    private fun getMembersInfo(members: List<User>): List<UserPresentation> {
        return members.map {
            val memberName =
                if (it.lastName.isEmpty()) it.firstName else "${it.firstName} ${it.lastName}"
            val image = if (it.image != null) imageConverter.convert(it.image) else null
            UserPresentation(it.id, memberName, image)
        }
    }
}