package ru.salazarev.roadsaround.domain

import io.mockk.every
import io.mockk.mockk
import io.mockk.verifySequence
import org.junit.Rule
import org.junit.Test
import ru.salazarev.roadsaround.RxSchedulerRule
import ru.salazarev.roadsaround.domain.event.EventInteractor
import ru.salazarev.roadsaround.domain.event.EventRepository
import ru.salazarev.roadsaround.domain.user.UserRepository
import ru.salazarev.roadsaround.models.data.EventData
import ru.salazarev.roadsaround.models.domain.Event
import ru.salazarev.roadsaround.models.domain.User
import ru.salazarev.roadsaround.models.presentation.EventPreview

class EventInteractorTest {

    companion object {
        const val ID_EVENT = "idEvent"
        const val ID_USER = "idUser"
        const val ID_OTHER_USER = "idOtherUser"
        const val NAME_EVENT = "nameEvent"
        const val NOTE_EVENT = "noteEvent"
        const val MOTION_TYPE = "motionType"
        const val TIME = 0L
        const val ROUTE = "route"
        const val FIRST_NAME = "FIRST_NAME"
        const val LAST_NAME = "LAST_NAME"
        val MEMBERS = listOf("memberId")
        val MEMBER_ONLY_USER = listOf<String>("idUser")
    }

    @Rule
    @JvmField
    val rxSchedulerRule = RxSchedulerRule()

    private val eventRepository: EventRepository = mockk(relaxed = true)
    private val userRepository: UserRepository = mockk(relaxed = true)
    private val authentication: ru.salazarev.roadsaround.network.Authentication =
        mockk(relaxed = true)
    private val interactor = EventInteractor(eventRepository, userRepository, authentication)
    private val exception = Exception()

    @Test
    fun `create event complete`() {
        //Arrange
        every { authentication.getUserId() } returns ID_USER
        //Act
        val completable = interactor.createEvent(
            ID_EVENT, NAME_EVENT, NOTE_EVENT, MOTION_TYPE, TIME,
            ROUTE, MEMBERS
        )
        //Assert
        completable.test().assertComplete()
        verifySequence {
            authentication.getUserId()
            eventRepository.sendEvent(
                ID_EVENT, ID_USER, NAME_EVENT, NOTE_EVENT, MOTION_TYPE, TIME,
                ROUTE, MEMBERS
            )
        }
    }

    @Test
    fun `create event error`() {
        //Arrange
        every { authentication.getUserId() } returns ID_USER
        every {
            eventRepository.sendEvent(
                ID_EVENT, ID_USER, NAME_EVENT, NOTE_EVENT, MOTION_TYPE,
                TIME, ROUTE, MEMBERS
            )
        } throws exception
        //Act
        val completable = interactor.createEvent(
            ID_EVENT, NAME_EVENT, NOTE_EVENT, MOTION_TYPE, TIME,
            ROUTE, MEMBERS
        )
        //Assert
        completable.test().assertError(exception)
        verifySequence {
            authentication.getUserId()
        }
    }

//
//    @Test
//    fun `get user event previews onNext`() {
//        //Arrange
//        every { authentication.getUserId() } returns ID_USER
//
//        val listEventData = listOf(EventData(ID_EVENT, ID_USER, NAME_EVENT, NOTE_EVENT,
//            MOTION_TYPE, TIME, ROUTE, MEMBERS))
//        every {eventRepository.getUserEvents(ID_USER)} returns listEventData
//
//        val user = User(ID_USER, FIRST_NAME, LAST_NAME,null)
//        every {userRepository.getUserData(ID_USER)} returns user
//
//
//        val compareList =
//            listOf(EventPreview(
//                ID_EVENT,
//                "$FIRST_NAME $LAST_NAME",
//                MOTION_TYPE,
//                NAME_EVENT,
//                "03:00 01/01/1970",
//                EventInteractor.Companion.TypeWorkWithEvent.AUTHOR))
//
//        //Act
//        val single = interactor.getUserEventPreviews()
//
//        //Assert
//        single.test().assertResult(compareList)
//    }
//
//    @Test
//    fun `get users event previews onNext`() {
//        //Arrange
//        every { authentication.getUserId() } returns ID_USER
//
//        val listEventData = listOf(EventData(ID_EVENT, ID_OTHER_USER, NAME_EVENT, NOTE_EVENT,
//            MOTION_TYPE, TIME, ROUTE, MEMBERS))
//        every { eventRepository.getAllEvents() } returns listEventData
//
//        val listUsers = listOf(User(ID_OTHER_USER, FIRST_NAME, LAST_NAME, null))
//        every { userRepository.getUsersData(listEventData.map { it.authorId }) } returns listUsers
//
//        val compareList = listOf(
//            EventPreview(
//                ID_EVENT, "$FIRST_NAME $LAST_NAME", MOTION_TYPE, NAME_EVENT,
//                "03:00 01/01/1970", EventInteractor.Companion.TypeWorkWithEvent.AUTHOR
//            )
//        )
//
//        //Act
//        val single = interactor.getUsersEventPreviews()
//        //Assert
//        single.test().assertValueAt(0,compareList)
//    }

