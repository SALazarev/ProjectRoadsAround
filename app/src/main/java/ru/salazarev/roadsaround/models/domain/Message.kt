package ru.salazarev.roadsaround.models.domain

data class Message(
    val id: String,
    val idAuthor: String,
    val name: String,
    val message: String,
    val time: String,
    val image: ByteArray?
)