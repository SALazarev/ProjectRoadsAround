package ru.salazarev.roadsaround.domain.user

import ru.salazarev.roadsaround.models.domain.User

interface UserRepository {
    fun getUserData(id: String): User
    fun setUserData(user: User)
    fun getUsersData(idList: List<String>): List<User>
}