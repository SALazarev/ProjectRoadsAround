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
import javax.inject.Inject

class EventInformationViewModel @Inject constructor(
    private val interactor: EventInteractor,
    private val imageConverter: ImageConverter
) : BaseViewModel() {

    val data = MutableLiveData<EventPresentation>()

    val progress = MutableLiveData<Boolean>()

    fun getEventData(eventId: String) {
        interactor.getEvent(eventId).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { progress.value = false }
            .doOnSubscribe { progress.value = true }
            .subscribe(
                { data.value = getEventInfo(it) },
                { data.value = null }
            )
            .addTo(compositeDisposable)
    }

    private fun getEventInfo(event: Event): EventPresentation {
        val users = event.members.map {
            val image = if (it.image != null) imageConverter.convert(it.image) else null
            UserPresentation(it.id,it.firstName, it.lastName, image)
        }
        return EventPresentation(
            event.id,
            event.authorId,
            event.name,
            event.note,
            event.motionType,
            event.time,
            event.route,
            users
        )
    }

}