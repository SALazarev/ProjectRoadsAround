package ru.salazarev.roadsaround

import com.google.common.truth.Truth
import io.mockk.every
import io.mockk.mockk
import io.mockk.verifySequence
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    interface GetNumber {
        fun getMyNumber(): Int
    }

    class NumberHandler(private val numberGetter: GetNumber) {
        fun numberProvider(): Int {
            return numberGetter.getMyNumber()
        }
    }

    @Test
    fun SimpleSample() {
        //Arrange
        val getMyNumber: GetNumber = mockk {
            every { getMyNumber() } returns 123
        }
        val numberHandler = NumberHandler(getMyNumber)

        //Act
        val resultValue: Int = numberHandler.numberProvider()

        //Assert
        Truth.assertThat(resultValue).isEqualTo(123)
        verifySequence {
            getMyNumber.getMyNumber()
        }
    }
}