package ru.salazarev.roadsaround.presentation.editevent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.salazarev.roadsaround.domain.event.EventInteractor
import ru.salazarev.roadsaround.models.domain.Event
import ru.salazarev.roadsaround.presentation.BaseViewModel
import ru.salazarev.roadsaround.presentation.chat.ChatFragment
import ru.salazarev.roadsaround.util.addTo

/** ViewModel для фрагмента [EditEventFragment].
 * @param interactor - объект управления информацией о событиях.
 * @param savedStateHandle - объект хранения информации в текущей ViewModel.
 */
class EditEventViewModel(
    private val interactor: EventInteractor,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    companion object {
        private const val ROUTE_KEY = "ROUTE_KEY"
        private const val TIME_KEY = "TIME_KEY"
        private const val LOAD_STATUS_KEY = "LOAD_STATUS_KEY"
        private const val ID_EVENT_KEY = "ID_EVENT_KEY"
        private const val MEMBERS_KEY = "MEMBERS_KEY"
    }

    private val _result = MutableLiveData<Boolean>()

    /** Прослушивание результата работы. */
    val result: LiveData<Boolean> = _result

    /** Прослушивание статуса загрузки данных. */
    val data = MutableLiveData<Event>()

    /** Прослушивание статуса загрузки. */
    val progress = MutableLiveData<Boolean>()

    /** Сохранение маршрута в [SavedStateHandle].
     * @param route - маршрут.
     */
    fun setRoute(route: String?) {
        savedStateHandle[ROUTE_KEY] = route
    }

    /** Сохранение идентификатора события в [SavedStateHandle].
     * @param idEvent - идентификатор события.
     */
    fun setIdEvent(idEvent: String) {
        savedStateHandle[ID_EVENT_KEY] = idEvent
    }

    /** Сохранение времени события в [SavedStateHandle].
     * @param time - время события.
     */
    fun setTime(time: Long?) {
        savedStateHandle[TIME_KEY] = time
    }

    /** Сохранение  статуса загрузки данных в [SavedStateHandle].
     * @param loadStatus - статус загрузки данных.
     */
    fun setLoadStatus(loadStatus: Boolean) {
        savedStateHandle[LOAD_STATUS_KEY] = loadStatus
    }

    /** Сохранение участников события в [SavedStateHandle].
     * @param members - участники события.
     */
    fun setMembers(members: List<String>) {
        savedStateHandle[MEMBERS_KEY] = members
    }

    /** Предоставление участников события из [SavedStateHandle].
     * @param members - участники события.
     */
    fun getMembers(): List<String> = savedStateHandle.get(MEMBERS_KEY) ?: listOf()


    fun getIdEvent(): String = savedStateHandle.get(ID_EVENT_KEY) ?: ""
    fun getTime(): Long? = savedStateHandle.get(TIME_KEY)
    fun getRoute(): String? = savedStateHandle.get(ROUTE_KEY)
    fun getLoadStatus(): Boolean = savedStateHandle.get(LOAD_STATUS_KEY) ?: false
    fun createEvent(name: String, note: String, motionType: String, time: Long, route: String) {
        interactor.createEvent(getIdEvent(), name, note, motionType, time, route, getMembers())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { _result.value = true },
                { _result.value = false }
            )
            .addTo(compositeDisposable)
    }

    fun getEventData(eventId: String) {
        interactor.getEvent(eventId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { progress.value = false }
            .doOnSubscribe { progress.value = true }
            .subscribe(
                {
                    data.value = it
                },
                {
                    data.value = null
                }
            )
            .addTo(compositeDisposable)
    }

}