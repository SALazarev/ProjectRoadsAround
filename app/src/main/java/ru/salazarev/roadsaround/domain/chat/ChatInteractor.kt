package ru.salazarev.roadsaround.domain.chat

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.schedulers.Schedulers
import io.reactivex.rxjava3.subjects.PublishSubject
import ru.salazarev.roadsaround.network.Authentication
import ru.salazarev.roadsaround.domain.user.UserInteractor
import ru.salazarev.roadsaround.domain.user.UserRepository
import ru.salazarev.roadsaround.models.data.MessageData
import ru.salazarev.roadsaround.models.domain.Message
import ru.salazarev.roadsaround.models.domain.User
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

/**
 * Класс, устанавливающий взаимодействие между уровнями хранения данных и представления.
 *  Предназначен для работы с чатами пользователей в событии.
 *
 *  @property chatRepository - репозиторий сообщений.
 *  @property userInteractor - интерактор работы с информацией о пользователях.
 *  @property authentication - предоставляет информацию о статусе авторизации и об авторизованном пользователе.
 */
class ChatInteractor @Inject constructor(
    private val chatRepository: ChatRepository,
    private val userRepository: UserRepository,
    private val authentication: Authentication
) {

    /**
     * Формирует пользовательское сообщение и отправляет в хранилище.
     * @param id - идентификатор сообщения.
     * @param textMessage - текст сообщения.
     */
    fun sendMessage(idEvent: String, textMessage: String) {
        chatRepository.sendMessage(idEvent, authentication.getUserId(), textMessage)
    }

    /**
     * Предоставляет все сообщения в чате события.
     * @param idEvent - идентификатор события.
     * @return объект для прослушивания получения информации о сообщениях.
     */
    fun getChatMessages(idEvent: String): PublishSubject<List<Message>> {
        val localCallback = PublishSubject.create<List<MessageData>>()
        chatRepository.subscribeOnChatMessages(idEvent, localCallback)

        val callback = PublishSubject.create<List<Message>>()

        localCallback.subscribe({ list ->
            val idList = list.map { it.authorId }.distinct()
            val single: Single<List<User>> = Single.fromCallable {
                return@fromCallable userRepository.getUsersData(idList)
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
                }, {})

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
                if (user.lastName.isEmpty()) user.firstName else "${user.firstName} ${user.lastName}"
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