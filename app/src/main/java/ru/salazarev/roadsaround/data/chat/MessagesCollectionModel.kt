package ru.salazarev.roadsaround.data.chat

object MessagesCollectionModel {
    fun getMessage() = Message
}

object Message {
    const val collectionName = "messages"
    fun getColumns() = Columns
}

object Columns {
    const val id = "id"
    const val authorId = "author_id"
    const val text = "text"
    const val time = "time"
}
