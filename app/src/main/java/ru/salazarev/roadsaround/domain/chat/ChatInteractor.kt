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
import java.lang.NullPointerException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class ChatInteractor @Inject constructor(
    private val chatRepository: ChatRepository,
    private val userInteractor: UserInteractor,
    private val auth: Authentication
) {
    fun sendMessage(idEvent: String, textMessage: String) {
        chatRepository.sendMessage(idEvent,auth.getUserId(), textMessage)
    }

    fun getChatMessages(idEvent: String):PublishSubject<List<Message>> {
        val localCallback = PublishSubject.create<List<MessageData>>()
        chatRepository.subscribeOnChatMessages(idEvent,localCallback)

        val callback = PublishSubject.create<List<Message>>()

        localCallback.subscribe({ list ->
            val idList = list.map { it.authorId }.distinct()
            val single: Single<List<User>> = Single.fromCallable {
                return@fromCallable userInteractor.getUsersData(idList)
            }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
            single.subscribe(
                { users ->
                    callback.onNext(
                        getMessages(
                            list.sortedBy { it.time!!.toDate().time },
                            users.map { it.id to it }.toMap()
                        )
                    )
                },
                {
                })

        }) {}
        return callback
    }

    private fun getMessages(data: List<MessageData>, users: Map<String, User>): List<Message> {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("HH:mm:ss dd/MM/yyyy", Locale.ROOT)

        return data.map {
            val user: User = users.getValue(it.authorId)
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