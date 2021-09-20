package ru.salazarev.roadsaround.presentation.searchevent

import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.salazarev.roadsaround.domain.event.EventInteractor
import ru.salazarev.roadsaround.models.presentation.EventPreview
import ru.salazarev.roadsaround.presentation.BaseViewModel
import ru.salazarev.roadsaround.util.addTo
import javax.inject.Inject

/** Фабрика ViewModel для фрагмента [SearchEventViewModel].
 * @param interactor - объект управления информацией о событиях.
 */
class SearchEventViewModel @Inject constructor(
    private val interactor: EventInteractor
) : BaseViewModel() {

    /** Прослушивание статуса загрузки данных. */
    val eventsLiveData = MutableLiveData<List<EventPreview>>()

    /** Загрузка событий других пользователей. */
    fun loadUsersEventsList() {
        interactor.getUsersEventPreviews().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(eventsLiveData::setValue) { eventsLiveData.value = null }
            .addTo(compositeDisposable)
    }
}