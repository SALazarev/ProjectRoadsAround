package ru.salazarev.roadsaround.domain.chat

interface ChatRepository {
    fun sendMessage(authorId: String, textMessage: String)
    fun getChatMessages()
}