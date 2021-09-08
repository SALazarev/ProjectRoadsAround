package ru.salazarev.roadsaround.domain.user

import ru.salazarev.roadsaround.models.domain.User

interface UserRepository {
    fun getUserData(): User?
    fun setUserData(user: User)
}