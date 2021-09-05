package ru.salazarev.roadsaround.domain

import androidx.lifecycle.LiveData
import ru.salazarev.roadsaround.models.presentation.User

interface UserRepository {
    fun getUserData(): LiveData<User>
    fun setUserData(user: User)
    fun getMessageWorkStatus(): LiveData<String>
}