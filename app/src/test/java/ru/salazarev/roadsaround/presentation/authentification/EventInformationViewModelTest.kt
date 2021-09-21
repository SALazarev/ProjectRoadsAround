package ru.salazarev.roadsaround.presentation.authentification

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
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
import ru.salazarev.roadsaround.models.domain.User
import ru.salazarev.roadsaround.models.presentation.EventPresentation
import ru.salazarev.roadsaround.models.presentation.UserPresentation
import ru.salazarev.roadsaround.presentation.eventinformation.EventInformationViewModel
import ru.salazarev.roadsaround.util.ImageConverter
import ru.salazarev.roadsaround.valuebucket.*

class EventInformationViewModelTest {

    @Rule
    @JvmField
    val rxSchedulerRule = RxSchedulerRule()

    @Rule
    @JvmField
    val liveDataTestRule = InstantTaskExecutorRule()

    private val imageConverter: ImageConverter = mockk(relaxed = true)
    private val interactor: EventInteractor = mockk(relaxed = true)

    private val viewModel = EventInformationViewModel(interactor, imageConverter)

    private val exception = Exception()

    @Test
    fun `get event data`() {
        //Arrange
        val observer: Observer<EventPresentation?> = mockk(relaxed = true)
        viewModel.data.observeForever(observer)

        val user = User(ID_USER, FIRST_NAME, LAST_NAME, null)
        val event = Event(
            EVENT_ID, USER_ID, EVENT_NAME, EVENT_NOTE, MOTION_TYPE,
            TIME, ROUTE, listOf(user)
        )
        val name = "${FIRST_NAME} ${LAST_NAME}"
        val compareValue = EventPresentation(
            EVENT_ID, USER_ID, EVENT_NAME, EVENT_NOTE,
            MOTION_TYPE, TIME_STRING_FROMAT, ROUTE, listOf(UserPresentation(ID_USER, name, null))
        )

        every { interactor.getEvent(EVENT_ID) } returns Single.just(event)
        //Act
        viewModel.getEventData(EVENT_ID)
        //Assert
        verifyAll { observer.onChanged(compareValue) }
    }

    @Test
    fun `participate from event complete`() {
        //Arrange
        every { interactor.addUserInEvent(EVENT_ID) } returns Completable.complete()
        val observer: Observer<Boolean> = mockk(relaxed = true)
        viewModel.resultParticipate.observeForever(observer)

        //Act
        viewModel.participateFromEvent(EVENT_ID)

        //Assert
        verifyAll { observer.onChanged(true) }
    }

    @Test
    fun `participate from event error`() {
        //Arrange
        every { interactor.addUserInEvent(EVENT_ID) } returns Completable.error(exception)
        val observer: Observer<Boolean> = mockk(relaxed = true)
        viewModel.resultParticipate.observeForever(observer)

        //Act
        viewModel.participateFromEvent(EVENT_ID)

        //Assert
        verifyAll { observer.onChanged(false) }
    }

    @Test
    fun `leave from event complete`() {
        //Arrange
        every { interactor.leaveUserFromEvent(EVENT_ID) } returns Completable.complete()
        val observer: Observer<Boolean> = mockk(relaxed = true)
        viewModel.resultLeave.observeForever(observer)

        //Act
        viewModel.leaveFromEvent(EVENT_ID)

        //Assert
        verifyAll { observer.onChanged(true) }
    }

    @Test
    fun `leave from event error`() {
        //Arrange
        every { interactor.leaveUserFromEvent(EVENT_ID) } returns Completable.error(exception)
        val observer: Observer<Boolean> = mockk(relaxed = true)
        viewModel.resultLeave.observeForever(observer)

        //Act
        viewModel.leaveFromEvent(EVENT_ID)

        //Assert
        verifyAll { observer.onChanged(false) }
    }
}