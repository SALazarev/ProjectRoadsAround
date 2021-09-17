package ru.salazarev.roadsaround.models.domain

/**
 * Модель сообщения уровня бизнес-логики.
 * @param id - идентификатор сообщения.
 * @param idAuthor - идентификатор автора сообщения.
 * @param nameAuthor - имя автора сообщения.
 * @param time - время отправки сообщения.
 * @param text - текст сообщения.
 * @param image - изображение автора сообщения.
 */
data class Message(
    val id: String,
    val idAuthor: String,
    val nameAuthor: String,
    val text: String,
    val time: String,
    val image: ByteArray?
)