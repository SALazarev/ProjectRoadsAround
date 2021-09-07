package ru.salazarev.roadsaround.domain.chat

import android.service.autofill.UserData
import ru.salazarev.roadsaround.domain.user.UserRepository
import ru.salazarev.roadsaround.models.presentation.Message
import ru.salazarev.roadsaround.models.presentation.User
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