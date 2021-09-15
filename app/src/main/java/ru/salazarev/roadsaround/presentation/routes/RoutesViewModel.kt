package ru.salazarev.roadsaround.presentation.routes

import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.salazarev.roadsaround.domain.event.EventInteractor
import ru.salazarev.roadsaround.models.presentation.Event
import ru.salazarev.roadsaround.presentation.common.BaseViewModel
import ru.salazarev.roadsaround.util.addTo
import javax.inject.Inject

class RoutesViewModel @Inject constructor(
    private val interactor: EventInteractor
) : BaseViewModel() {

    val userLiveData = MutableLiveData<List<Event>>()

    val progress = MutableLiveData<Boolean>()

    init {
        loadQuotationList()
    }

    private fun loadQuotationList() {
        interactor.getUsersEvents().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { progress.value = false }
            .doOnSubscribe { progress.value = true }
            .subscribe(userLiveData::setValue) { userLiveData.value = null }
            .addTo(compositeDisposable)
    }
}