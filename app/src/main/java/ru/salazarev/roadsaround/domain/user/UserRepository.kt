package ru.salazarev.roadsaround.domain.user

import androidx.lifecycle.LiveData
import ru.salazarev.roadsaround.data.user.UserRepositoryImpl
import ru.salazarev.roadsaround.models.domain.User

interface UserRepository {
    fun getUserData(): User?
    fun setUserData(user: User): Boolean
}