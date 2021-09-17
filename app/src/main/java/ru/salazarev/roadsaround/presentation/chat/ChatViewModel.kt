package ru.salazarev.roadsaround.presentation.chat

import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.salazarev.roadsaround.domain.chat.ChatInteractor
import ru.salazarev.roadsaround.models.domain.Message
import ru.salazarev.roadsaround.models.presentation.MessageChat
import ru.salazarev.roadsaround.presentation.BaseViewModel
import ru.salazarev.roadsaround.util.ImageConverter
import ru.salazarev.roadsaround.util.addTo

class ChatViewModel(
    private val chatInteractor: ChatInteractor,
    private val imageConverter: ImageConverter,
) : BaseViewModel() {
    val progress = MutableLiveData<Boolean>()
    val result = MutableLiveData<Boolean>()
    val messages = MutableLiveData<List<MessageChat>>()

    fun sendMessage(eventId: String, text: String) {
        val completable = Completable.fromCallable {
            return@fromCallable chatInteractor.sendMessage(eventId, text)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { progress.value = false }
            .doOnSubscribe { progress.value = true }

        completable.subscribe({ result.value = true }) { result.value = false }
    }

    fun getMessages(idEvent: String) {
        progress.value = true
        chatInteractor.getChatMessages(idEvent).subscribe(
            {
                progress.value = false
                getMessagesInLiveData(it)
            },
            {
                progress.value = false
            }
        ).addTo(compositeDisposable)
    }

    private fun getMessagesInLiveData(data: List<Message>) {
        messages.value = data.map {
            val image = if (it.image != null) imageConverter.convert(it.image) else null
            MessageChat(
                it.id,
                it.idAuthor,
                it.name,
                it.message,
                it.time,
                image
            )
        }
    }
}