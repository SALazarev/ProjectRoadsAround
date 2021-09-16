package ru.salazarev.roadsaround.presentation.editevent

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.salazarev.roadsaround.domain.event.EventInteractor
import ru.salazarev.roadsaround.presentation.BaseViewModel
import ru.salazarev.roadsaround.util.addTo

class EditEventViewModel(
    private val interactor: EventInteractor,
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {
    companion object{
        const val ROUTE_KEY = "ROUTE_KEY"
        const val TIME_KEY = "TIME_KEY"
    }

    private val _result = MutableLiveData<Boolean>()
    val result: LiveData<Boolean> = _result

    fun setRoute(route: String?) {
        savedStateHandle[ROUTE_KEY] = route
    }

    fun setTime(time: Long?) {
        savedStateHandle[TIME_KEY] = time
    }

    fun getTime(): Long? = savedStateHandle.get(TIME_KEY)
    fun getRoute(): String? = savedStateHandle.get(ROUTE_KEY)
    fun createEvent(name: String, note: String, motionType: String, time: Long, route: String) {
        interactor.createEvent(name,note,motionType,time,route).subscribeOn(
            Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())

            .subscribe(
                { _result.value = true },
                { _result.value = false }
            )
            .addTo(compositeDisposable)
    }

}