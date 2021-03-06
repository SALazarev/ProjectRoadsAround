package ru.salazarev.roadsaround.presentation.chat

import androidx.lifecycle.MutableLiveData
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.salazarev.roadsaround.domain.chat.ChatInteractor
import ru.salazarev.roadsaround.models.domain.Message
import ru.salazarev.roadsaround.models.presentation.MessageChat
import ru.salazarev.roadsaround.presentation.BaseViewModel
import ru.salazarev.roadsaround.util.ImageConverter
import ru.salazarev.roadsaround.util.addTo

/** ViewModel для фрагмента [ChatFragment].
 * @param chatInteractor - объект управления информацией о сообщениях.
 * @param imageConverter - конвертер изображений.
 */
class ChatViewModel(
    private val chatInteractor: ChatInteractor,
    private val imageConverter: ImageConverter,
) : BaseViewModel() {
    /** Прослушивание статуса загрузки. */
    val progress = MutableLiveData<Boolean>()

    /** Прослушивание статуса работы. */
    val result = MutableLiveData<Boolean>()

    /** Прослушивание статуса загрузки сообщений. */
    val messages = MutableLiveData<List<MessageChat>>()

    /** Отпрака сообщения.
     * @param eventId - идентификатор события.
     * @param text - текст сообщения.
     */
    fun sendMessage(eventId: String, text: String) {
        chatInteractor.sendMessage(eventId, text)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { progress.value = false }
            .doOnSubscribe { progress.value = true }
            .subscribe(
                { result.value = true },
                { result.value = false }
            )
            .addTo(compositeDisposable)
    }

    /** Получение списка сообщений.
     * @param idEvent - идентификатор события.
     */
    fun getMessages(idEvent: String) {
        progress.value = true
        chatInteractor.getChatMessages(idEvent)
            .subscribe(
            {
                progress.postValue(false)
                getMessagesInLiveData(it)
            },
            {
                progress.postValue(false)
            }
        ).addTo(compositeDisposable)
    }

    private fun getMessagesInLiveData(data: List<Message>) {
        val result = data.map {
            val image = if (it.image != null) imageConverter.convert(it.image) else null
            MessageChat(
                it.id,
                it.idAuthor,
                it.nameAuthor,
                it.text,
                it.time,
                image
            )
        }
        messages.postValue(result)
    }
}