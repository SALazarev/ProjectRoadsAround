package ru.salazarev.roadsaround.presentation.routes

import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.salazarev.roadsaround.domain.event.EventInteractor
import ru.salazarev.roadsaround.models.presentation.EventPreview
import ru.salazarev.roadsaround.presentation.BaseViewModel
import ru.salazarev.roadsaround.util.addTo
import javax.inject.Inject

/** ViewModel для фрагмента [RoutesViewModel].
 * @param interactor - объект управления информацией о событиях.
 */
class RoutesViewModel @Inject constructor(
    private val interactor: EventInteractor
) : BaseViewModel() {

    /** Прослушивание статуса загрузки данных. */
    val eventsLiveData = MutableLiveData<List<EventPreview>>()

    /** Загрузка событий, в который участвует пользователь */
    fun loadUserEvents() {
        interactor.getUserEventPreviews().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(eventsLiveData::setValue) { eventsLiveData.value = null }
            .addTo(compositeDisposable)
    }
}