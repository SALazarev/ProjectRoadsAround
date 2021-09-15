package ru.salazarev.roadsaround.models.presentation

class EventPresentation(
    var id: String,
    var authorId: String,
    var name: String,
    var note: String,
    var motionType: String,
    var time: String,
    var route: String,
    var members: List<UserPresentation>
)