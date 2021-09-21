package ru.salazarev.roadsaround.presentation.authentification

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyAll
import io.reactivex.rxjava3.core.Single
import org.junit.Rule
import org.junit.Test
import ru.salazarev.roadsaround.RxSchedulerRule
import ru.salazarev.roadsaround.domain.user.UserInteractor
import ru.salazarev.roadsaround.models.domain.User
import ru.salazarev.roadsaround.models.presentation.UserPresentation
import ru.salazarev.roadsaround.presentation.profile.ProfileViewModel
import ru.salazarev.roadsaround.util.ImageConverter
import ru.salazarev.roadsaround.valuebucket.FIRST_NAME
import ru.salazarev.roadsaround.valuebucket.FULL_NAME
import ru.salazarev.roadsaround.valuebucket.ID_USER
import ru.salazarev.roadsaround.valuebucket.LAST_NAME

class ProfileViewModelTest {
    @Rule
    @JvmField
    val rxSchedulerRule = RxSchedulerRule()

    @Rule
    @JvmField
    val liveDataTestRule = InstantTaskExecutorRule()

    private val interactor: UserInteractor = mockk(relaxed = true)

    private val imageChatInteractor: ImageConverter = mockk(relaxed = true)

    private val viewModel = ProfileViewModel(interactor, imageChatInteractor)

//    @Test
//    fun `load user data`() {
//        //Arrange
//        val observer: Observer<UserPresentation?> = mockk(relaxed = true)
//        viewModel.user.observeForever(observer)
//
//        val user = User(ID_USER, FIRST_NAME, LAST_NAME, null)
//        val userPresentation = UserPresentation(ID_USER, FULL_NAME, null)
//        every { interactor.getUserData() } returns  Single.just(user)
//        //Act
//        viewModel.loadUserData()
//        //Assert
//        verifyAll { observer.onChanged(userPresentation) }
//    }
}