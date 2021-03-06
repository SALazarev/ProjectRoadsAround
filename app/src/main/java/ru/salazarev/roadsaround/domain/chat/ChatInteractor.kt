package ru.salazarev.roadsaround.domain.chat

import io.reactivex.rxjava3.core.*
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.salazarev.roadsaround.network.Authentication
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
 *  @property userRepository - интерактор работы с информацией о пользователях.
 *  @property authentication - предоставляет информацию о статусе авторизации и об авторизованном пользователе.
 */
class ChatInteractor @Inject constructor(
    private val chatRepository: ChatRepository,
    private val userRepository: UserRepository,
    private val authentication: Authentication
) {

    /**
     * Формирует пользовательское сообщение и отправляет в хранилище.
     * @param idEvent - идентификатор сообщения.
     * @param textMessage - текст сообщения.
     * @return объект для прослушивания получения информации о сообщении.
     */
    fun sendMessage(idEvent: String, textMessage: String): Completable = chatRepository.sendMessage(
        idEvent,
        authentication.getUserId(),
        textMessage
    )

    /**
     * Предоставляет все сообщения в чате события.
     * @param idEvent - идентификатор события.
     * @return объект для прослушивания получения информации о сообщениях.
     */
    fun getChatMessages(idEvent: String): Observable<List<Message>> {
        return chatRepository.subscribeOnChatMessages(idEvent)
            .observeOn(Schedulers.io())
            .map { list ->
                val idList = list.map { it.authorId }.distinct()
                val users = userRepository.getUsersData(idList)
                val usersMap =  users.map { it.id to it }.toMap()
                val messagesData =  list.sortedBy { it.time!!.toDate().time }
                getMessages(messagesData,usersMap)
            }
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