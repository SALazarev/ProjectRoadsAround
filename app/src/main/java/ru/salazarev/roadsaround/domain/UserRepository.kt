package ru.salazarev.roadsaround.domain

import androidx.lifecycle.LiveData
import ru.salazarev.roadsaround.data.UserRepositoryImpl
import ru.salazarev.roadsaround.models.presentation.User

interface UserRepository {
    fun getUserLiveData(): LiveData<User>
    fun setUserData(user: User)
    fun getWorkStatus(): LiveData<UserRepositoryImpl.WorkStatus>
}