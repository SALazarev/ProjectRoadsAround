package ru.salazarev.roadsaround.domain.user

import ru.salazarev.roadsaround.models.domain.User
import javax.inject.Inject

class UserInteractor @Inject constructor(private val repository: UserRepository) {

    fun setUserData(firstName: String, lastName: String, image: ByteArray) {
        val user = User(firstName, lastName, image)
        repository.setUserData(user)
    }

    fun getUserData() = repository.getUserData()

    fun getMessageWorkStatus() = repository.getWorkStatusData()
}