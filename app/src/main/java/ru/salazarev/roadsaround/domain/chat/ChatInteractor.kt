package ru.salazarev.roadsaround.domain.chat

import ru.salazarev.roadsaround.models.domain.User
import javax.inject.Inject

class ChatInteractor @Inject constructor(val chatRepository: ChatRepository) {


    fun sendMessage(textMessage: String){
//        val message = Message(textMessage)
//        chatRepository.sendMessage(message)
    }

    fun getChatMessages(){

    }

    fun getMessageWorkStatus() {

    }

    fun getUserData(): User? = chatRepository.getUserData()
}