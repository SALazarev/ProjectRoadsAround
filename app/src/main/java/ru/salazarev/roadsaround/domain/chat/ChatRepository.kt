package ru.salazarev.roadsaround.domain.chat

import ru.salazarev.roadsaround.models.presentation.Message
import ru.salazarev.roadsaround.models.presentation.User

interface ChatRepository {
    fun sendMessage(message: Message)
    fun getChatMessages()
    fun getMessageWorkStatus()
    fun getUserData(): User?
}