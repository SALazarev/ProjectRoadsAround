package ru.salazarev.roadsaround.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.salazarev.roadsaround.domain.user.UserInteractor
import ru.salazarev.roadsaround.util.ImageConverter
import javax.inject.Inject

class ProfileViewModelFactory @Inject constructor(
    private val interactor: UserInteractor,
    private val imageConverter: ImageConverter
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(aClass: Class<T>): T {
        return ProfileViewModel(interactor, imageConverter) as T
    }
}