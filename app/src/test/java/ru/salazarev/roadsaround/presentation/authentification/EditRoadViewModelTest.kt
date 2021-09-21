package ru.salazarev.roadsaround.presentation.authentification


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.google.android.gms.maps.GoogleMap
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import ru.salazarev.roadsaround.RxSchedulerRule
import ru.salazarev.roadsaround.presentation.editevent.EditEventViewModel
import ru.salazarev.roadsaround.presentation.editroad.EditRoadViewModel

class EditRoadViewModelTest {


    @Rule
    @JvmField
    val rxSchedulerRule = RxSchedulerRule()

    @Rule
    @JvmField
    val liveDataTestRule = InstantTaskExecutorRule()

    private val viewModel: EditRoadViewModel = EditRoadViewModel()

    private val exception = Exception()

    @Test
    fun `configure map`() {
        //Arrange
        //val listener
        val resultObserver: Observer<Boolean> = mockk(relaxed = true)
        viewModel.resultCreateRoute.observeForever(resultObserver)

        val map: GoogleMap = mockk()
        val key = "key"
        val typeWork: ru.salazarev.roadsaround.network.map.GoogleMap.Companion.TypeWork =
            ru.salazarev.roadsaround.network.map.GoogleMap.Companion.TypeWork.EDIT
        val listener = object : ru.salazarev.roadsaround.network.map.GoogleMap.CompleteCallback {
            override fun onComplete(status: Boolean) {
                viewModel.resultCreateRoute.value = status
            }
        }
        //Act
        //viewModel.configureMap(map, key, typeWork)

        //Assert

    }
}