package ru.salazarev.roadsaround.domain

import com.google.firebase.auth.FirebaseAuth
import ru.salazarev.roadsaround.models.presentation.User
import javax.inject.Inject

class UserInteractor @Inject constructor (private val repository: UserRepository) {

    fun setUser(firstName: String, lastName: String, image: ByteArray){
        val user = User(firstName, lastName, image)
        repository.setUserData(user)
    }
}