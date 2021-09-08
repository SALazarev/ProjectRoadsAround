package ru.salazarev.roadsaround.domain.chat

import ru.salazarev.roadsaround.domain.user.Authentication
import ru.salazarev.roadsaround.domain.user.UserRepository
import ru.salazarev.roadsaround.models.domain.Message
import javax.inject.Inject

class ChatInteractor @Inject constructor(
    private val chatRepository: ChatRepository,
    private val userRepository: UserRepository,
    private val auth: Authentication
) {


    fun sendMessage(textMessage: String) {
        chatRepository.sendMessage(auth.getUserId(),textMessage)
    }

    fun getChatMessages() {

    }

    fun getMessageWorkStatus() {

    }
}