package ru.salazarev.roadsaround.presentation.eventinformation

import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.salazarev.roadsaround.domain.event.EventInteractor
import ru.salazarev.roadsaround.models.domain.Event
import ru.salazarev.roadsaround.models.presentation.EventPresentation
import ru.salazarev.roadsaround.models.presentation.UserPresentation
import ru.salazarev.roadsaround.presentation.BaseViewModel
import ru.salazarev.roadsaround.util.ImageConverter
import ru.salazarev.roadsaround.util.addTo
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


/** ViewModel для фрагмента [EventInformationFragment].
 * @param interactor - объект управления информацией о событиях.
 * @param imageConverter - конвертер изображений.
 */
class EventInformationViewModel @Inject constructor(
    private val interactor: EventInteractor,
    private val imageConverter: ImageConverter
) : BaseViewModel() {

    /** Прослушивание статуса загрузки событий. */
    val data = MutableLiveData<EventPresentation?>()

    /** Прослушивание статуса участия в событии. */
    val resultParticipate = MutableLiveData<Boolean>()
    /** Прослушивание статуса выхода из события. */
    val resultLeave = MutableLiveData<Boolean>()
    /** Прослушивание статуса загрузки. */
    val progress = MutableLiveData<Boolean>()


    /**
     * Предоставляет информацию о событии.
     * @param eventId - идентификатор события.
     */
    fun getEventData(eventId: String) {
        interactor.getEvent(eventId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { progress.value = false }
            .doOnSubscribe { progress.value = true }
            .subscribe(
                {
                    data.value = getEventInfo(it)
                },
                {
                    data.value = null
                }
            )
            .addTo(compositeDisposable)
    }

    private fun getEventInfo(event: Event): EventPresentation {
        val users = event.members.map {
            val memberName =
                if (it.lastName.isEmpty()) it.firstName else "${it.firstName} ${it.lastName}"
            val image = if (it.image != null) imageConverter.convert(it.image) else null
            UserPresentation(it.id, memberName, image)
        }

        val calendar = Calendar.getInstance()
        calendar.time = Date(event.time)
        val dateFormat = SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.ROOT)

        return EventPresentation(
            event.id,
            event.authorId,
            event.name,
            event.description,
            event.motionType,
            dateFormat.format(calendar.time),
            event.route,
            users
        )
    }

    /**
     * Устанавливает пользователя как участника события.
     * @param eventId - идентификатор события.
     */
    fun participateFromEvent(eventId: String) {
        interactor.addUserInEvent(eventId).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { resultParticipate.value = true },
                { resultParticipate.value = false }
            )
            .addTo(compositeDisposable)
    }

    /**
     * Освобождает пользователя от участия в событии.
     * @param eventId - идентификатор события.
     */
    fun leaveFromEvent(eventId: String) {
        interactor.leaveUserFromEvent(eventId).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { resultLeave.value = true },
                { resultLeave.value = false }
            )
            .addTo(compositeDisposable)
    }

}