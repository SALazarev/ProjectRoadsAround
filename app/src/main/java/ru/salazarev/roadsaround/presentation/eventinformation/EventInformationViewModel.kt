package ru.salazarev.roadsaround.presentation.eventinformation

import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.salazarev.roadsaround.domain.event.EventInteractor
import ru.salazarev.roadsaround.models.presentation.EventInformation
import ru.salazarev.roadsaround.presentation.common.BaseViewModel
import ru.salazarev.roadsaround.util.addTo
import javax.inject.Inject

class EventInformationViewModel @Inject constructor(
    private val interactor: EventInteractor
) : BaseViewModel() {

    val data = MutableLiveData<List<EventInformation>>()

    val progress = MutableLiveData<Boolean>()

    init {
       // loadEventList()
    }

    private fun loadEventList() {
        interactor.getEvent().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { progress.value = false }
            .doOnSubscribe { progress.value = true }
            .subscribe(data::setValue) { data.value = null }
            .addTo(compositeDisposable)
    }
}