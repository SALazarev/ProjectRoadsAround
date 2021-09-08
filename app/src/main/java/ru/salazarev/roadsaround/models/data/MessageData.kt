package ru.salazarev.roadsaround.models.data

import com.google.firebase.firestore.PropertyName
import ru.salazarev.roadsaround.data.chat.MessagesCollectionModel

class MessageData(
    @get:PropertyName(MessagesCollectionModel.getMessage().getColumns().id)
    @set:PropertyName(MessagesCollectionModel.getMessage().getColumns().id)
    var id: String = "",
    @get:PropertyName(MessagesCollectionModel.getMessage().getColumns().authorId)
    @set:PropertyName(MessagesCollectionModel.getMessage().getColumns().authorId)
    var authorId: String = "",
    @get:PropertyName(MessagesCollectionModel.getMessage().getColumns().time)
    @set:PropertyName(MessagesCollectionModel.getMessage().getColumns().time)
    var time: String = "",
    @get:PropertyName(MessagesCollectionModel.getMessage().getColumns().text)
    @set:PropertyName(MessagesCollectionModel.getMessage().getColumns().text)
    var text: String = ""
)