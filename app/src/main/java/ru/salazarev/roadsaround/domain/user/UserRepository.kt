package ru.salazarev.roadsaround.domain.user

import androidx.lifecycle.LiveData
import ru.salazarev.roadsaround.data.user.UserRepositoryImpl
import ru.salazarev.roadsaround.models.domain.User

interface UserRepository {
    fun getUserData(): LiveData<User>
    fun setUserData(user: User)
    fun getWorkStatusData(): LiveData<UserRepositoryImpl.WorkStatus>
}