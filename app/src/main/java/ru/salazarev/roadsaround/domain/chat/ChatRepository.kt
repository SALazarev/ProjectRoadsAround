package ru.salazarev.roadsaround.domain.chat

import io.reactivex.rxjava3.core.Observable
import ru.salazarev.roadsaround.models.data.MessageData

interface ChatRepository {
    fun sendMessage(authorId: String, textMessage: String)
    fun getChatMessages(listener: ChatRepoListener): Observable<List<MessageData>>
    fun test(): Observable<String>
}