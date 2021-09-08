package ru.salazarev.roadsaround.presentation.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.salazarev.roadsaround.domain.chat.ChatInteractor
import ru.salazarev.roadsaround.domain.user.UserInteractor
import ru.salazarev.roadsaround.models.presentation.UserChat
import ru.salazarev.roadsaround.util.ImageConverter

class ChatViewModel(
    private val chatInteractor: ChatInteractor,
    private val userInteractor: UserInteractor,
    private val imageConverter: ImageConverter
) : ViewModel() {
    fun sendMessage(text: String) {
        chatInteractor.sendMessage(text)
    }

    val user = MutableLiveData<UserChat?>()

    /**
     * Наблюдаемое хранилище состояния загрузки данных.
     */
    val progress = MutableLiveData<Boolean>()

    init {
        loadQuotationList()
    }

    private fun loadQuotationList() {
        val user: Single<UserChat?> = Single.fromCallable {
            return@fromCallable userInteractor.getUserData()
        }
            .map { user -> UserChat(user!!.firstName, user.lastName,
                if (user.image != null) imageConverter.convert(user.image) else null) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { progress.value = false }
            .doOnSubscribe { progress.value = true }

        user.subscribe(this.user::setValue) { this.user.value = null }
    }


}