package ru.salazarev.roadsaround.models.domain

data class Event(
    var id: String,
    var authorId: String,
    var name: String,
    var note: String,
    var motionType: String,
    var time: String,
    var route: String,
    var members: List<User>
)