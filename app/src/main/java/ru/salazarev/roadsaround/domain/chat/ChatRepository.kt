package ru.salazarev.roadsaround.domain.chat

import ru.salazarev.roadsaround.models.domain.Message
import ru.salazarev.roadsaround.models.domain.User

interface ChatRepository {
    fun sendMessage(message: Message)
    fun getChatMessages()
    fun getMessageWorkStatus()
}