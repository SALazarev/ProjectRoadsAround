package ru.salazarev.roadsaround.domain

import io.mockk.every
import io.mockk.mockk
import io.mockk.verifySequence
import org.junit.Rule
import org.junit.Test
import ru.salazarev.roadsaround.RxSchedulerRule
import ru.salazarev.roadsaround.domain.user.UserInteractor
import ru.salazarev.roadsaround.domain.user.UserRepository
import ru.salazarev.roadsaround.models.domain.User

class UserInteractorTest {

    companion object {
        const val ID_USER = "idUser"
        const val EMAIL = "email"
        const val PASSWORD = "password"
        const val FIRST_NAME = "FIRST_NAME"
        const val LAST_NAME = "LAST_NAME"
    }

    @Rule
    @JvmField
    val rxSchedulerRule = RxSchedulerRule()

    private val userRepository: UserRepository = mockk(relaxed = true)
    private val authentication: ru.salazarev.roadsaround.network.Authentication = mockk(relaxed = true)
    private val interactor = UserInteractor(userRepository, authentication)
    private val exception = Exception()

    @Test
    fun `user authentication complete`() {
        //Arrange
        every { authentication.authentication(EMAIL, PASSWORD) } returns ID_USER

        //Act
        val completable = interactor.userAuthentication(EMAIL, PASSWORD)

        //Assert
        completable.test().assertComplete()
        verifySequence {
            authentication.authentication(EMAIL, PASSWORD)
        }
    }

    @Test
    fun `user authentication error`() {
        //Arrange
        every { authentication.authentication(EMAIL, PASSWORD) } throws (exception)

        //Act
        val completable = interactor.userAuthentication(EMAIL, PASSWORD)

        //Assert
        completable.test().assertError(exception)
        verifySequence {
            authentication.authentication(EMAIL, PASSWORD)
        }
    }

    @Test
    fun `get user data complete`() {
        //Arrange
        val user: User = mockk()
        every { userRepository.getUserData(ID_USER) } returns user

        //Act
        val single = interactor.getUserData(ID_USER)

        //Assert
        single.test().assertValue(user)
    }

    @Test
    fun `get user data error`() {
        //Arrange
        every { userRepository.getUserData(ID_USER) } throws exception

        //Act
        val single = interactor.getUserData(ID_USER)

        //Assert
        single.test().assertError(exception)
    }

    @Test
    fun `reset password complete`() {
        //Arrange

        //Act
        val completable = interactor.resetUserPassword(EMAIL)
        //Assert
        completable.test().assertComplete()
        verifySequence {
            authentication.resetPassword(EMAIL)
        }
    }

    @Test
    fun `reset password error`() {
        //Arrange
        every {authentication.resetPassword(EMAIL)} throws exception
        //Act
        val completable = interactor.resetUserPassword(EMAIL)
        //Assert
        completable.test().assertError(exception)
        verifySequence {
            authentication.resetPassword(EMAIL)
        }
    }

    @Test
    fun `user registration complete`() {
        //Arrange
        every { authentication.registration(EMAIL, PASSWORD) } returns ID_USER
        val image = ByteArray(0)
        val user = User(ID_USER, FIRST_NAME, LAST_NAME,image)
        //Act
        val completable = interactor.registrationUser(EMAIL, PASSWORD, FIRST_NAME, LAST_NAME,image)
        //Assert
        completable.test().assertComplete()
        verifySequence {
            authentication.registration(EMAIL, PASSWORD)
            userRepository.setUserData(user)
        }
    }

    @Test
    fun `user registration error`() {
        //Arrange
        every { authentication.registration(EMAIL, PASSWORD) } throws exception
        val image = ByteArray(0)
        //Act
        val completable = interactor.registrationUser(EMAIL, PASSWORD, FIRST_NAME, LAST_NAME,image)
        //Assert
        completable.test().assertError(exception)
        verifySequence {
            authentication.registration(EMAIL, PASSWORD)
        }
    }


}