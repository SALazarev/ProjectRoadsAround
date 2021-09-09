package ru.salazarev.roadsaround.domain.user

import ru.salazarev.roadsaround.models.domain.User
import javax.inject.Inject

class UserInteractor @Inject constructor(
    private val repository: UserRepository,
    private val auth: Authentication
) {

    fun registrationUser(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        image: ByteArray?
    ) {
        val id = auth.registration(email, password)
        val user = User(id, firstName, lastName, image)
        repository.setUserData(user)
    }

    fun getUserData(id: String = auth.getUserId()) = repository.getUserData(id)

    fun userAuthentication(email: String, password: String) {
        auth.authentication(email, password)
    }

    fun resetUserPassword(email: String): Boolean = auth.resetPassword(email)
}