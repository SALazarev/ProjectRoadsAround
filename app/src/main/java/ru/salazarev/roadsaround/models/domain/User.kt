package ru.salazarev.roadsaround.models.domain

/**
 * Модель пользователя уровня бизнес-логики.
 * @param id - идентификатор пользователя.
 * @param firstName - имя пользователя.
 * @param lastName - фамилия пользователя.
 * @param image - изображение пользователя.
 */
data class User(val id: String, val firstName: String, val lastName: String, val image: ByteArray?)