package ru.salazarev.roadsaround.data.event

/** Объект, предлставляющий информацию об структуре модели событий Firebase. */
object EventsCollectionModel {

    /** Возвращает модель события. */
    fun getEvent() = Event
}

/** Объект, предлставляющий информацию об структуре коллекции событий. */
object Event {

    /** Название коллекции событий.. */
    const val collectionName = "event"

    /** Возвращает модель таблицы сообщений. */
    fun getColumns() = Columns
}

/** Объект, предлставляющий информацию об структуре таблицы события. */
object Columns {

    /** Идентификатор события. */
    const val id = "id"

    /** Идентификатор организатора события. */
    const val authorId = "author_id"

    /** Название события. */
    const val name = "name"

    /** Описание события. */
    const val note = "note"

    /** Тип перемещения по маршруту. */
    const val motionType = "motion_type"

    /** Назначенное время события. */
    const val time = "time"

    /** Маршрут события. */
    const val route = "route"

    /** Участники события. */
    const val members = "members"
}
