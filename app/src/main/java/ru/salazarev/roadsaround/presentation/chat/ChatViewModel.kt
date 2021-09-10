package ru.salazarev.roadsaround.presentation.chat

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import ru.salazarev.roadsaround.domain.chat.ChatInteractor
import ru.salazarev.roadsaround.models.domain.Message
import ru.salazarev.roadsaround.models.presentation.MessageChat
import ru.salazarev.roadsaround.util.ImageConverter

class ChatViewModel(
    private val chatInteractor: ChatInteractor,
    private val imageConverter: ImageConverter,
) : ViewModel() {
    val progress = MutableLiveData<Boolean>()
    val result = MutableLiveData<Boolean>()
    val messages = MutableLiveData<List<MessageChat>>()
    val test = MutableLiveData<String>()

    fun sendMessage(text: String) {
        val completable = Completable.fromCallable {
            return@fromCallable chatInteractor.sendMessage(text)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { progress.value = false }
            .doOnSubscribe { progress.value = true }

        completable.subscribe({ result.value = true }) { result.value = false }
    }

    fun getMessages() {
        val callback = PublishSubject.create<List<Message>>()
        callback.subscribe(::getMessagesInLiveData)
        chatInteractor.getChatMessages(callback)
    }

    private fun getMessagesInLiveData(data: List<Message>){
        messages.value = data.map{
            val image =  if (it.image!=null) imageConverter.convert(it.image) else null
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