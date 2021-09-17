package ru.salazarev.roadsaround.data.user

/** Объект, предлставляющий информацию об структуре модели пользователя Firebase. */
object UsersCollectionModel {

    /** Возвращает модель пользователя. */
    fun getUser() = Users
}

/** Объект, предлставляющий информацию об структуре коллекции пользователей. */
object Users {

    /** Название коллекции пользователей. */
    const val collectionName = "users"

    /** Возвращает модель таблицы пользователей. */
    fun getColumns() = Columns
}

/** Объект, предлставляющий информацию об структуре таблицы пользователя. */
object Columns {

    /** Идентификатор пользователя. */
    const val id = "id"

    /** Имя пользователя. */
    const val firstName = "first_name"

    /** Фамилия пользователя. */
    const val lastName = "last_name"

    /** Изображение пользователя. */
    const val image = "image"
}
