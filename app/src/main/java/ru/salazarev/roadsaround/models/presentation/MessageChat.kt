package ru.salazarev.roadsaround.models.presentation

import android.graphics.drawable.Drawable

/**
 * Модель сообщения уровня бизнес-логики.
 * @param id - идентификатор сообщения.
 * @param idAuthor - идентификатор автора сообщения.
 * @param name - имя автора сообщения.
 * @param text - текст сообщения.
 * @param time - время отправки сообщения.
 * @param image - изображение автора сообщения.
 */
data class MessageChat(
    val id: String,
    val idAuthor: String,
    val name: String,
    val text: String,
    val time: String,
    val photo: Drawable?
)