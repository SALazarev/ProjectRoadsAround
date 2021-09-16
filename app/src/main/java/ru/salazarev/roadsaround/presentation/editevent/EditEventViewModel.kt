package ru.salazarev.roadsaround.presentation.editevent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.salazarev.roadsaround.domain.event.EventInteractor
import ru.salazarev.roadsaround.models.domain.Event
import ru.salazarev.roadsaround.presentation.BaseViewModel
import ru.salazarev.roadsaround.util.addTo

class EditEventViewModel(
    private val interactor: EventInteractor,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {
    companion object{
        const val ROUTE_KEY = "ROUTE_KEY"
        const val TIME_KEY = "TIME_KEY"
        const val LOAD_STATUS_KEY = "LOAD_STATUS_KEY"
        const val ID_EVENT_KEY = "ID_EVENT_KEY"
        const val MEMBERS_KEY = "MEMBERS_KEY"
    }

    private val _result = MutableLiveData<Boolean>()
    val result: LiveData<Boolean> = _result

    val data = MutableLiveData<Event>()

    val progress = MutableLiveData<Boolean>()

    fun setRoute(route: String?) {
        savedStateHandle[ROUTE_KEY] = route
    }

    fun setIdEvent(idEvent: String) {
        savedStateHandle[ID_EVENT_KEY] = idEvent
    }

    fun setTime(time: Long?) {
        savedStateHandle[TIME_KEY] = time
    }

    fun setLoadStatus(loadStatus: Boolean) {
        savedStateHandle[LOAD_STATUS_KEY] = loadStatus
    }

    fun setMembers(members: List<String>) {
        savedStateHandle[MEMBERS_KEY] = members
    }

    fun getMembers(): List<String> = savedStateHandle.get(MEMBERS_KEY)?: listOf()
    fun getIdEvent(): String = savedStateHandle.get(ID_EVENT_KEY)?:""
    fun getTime(): Long? = savedStateHandle.get(TIME_KEY)
    fun getRoute(): String? = savedStateHandle.get(ROUTE_KEY)
    fun getLoadStatus(): Boolean = savedStateHandle.get(LOAD_STATUS_KEY)?:false
    fun createEvent(name: String, note: String, motionType: String, time: Long, route: String) {
        interactor.createEvent(getIdEvent(),name,note,motionType,time,route,getMembers())
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