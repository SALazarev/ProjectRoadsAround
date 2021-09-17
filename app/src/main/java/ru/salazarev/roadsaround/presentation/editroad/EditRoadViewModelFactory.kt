package ru.salazarev.roadsaround.presentation.editroad

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject

/** Фабрика ViewModel для фрагмента [EditRoadViewModel]. */
class EditRoadViewModelFactory @Inject constructor(
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(aClass: Class<T>): T {
        return EditRoadViewModel() as T
    }
}