package ru.salazarev.roadsaround.network

interface Authentication {
    fun registration(email: String, password: String): String

    fun authentication(email: String, password: String): String

    fun resetPassword(email: String): Boolean

    fun getUserId(): String
}