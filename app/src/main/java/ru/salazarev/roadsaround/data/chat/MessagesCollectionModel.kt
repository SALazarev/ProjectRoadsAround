package ru.salazarev.roadsaround.data.chat


/** Объект, предлставляющий информацию об структуре модели сообщения Firebase. */
object MessagesCollectionModel {

    /** Возвращает модель сообщения. */
    fun getMessage() = Message
}

/** Объект, предлставляющий информацию об структуре коллекции сообщений. */
object Message {

    /** Название коллекции сообщений. */
    const val collectionName = "messages"

    /** Возвращает модель таблицы сообщений. */
    fun getColumns() = Columns
}

/** Объект, предлставляющий информацию об структуре таблицы сообщения. */
object Columns {

    /** Идентификатор сообщения. */
    const val id = "id"

    /** Идентификатор автора сообщения. */
    const val authorId = "author_id"

    /** Текст сообщения. */
    const val text = "text"

    /** Время отправки сообщения. */
    const val time = "time"
}
