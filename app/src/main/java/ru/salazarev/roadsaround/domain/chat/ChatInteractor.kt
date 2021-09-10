package ru.salazarev.roadsaround.domain.chat

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.AsyncSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import ru.salazarev.roadsaround.domain.user.Authentication
import ru.salazarev.roadsaround.domain.user.UserInteractor
import ru.salazarev.roadsaround.models.data.MessageData
import ru.salazarev.roadsaround.models.domain.Message
import javax.inject.Inject

class ChatInteractor @Inject constructor(
    private val chatRepository: ChatRepository,
    private val userInteractor: UserInteractor,
    private val auth: Authentication
) {


    fun sendMessage(textMessage: String) {
        chatRepository.sendMessage(auth.getUserId(), textMessage)
    }

    fun getChatMessages(callback: ChatDomainListener) {
        chatRepository.getChatMessages(object : ChatRepoListener {
            override fun getData(data: List<MessageData>) {
                val newData: List<Message> = data.map {
                    val user = userInteractor.getUserData(it.id)
                    val name =
                        if (user.lastName == "") user.firstName else "${user.firstName} ${user.lastName}"
                    Message(
                        it.id,
                        it.authorId,
                        name,
                        it.text,
                        it.time!!.toDate().toString(),
                        user.image
                    )
                }
                callback.getData(newData)
            }
        })
    }

    fun test() = chatRepository.test()

}