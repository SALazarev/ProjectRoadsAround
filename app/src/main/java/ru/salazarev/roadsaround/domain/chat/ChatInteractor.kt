package ru.salazarev.roadsaround.domain.chat

import ru.salazarev.roadsaround.domain.user.UserRepository
import ru.salazarev.roadsaround.models.domain.User
import javax.inject.Inject

class ChatInteractor @Inject constructor(val userRepository: UserRepository) {


    fun sendMessage(textMessage: String){
//        val message = Message(textMessage)
//        chatRepository.sendMessage(message)
    }

    fun getChatMessages(){

    }

    fun getMessageWorkStatus() {

    }

    fun getUserData(): User? = userRepository.getUserData()
}