package ru.salazarev.roadsaround.data.user

object UsersCollectionModel {
    fun getUsers() = Users
}

object Users {
    const val collectionName = "users"
    fun getColumns() = Columns
}

object Columns {
    const val id = "id"
    const val firstName = "first_name"
    const val lastName = "last_name"
    const val image = "image"
}
