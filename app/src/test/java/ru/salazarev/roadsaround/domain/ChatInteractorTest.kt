package ru.salazarev.roadsaround.domain

import io.mockk.every
import io.mockk.mockk
import io.mockk.verifySequence
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.subjects.PublishSubject
import org.junit.Rule
import org.junit.Test
import ru.salazarev.roadsaround.RxSchedulerRule
import ru.salazarev.roadsaround.domain.chat.ChatInteractor
import ru.salazarev.roadsaround.domain.chat.ChatRepository
import ru.salazarev.roadsaround.domain.user.UserRepository
import ru.salazarev.roadsaround.models.data.MessageData
import ru.salazarev.roadsaround.models.domain.User

class ChatInteractorTest {

    companion object {
        const val ID_EVENT = "idEvent"
        const val ID_AUTHOR = "idAuthor"
        const val TEXT_MESSAGE = "text"
        const val FIRST_NAME = "FIRST_NAME"
        const val LAST_NAME = "LAST_NAME"
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

    @Test
    fun `get messages onNext 2`() {
        //Arrange
        val listOfMessageData = listOf(MessageData(authorId = ID_AUTHOR))
        val listId = listOf(ID_AUTHOR)
        val listUser = listOf(User(ID_AUTHOR, FIRST_NAME, LAST_NAME, null))
        every {userRepository.getUsersData(listId)} returns listUser
       // every{chatRepository.subscribeOnChatMessages(ID_EVENT)} returns PublishSubject.create()

        //Act
        val testPS =  chatRepository.subscribeOnChatMessages(ID_EVENT).blockingFirst()

        val publicSubject =   interactor.getChatMessages(ID_EVENT)
        publicSubject.test().assertResult()



        //Assert
        publicSubject.test().assertComplete()
        verifySequence {
            chatRepository.subscribeOnChatMessages(ID_EVENT)
        }
    }

}
