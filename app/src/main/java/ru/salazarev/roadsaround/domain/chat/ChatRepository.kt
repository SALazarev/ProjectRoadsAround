package ru.salazarev.roadsaround.domain.chat

import io.reactivex.rxjava3.subjects.PublishSubject
import ru.salazarev.roadsaround.models.data.MessageData

interface ChatRepository {
    fun sendMessage(eventId: String, authorId: String, textMessage: String)
    fun subscribeOnChatMessages(eventId: String, callback: PublishSubject<List<MessageData>>)
}