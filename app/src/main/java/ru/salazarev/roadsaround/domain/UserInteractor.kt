package ru.salazarev.roadsaround.domain

import android.graphics.drawable.Drawable
import com.google.firebase.auth.FirebaseAuth
import ru.salazarev.roadsaround.models.User
import javax.inject.Inject

class UserInteractor @Inject constructor (private val repository: UserRepository, private val firebaseAuth: FirebaseAuth) {

    fun setUser(email: String, firstName: String, lastName: String, image: String){
        val user = User("1",email, firstName, lastName, image)
        repository.setUserData(user)
    }
}