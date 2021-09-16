package ru.salazarev.roadsaround.presentation.searchevent

import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.salazarev.roadsaround.domain.event.EventInteractor
import ru.salazarev.roadsaround.models.presentation.EventPreview
import ru.salazarev.roadsaround.presentation.BaseViewModel
import ru.salazarev.roadsaround.util.addTo
import javax.inject.Inject

class SearchEventViewModel @Inject constructor(
    private val interactor: EventInteractor
) : BaseViewModel() {

    val eventsLiveData = MutableLiveData<List<EventPreview>>()

    val progress = MutableLiveData<Boolean>()

    init {
        loadEventList()
    }

    private fun loadEventList() {
        interactor.getUsersEventsPreview().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { progress.value = false }
            .doOnSubscribe { progress.value = true }
            .subscribe(eventsLiveData::setValue) { eventsLiveData.value = null }
            .addTo(compositeDisposable)
    }
}