package ru.salazarev.roadsaround.presentation.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.salazarev.roadsaround.data.user.UserRepositoryImpl
import ru.salazarev.roadsaround.domain.user.UserInteractor
import ru.salazarev.roadsaround.models.presentation.User
import javax.inject.Inject

class ProfileViewModel @Inject constructor(interactor: UserInteractor) : ViewModel() {
    val userLiveData: LiveData<User> = interactor.getUserData()
    val workStatus: LiveData<UserRepositoryImpl.WorkStatus> = interactor.getMessageWorkStatus()
}