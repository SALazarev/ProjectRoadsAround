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
import ru.salazarev.roadsaround.models.domain.User
import ru.salazarev.roadsaround.models.presentation.UserPresentation
import ru.salazarev.roadsaround.presentation.members.MembersViewModel
import ru.salazarev.roadsaround.util.ImageConverter
import ru.salazarev.roadsaround.valuebucket.*

class MembersViewModelTest {
    @Rule
    @JvmField
    val rxSchedulerRule = RxSchedulerRule()

    @Rule
    @JvmField
    val liveDataTestRule = InstantTaskExecutorRule()

    private val interactor: EventInteractor = mockk()

    private val imageChatInteractor: ImageConverter = mockk()

    private val viewModel: MembersViewModel = MembersViewModel(interactor, imageChatInteractor)


    @Test
    fun `get members progress`() {
        //Arrange
        val observer: Observer<Boolean> = mockk(relaxed = true)
        viewModel.progress.observeForever(observer)

        val listUser = listOf(User(ID_USER, FIRST_NAME, LAST_NAME,null))
        every{ interactor.getMembersEvent(EVENT_ID)} returns Single.just(listUser)
        val listUserPresentation = listOf(UserPresentation(ID_USER, FULL_NAME, null))
        //Act
        viewModel.getMembers(EVENT_ID)
        //Assert
        verifyAll {
            observer.onChanged(true)
            observer.onChanged(false)
        }
    }

    @Test
    fun `get members`() {
        //Arrange
        val observer: Observer<List<UserPresentation>> = mockk(relaxed = true)
        viewModel.members.observeForever(observer)

        val listUser = listOf(User(ID_USER, FIRST_NAME, LAST_NAME,null))
        every{ interactor.getMembersEvent(EVENT_ID)} returns Single.just(listUser)
        val listUserPresentation = listOf(UserPresentation(ID_USER, FULL_NAME, null))
        //Act
        viewModel.getMembers(EVENT_ID)
        //Assert
        verifyAll { observer.onChanged(listUserPresentation) }
    }

}