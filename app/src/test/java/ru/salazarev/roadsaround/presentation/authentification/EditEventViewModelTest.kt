package ru.salazarev.roadsaround.presentation.authentification

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyAll
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import org.junit.Rule
import org.junit.Test
import ru.salazarev.roadsaround.RxSchedulerRule
import ru.salazarev.roadsaround.domain.event.EventInteractor
import ru.salazarev.roadsaround.models.domain.Event
import ru.salazarev.roadsaround.presentation.editevent.EditEventViewModel

class EditEventViewModelTest {
    private companion object {
        private const val EMAIL = "email"
        private const val PASSWORD = "password"
        private const val ID_EVENT = "ID_EVENT"
        private const val TEXT = "TEXT"
        private const val ID_AUTHOR = "idAuthor"
        private const val TEXT_MESSAGE = "text"
        private const val FIRST_NAME = "FIRST_NAME"
        private const val LAST_NAME = "LAST_NAME"
        private const val ID_MESSAGE = "ID_MESSAGE"
        const val ID_USER = "idUser"
        const val NOTE_EVENT = "noteEvent"
        private const val TIME = 0L
        private const val NAME_EVENT = "NAME_EVENT"
        private const val NOTE = "NOTE"
        private const val MOTION_TYPE = "MOTION_TYPE"
        private const val ROUTE = "ROUTE"
        val MEMBERS = listOf("memberId")
    }

    @Rule
    @JvmField
    val rxSchedulerRule = RxSchedulerRule()

    @Rule
    @JvmField
    val liveDataTestRule = InstantTaskExecutorRule()

    private val interactor: EventInteractor = mockk()

    private val savedStateHandle: SavedStateHandle = mockk(relaxed = true)

    private val viewModel: EditEventViewModel = EditEventViewModel(interactor, savedStateHandle)

    private val exception = Exception()

    @Test
    fun `create event complete`() {
        //Arrange
        every {
            interactor.createEvent(
                ID_EVENT, NAME_EVENT, NOTE, MOTION_TYPE, TIME, ROUTE, MEMBERS
            )
        } returns Completable.complete()

        every { viewModel.getMembers() } returns MEMBERS
        every { viewModel.getIdEvent() } returns ID_EVENT

        val resultObserver: Observer<Boolean> = mockk(relaxed = true)
        viewModel.result.observeForever(resultObserver)
        //Act
        viewModel.createEvent(NAME_EVENT, NOTE, MOTION_TYPE, TIME, ROUTE)
        //Assert
        verifyAll { resultObserver.onChanged(true) }
    }

    @Test
    fun `create event error`() {
        //Arrange
        every {
            interactor.createEvent(
                ID_EVENT, NAME_EVENT, NOTE, MOTION_TYPE, TIME, ROUTE, MEMBERS
            )
        } returns Completable.error(exception)

        every { viewModel.getMembers() } returns MEMBERS
        every { viewModel.getIdEvent() } returns ID_EVENT

        val resultObserver: Observer<Boolean> = mockk(relaxed = true)
        viewModel.result.observeForever(resultObserver)
        //Act
        viewModel.createEvent(NAME_EVENT, NOTE, MOTION_TYPE, TIME, ROUTE)
        //Assert
        verifyAll { resultObserver.onChanged(false) }
    }

    @Test
    fun `get event data yes data`() {
        //Arrange
        val event: Event = mockk()
        val resultObserver: Observer<Event> = mockk(relaxed = true)
        viewModel.data.observeForever(resultObserver)

        every {  interactor.getEvent(ID_EVENT) } returns Single.just(event)
        //Act

        viewModel.getEventData(ID_EVENT)
        //Assert
        verifyAll { resultObserver.onChanged(event) }
    }

    @Test
    fun `get event data null`() {
        //Arrange
        val event: Event? = null
        val resultObserver: Observer<Event> = mockk(relaxed = true)
        viewModel.data.observeForever(resultObserver)

        every {  interactor.getEvent(ID_EVENT) } returns Single.error(exception)
        //Act

        viewModel.getEventData(ID_EVENT)
        //Assert
        verifyAll { resultObserver.onChanged(event) }
    }
}