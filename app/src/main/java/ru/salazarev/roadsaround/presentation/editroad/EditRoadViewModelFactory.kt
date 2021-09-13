package ru.salazarev.roadsaround.presentation.editroad

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.salazarev.roadsaround.network.GoogleMap
import javax.inject.Inject

class EditRoadViewModelFactory @Inject constructor(private val mapApi: GoogleMap
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(aClass: Class<T>): T {
        return EditRoadViewModel(mapApi) as T
    }
}