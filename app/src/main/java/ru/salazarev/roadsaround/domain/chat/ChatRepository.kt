package ru.salazarev.roadsaround.domain.chat

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import ru.salazarev.roadsaround.models.data.MessageData

interface ChatRepository {
    fun sendMessage(authorId: String, textMessage: String)
    fun subscribeOnChatMessages(callback: PublishSubject<List<MessageData>>)
}