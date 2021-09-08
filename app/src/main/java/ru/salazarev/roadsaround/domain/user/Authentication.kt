package ru.salazarev.roadsaround.domain.user

interface Authentication {
    fun registration(email: String, password: String): String

    fun authentication(email: String, password: String): String

    fun resetPassword(email: String): Boolean
}