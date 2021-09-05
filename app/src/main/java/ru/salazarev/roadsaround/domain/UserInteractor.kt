package ru.salazarev.roadsaround.domain

import ru.salazarev.roadsaround.models.presentation.User
import javax.inject.Inject

class UserInteractor @Inject constructor(private val repository: UserRepository) {

    fun setUserData(firstName: String, lastName: String, image: ByteArray) {
        val user = User(firstName, lastName, image)
        repository.setUserData(user)
    }

    fun getUserData() = repository.getUserLiveData()

    fun getMessageWorkStatus() = repository.getMessageWorkStatus()
}