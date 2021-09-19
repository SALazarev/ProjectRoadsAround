package ru.salazarev.roadsaround.domain

import io.mockk.every
import io.mockk.mockk
import io.mockk.verifySequence
import io.reactivex.rxjava3.core.Completable
import org.junit.Rule
import org.junit.Test
import ru.salazarev.roadsaround.RxSchedulerRule
import ru.salazarev.roadsaround.domain.chat.ChatInteractor
import ru.salazarev.roadsaround.domain.chat.ChatRepository
import ru.salazarev.roadsaround.domain.user.UserRepository
import ru.salazarev.roadsaround.models.data.MessageData

class ChatInteractorTest {

    companion object {
        const val ID_EVENT = "idEvent"
        const val ID_AUTHOR = "idAuthor"
        const val TEXT_MESSAGE = "text"
    }

    @Rule
    @JvmField
    val rxSchedulerRule = RxSchedulerRule()

    private val chatRepository: ChatRepository = mockk(relaxed = true)
    private val userRepository: UserRepository = mockk(relaxed = true)
    private val authentication: ru.salazarev.roadsaround.network.Authentication = mockk(relaxed = true)
    private val interactor = ChatInteractor(chatRepository, userRepository, authentication)

    @Test
    fun `send message complete`() {
        //Arrange
        every { authentication.getUserId() } returns ID_AUTHOR

        //Act
        val completable = interactor.sendMessage(ID_EVENT, TEXT_MESSAGE)

        //Assert
        completable.test().assertComplete()
        verifySequence {
            authentication.getUserId()
            chatRepository.sendMessage(ID_EVENT, ID_AUTHOR, TEXT_MESSAGE)
        }
    }

    @Test
    fun `send message error`() {
        //Arrange
        val exception = Exception()
        every { authentication.getUserId() } returns ID_AUTHOR
        every{ chatRepository.sendMessage(ID_EVENT, ID_AUTHOR, TEXT_MESSAGE) } returns Completable.error(exception)

        //Act
        val completable = interactor.sendMessage(ID_EVENT, TEXT_MESSAGE)

        //Assert
        completable.test().assertError(exception)
        verifySequence {
            authentication.getUserId()
            chatRepository.sendMessage(ID_EVENT, ID_AUTHOR, TEXT_MESSAGE)
        }
    }

    @Test
    fun `get messages onNext`() {
        //Arrange
        val listOfMessage = listOf(MessageData())

        //Act
        val publicSubject = chatRepository.subscribeOnChatMessages(ID_EVENT)
        publicSubject.onNext(listOfMessage)

        //Assert
        publicSubject.test().assertComplete()
        verifySequence {
            chatRepository.subscribeOnChatMessages(ID_EVENT)
        }
    }

}
