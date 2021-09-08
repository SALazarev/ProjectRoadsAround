package ru.salazarev.roadsaround.presentation.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.salazarev.roadsaround.domain.chat.ChatInteractor

class ChatViewModel(
    private val chatInteractor: ChatInteractor,
) : ViewModel() {
    val progress = MutableLiveData<Boolean>()
    val result = MutableLiveData<Boolean>()

    fun sendMessage(text: String) {
        val user = Completable.fromCallable {
            return@fromCallable chatInteractor.sendMessage(text)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { progress.value = false }
            .doOnSubscribe { progress.value = true }

        user.subscribe({ result.value = true }) { result.value = false }
    }
}