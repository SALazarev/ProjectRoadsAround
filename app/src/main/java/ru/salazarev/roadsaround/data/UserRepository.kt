package ru.salazarev.roadsaround.data

import com.google.firebase.database.*
import ru.salazarev.roadsaround.domain.UserRepository
import ru.salazarev.roadsaround.models.User
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(private val database: DatabaseReference): UserRepository {
    override fun getUserData(): User {
        TODO()
    }

    override fun setUserData(user: User) {
       database.push().setValue(user)
    }

}