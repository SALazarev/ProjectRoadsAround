package ru.salazarev.roadsaround.models.domain

data class Event(
    var id: String,
    var authorId: String,
    var name: String,
    var description: String,
    var motionType: String,
    var time: Long,
    var route: String,
    var members: List<User>
)