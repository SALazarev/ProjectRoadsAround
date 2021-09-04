package ru.salazarev.roadsaround.domain

import ru.salazarev.roadsaround.models.presentation.User

interface UserRepository {
    fun getUserData(): User
    fun setUserData(user: User)
}