package ru.salazarev.roadsaround.domain.user

import ru.salazarev.roadsaround.models.domain.User
import javax.inject.Inject

class UserInteractor @Inject constructor(private val repository: UserRepository) {

    fun registrationUser(email: String, password: String, firstName: String, lastName: String, image: ByteArray?) {
        val id = repository.registration(email,password)
        val user = User(id,firstName, lastName, image)
        repository.setUserData(user)
    }

    fun getUserData() = repository.getUserData()
}