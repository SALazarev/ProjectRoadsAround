package ru.salazarev.roadsaround.presentation.profile

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.salazarev.roadsaround.domain.UserInteractor
import ru.salazarev.roadsaround.models.presentation.User
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class ProfileViewModel @Inject constructor(private val interactor: UserInteractor) : ViewModel() {

    val userLiveData: LiveData<User> = interactor.getUserData()
}