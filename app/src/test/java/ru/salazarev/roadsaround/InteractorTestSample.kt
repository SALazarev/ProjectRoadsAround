package ru.salazarev.roadsaround

import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Single
import org.junit.Rule
import org.junit.Test

class InteractorTestSample {

    @Rule
    @JvmField
    val rxSchedulerRule = RxSchedulerRule()

    interface Repository {
        fun requestNumber(): Single<Int>
    }

    class NumberInteractor(private val repository: Repository) {
        fun requestNumber(): Single<Int> {
            return repository.requestNumber().doOnSuccess {
                //do nothing
            }
        }
    }

    @Test
    fun rxTest() {
        //Arrange
        val repositoryMock: Repository = mockk {
            every { requestNumber() } returns Single.just(123)
        }
        val interactorMock: NumberInteractor = NumberInteractor(repositoryMock)

        //Act
        val single = interactorMock.requestNumber()

        //Assert
        single
            .test()
            .assertResult(123)
    }
}