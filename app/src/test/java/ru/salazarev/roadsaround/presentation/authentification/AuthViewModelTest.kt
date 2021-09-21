package ru.salazarev.roadsaround.presentation.authentification

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifyAll
import io.reactivex.rxjava3.core.Completable
import org.junit.Rule
import org.junit.Test
import ru.salazarev.roadsaround.RxSchedulerRule
import ru.salazarev.roadsaround.domain.user.UserInteractor


class AuthViewModelTest {

    private companion object {
        private const val EMAIL = "email"
        private const val PASSWORD = "password"
    }

    @Rule
    @JvmField
    val rxSchedulerRule = RxSchedulerRule()

    @Rule
    @JvmField
    val liveDataTestRule = InstantTaskExecutorRule()

    private val interactor: UserInteractor = mockk()

    private val viewModel: AuthViewModel = AuthViewModel(interactor)

    private val exception = Exception()

    @Test
    fun `authenticationUser progress`() {
        //Arrange
        every { interactor.userAuthentication(EMAIL, PASSWORD) } returns Completable.complete()
        val observer: Observer<Boolean?> = mockk(relaxed = true)
        viewModel.progress.observeForever(observer)

        //Act
        viewModel.authenticationUser(EMAIL, PASSWORD)

        //Assert
        verifyAll {
            observer.onChanged(true)
            observer.onChanged(false)
        }
    }

    @Test
    fun `authenticationUser complete`() {
        //Arrange
        every { interactor.userAuthentication(EMAIL, PASSWORD) } returns Completable.complete()
        val authStatusObserver: Observer<Boolean?> = mockk(relaxed = true)
        viewModel.authStatus.observeForever(authStatusObserver)

        //Act
        viewModel.authenticationUser(EMAIL, PASSWORD)

        //Assert
        verifyAll {
            authStatusObserver.onChanged(true)
        }
    }

    @Test
    fun `authenticationUser error`() {
        //Arrange
        every {
            interactor.userAuthentication(
                EMAIL,
                PASSWORD
            )
        } returns Completable.error(exception)
        val authStatusObserver: Observer<Boolean?> = mockk(relaxed = true)
        viewModel.authStatus.observeForever(authStatusObserver)

        //Act
        viewModel.authenticationUser(EMAIL, PASSWORD)

        //Assert
        verifyAll {
            authStatusObserver.onChanged(false)
        }
    }

    @Test
    fun `reset password onComplete`() {
        //Arrange
        every { interactor.resetUserPassword(EMAIL) } returns Completable.complete()
        val resetPasswordObserver: Observer<Boolean> = mockk(relaxed = true)
        viewModel.resetPassStatus.observeForever(resetPasswordObserver)

        //Act
        viewModel.resetPassword(EMAIL)

        //Assert
        verifyAll { resetPasswordObserver.onChanged(true) }
    }

    @Test
    fun `reset password Progress`() {
        //Arrange
        every { interactor.resetUserPassword(EMAIL) } returns Completable.complete()
        val progressObserver: Observer<Boolean?> = mockk(relaxed = true)
        viewModel.progress.observeForever(progressObserver)

        //Act
        viewModel.resetPassword(EMAIL)

        //Assert
        verifyAll {
            progressObserver.onChanged(true)
            progressObserver.onChanged(false)
        }
    }

    @Test
    fun `reset password onError`() {
        //Arrange
        every { interactor.resetUserPassword(EMAIL) } returns Completable.error(exception)
        val resetPasswordObserver: Observer<Boolean> = mockk(relaxed = true)
        viewModel.resetPassStatus.observeForever(resetPasswordObserver)

        //Act
        viewModel.resetPassword(EMAIL)

        //Assert
        verifyAll { resetPasswordObserver.onChanged(false) }
    }

}