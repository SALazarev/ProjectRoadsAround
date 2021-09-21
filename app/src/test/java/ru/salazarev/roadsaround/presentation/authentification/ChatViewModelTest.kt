package ru.salazarev.roadsaround.presentation.authentification

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyAll
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import org.junit.Rule
import org.junit.Test
import ru.salazarev.roadsaround.RxSchedulerRule
import ru.salazarev.roadsaround.domain.ChatInteractorTest
import ru.salazarev.roadsaround.domain.chat.ChatInteractor
import ru.salazarev.roadsaround.models.domain.Message
import ru.salazarev.roadsaround.models.presentation.MessageChat
import ru.salazarev.roadsaround.presentation.chat.ChatViewModel
import ru.salazarev.roadsaround.util.ImageConverter

class ChatViewModelTest {

    private companion object {
        private const val EMAIL = "email"
        private const val PASSWORD = "password"
        private const val ID_EVENT = "ID_EVENT"
        private const val TEXT = "TEXT"
        const val ID_AUTHOR = "idAuthor"
        const val TEXT_MESSAGE = "text"
        const val FIRST_NAME = "FIRST_NAME"
        const val LAST_NAME = "LAST_NAME"
        const val ID_MESSAGE = "ID_MESSAGE"
        const val TIME = "TIME"
    }

    @Rule
    @JvmField
    val rxSchedulerRule = RxSchedulerRule()

    @Rule
    @JvmField
    val liveDataTestRule = InstantTaskExecutorRule()

    private val interactor: ChatInteractor = mockk()

    private val imageChatInteractor: ImageConverter = mockk()

    private val viewModel: ChatViewModel = ChatViewModel(interactor, imageChatInteractor)

    private val exception = Exception()

    @Test
    fun `send message complete`() {
        //Arrange
        every { interactor.sendMessage(ID_EVENT, TEXT) } returns Completable.complete()
        val resultObserver: Observer<Boolean?> = mockk(relaxed = true)
        viewModel.result.observeForever(resultObserver)

        //Act
        viewModel.sendMessage(ID_EVENT, TEXT)

        //Assert
        verifyAll { resultObserver.onChanged(true) }
    }

    @Test
    fun `send message error`() {
        //Arrange
        every { interactor.sendMessage(ID_EVENT, TEXT) } returns Completable.error(exception)
        val resultObserver: Observer<Boolean?> = mockk(relaxed = true)
        viewModel.result.observeForever(resultObserver)

        //Act
        viewModel.sendMessage(ID_EVENT, TEXT)

        //Assert
        verifyAll { resultObserver.onChanged(false) }
    }

    @Test
    fun `get messages`() {
        //Arrange
        val resultObserver: Observer<List<MessageChat>> = mockk(relaxed = true)
        viewModel.messages.observeForever(resultObserver)
        val name = "${FIRST_NAME} ${LAST_NAME}"
        val listMessage = listOf(
            Message(
                ID_MESSAGE, ID_AUTHOR, name,
                TEXT, TIME, null
            )
        )
        every { interactor.getChatMessages(ID_EVENT) } returns Observable.just(listMessage)
        val messagesChat = listOf(MessageChat(ID_MESSAGE, ID_AUTHOR, name, TEXT, TIME, null))
        //Act
        viewModel.getMessages(ID_EVENT)

        //Assert

        verifyAll { resultObserver.onChanged(messagesChat) }
    }

}