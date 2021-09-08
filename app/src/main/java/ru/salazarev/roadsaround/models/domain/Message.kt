package ru.salazarev.roadsaround.models.domain

import android.graphics.drawable.Drawable

data class Message(
    val id: String,
    val name: String,
    val message: String,
    val time: String,
    val photo: Drawable
)