    @Test
    fun `get members event onNext`() {
        //Arrange
        val eventData = EventData(
            ID_EVENT, ID_USER, NAME_EVENT, NOTE_EVENT,
            MOTION_TYPE, TIME, ROUTE, MEMBERS
        )
        every { eventRepository.getEvent(ID_EVENT) } returns eventData

        val listMembers: List<User> = listOf(User(ID_USER, FIRST_NAME, LAST_NAME, null))
        every { userRepository.getUsersData(eventData.members) } returns listMembers

        //Act
        val single = interactor.getMembersEvent(ID_EVENT)
        //Assert
        single.test().assertResult(listMembers)
    }

    @Test
    fun `get event onNext`() {
        //Arrange
        val eventData = EventData(
            ID_EVENT, ID_USER, NAME_EVENT, NOTE_EVENT,
            MOTION_TYPE, TIME, ROUTE, MEMBERS
        )
        every { eventRepository.getEvent(ID_EVENT) } returns eventData
        val users = listOf(User(ID_USER, FIRST_NAME, LAST_NAME, null))
        every { userRepository.getUsersData(eventData.members) } returns users

        val event = Event(
            eventData.id,
            eventData.authorId,
            eventData.name,
            eventData.note,
            eventData.motionType,
            eventData.time,
            eventData.route,
            users
        )
        //Act
        val single = interactor.getEvent(ID_EVENT)
        //Assert
        single.test().assertResult(event)
    }

    @Test
    fun `add user in event complete`() {
        //Arrange
        every { authentication.getUserId() } returns ID_USER
        //Act
        val completable = interactor.addUserInEvent(ID_EVENT)
        //Assert
        completable.test().assertComplete()
        verifySequence {
            authentication.getUserId()
            eventRepository.addUserInEvent(ID_USER, ID_EVENT)
        }
    }

    @Test
    fun `add user in event error`() {
        //Arrange
        every { authentication.getUserId() } returns ID_USER
        every { eventRepository.addUserInEvent(ID_USER, ID_EVENT) } throws exception
        //Act
        val completable = interactor.addUserInEvent(ID_EVENT)
        //Assert
        completable.test().assertError(exception)
        verifySequence {
            authentication.getUserId()
        }
    }

    @Test
    fun `leave user from event complete`() {
        //Arrange
        every { authentication.getUserId() } returns ID_USER
        //Act
        val completable = interactor.leaveUserFromEvent(ID_EVENT)
        //Assert
        completable.test().assertComplete()
        verifySequence {
            authentication.getUserId()
            eventRepository.leaveUserFromEvent(ID_USER, ID_EVENT)
        }
    }

    @Test
    fun `leave user from event error`() {
        //Arrange
        every { authentication.getUserId() } returns ID_USER
        every { eventRepository.leaveUserFromEvent(ID_USER, ID_EVENT) } throws exception
        //Act
        val completable = interactor.leaveUserFromEvent(ID_EVENT)
        //Assert
        completable.test().assertError(exception)
        verifySequence {
            authentication.getUserId()
        }
    }
}