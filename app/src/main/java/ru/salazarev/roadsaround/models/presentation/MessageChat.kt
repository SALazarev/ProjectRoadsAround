package ru.salazarev.roadsaround.models.presentation

import android.graphics.drawable.Drawable

data class MessageChat(
    val id: String,
    val idAuthor: String,
    val name: String,
    val message: String,
    val time: String,
    val photo: Drawable
)