package ru.salazarev.roadsaround.presentation.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.salazarev.roadsaround.data.user.UserRepositoryImpl
import ru.salazarev.roadsaround.domain.chat.ChatInteractor
import ru.salazarev.roadsaround.domain.user.UserInteractor
import ru.salazarev.roadsaround.models.presentation.User

class ChatViewModel(
    private val chatInteractor: ChatInteractor,
    private val userInteractor: UserInteractor
) : ViewModel() {
    fun sendMessage(text: String) {
        chatInteractor.sendMessage(text)
    }

    val userLiveData = MutableLiveData<User>()
    val workStatus: LiveData<UserRepositoryImpl.WorkStatus> = userInteractor.getMessageWorkStatus()
//    val chatLiveData: LiveData<User> = interactor.getChatMessages()
//    val workStatus: LiveData<UserRepositoryImpl.WorkStatus> = interactor.getMessageWorkStatus()

    /**
     * Наблюдаемое хранилище состояния загрузки данных.
     */
    val progress = MutableLiveData<Boolean>()

    /**
     * Наблюдаемое хранилище ошибки.
     */
    var errors = MutableLiveData<Throwable>()

    init {
        loadQuotationList()
    }

    private fun loadQuotationList() {
        val user: Single<User?> = Single.fromCallable {
            return@fromCallable chatInteractor.getUserData()
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { progress.value = false }
            .doOnSubscribe { progress.value = true }

        user.subscribe(userLiveData::setValue, errors::setValue)
    }
}