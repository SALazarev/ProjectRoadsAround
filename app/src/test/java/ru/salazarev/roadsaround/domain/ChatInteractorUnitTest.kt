package ru.salazarev.roadsaround.domain

import io.mockk.every
import io.mockk.mockk
import io.mockk.verifySequence
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import ru.salazarev.roadsaround.RxSchedulerRule
import ru.salazarev.roadsaround.domain.chat.ChatInteractor
import ru.salazarev.roadsaround.domain.chat.ChatRepository
import ru.salazarev.roadsaround.domain.user.UserRepository

class ChatInteractorUnitTest {

    companion object {
        const val ID_EVENT = "idEvent"
        const val ID_AUTHOR = "idAuthor"
        const val TEXT_MESSAGE = "text"
    }

    @Rule
    @JvmField
    val rxSchedulerRule = RxSchedulerRule()


    @Before

    @Test
    fun `send message`() {
        //Arrange


        val chatRepository: ChatRepository = mockk(relaxed = true)
        val userRepository: UserRepository = mockk()
        val authentication: ru.salazarev.roadsaround.network.Authentication = mockk {
            every { getUserId() } returns ID_AUTHOR
        }
        val interactor = ChatInteractor(chatRepository, userRepository, authentication)

        //Act
        val completable = interactor.sendMessage(ID_EVENT, TEXT_MESSAGE)

        //Assert
        completable.test().assertComplete()
        verifySequence {
            authentication.getUserId()
            interactor.sendMessage(ID_EVENT, TEXT_MESSAGE)
        }
    }

    @Test
    fun `get messages`() {
        //Arrange
        val idEvent = "idEvent"
        val chatRepository: ChatRepository = mockk(relaxed = true)
        val userRepository: UserRepository = mockk()
        val authentication: ru.salazarev.roadsaround.network.Authentication = mockk {
            //every { getUserId() } returns idAuthor
        }
        val interactor = ChatInteractor(chatRepository, userRepository, authentication)

        //Act
        val publicSubject = interactor.getChatMessages(idEvent)

        publicSubject.test().assertComplete()

        //Assert

    }

}

}