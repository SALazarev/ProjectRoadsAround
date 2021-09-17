package ru.salazarev.roadsaround.domain.chat

import io.reactivex.rxjava3.subjects.PublishSubject
import ru.salazarev.roadsaround.models.data.MessageData

/** Интерфейс хранилища данных о сообщениях в чате события. */
interface ChatRepository {

    /**
     * Отправка сообщения в хранилище.
     * @param eventId - идентификатор события.
     * @param authorId - идентификатор автора сообщения.
     * @param textMessage - текст сообщения.
     */
    fun sendMessage(eventId: String, authorId: String, textMessage: String)

    /**
     * Подписка на прослушивание чата события.
     * @param eventId - идентификатор события.
     * @param - прослушиватель изменения информации о сообщениях в чате.
     */
    fun subscribeOnChatMessages(eventId: String, callback: PublishSubject<List<MessageData>>)
}