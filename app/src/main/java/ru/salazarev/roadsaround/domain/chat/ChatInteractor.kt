package ru.salazarev.roadsaround.domain.chat

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import ru.salazarev.roadsaround.domain.user.Authentication
import ru.salazarev.roadsaround.domain.user.UserInteractor
import ru.salazarev.roadsaround.models.data.MessageData
import ru.salazarev.roadsaround.models.domain.Message
import ru.salazarev.roadsaround.models.domain.User
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ChatInteractor @Inject constructor(
    private val chatRepository: ChatRepository,
    private val userInteractor: UserInteractor,
    private val auth: Authentication
) {
    fun sendMessage(textMessage: String) {
        chatRepository.sendMessage(auth.getUserId(), textMessage)
    }

    fun getChatMessages(callback: PublishSubject<List<Message>>) {
        val localCallback = PublishSubject.create<List<MessageData>>()
        localCallback.subscribe({ list ->
            val single: Single<User> = Single.fromCallable {
                return@fromCallable userInteractor.getUserData()
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
            single.subscribe({ callback.onNext(getMessages(list, it)) }) {}
        }){}
        chatRepository.getChatMessages(localCallback)
    }

    private fun getMessages(data: List<MessageData>, user: User): List<Message> {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("HH:mm:ss dd/MM/yyyy", Locale.ROOT)
        return data.map {
            calendar.time = it.time!!.toDate()
            val name =
                if (user.lastName == "") user.firstName else "${user.firstName} ${user.lastName}"
            Message(
                it.id,
                it.authorId,
                name,
                it.text,
                dateFormat.format(calendar.time),
                user.image
            )
        }
    }
}