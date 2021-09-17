package ru.salazarev.roadsaround.data.user

/**
 * Объект, предоставляющий информацию о конфигурации хранилища изображений
 * Firebase Storage.
 */
object ImageStorageHelper {
    /** Название директории хранения пользовательских аватаров. */
    const val folder = "UsersAvatar"

    /** Возвращает шаблон названия файла изображения. */
    fun getFileName(id: String): String = "avatar_$id"

    /** Возвращает строковвое название формата изображения jpg. */
    const val jpegFileFormat = "jpg"

    /** Возвращает размер буфера для изображений в хранилище. */
    const val imageBuffer: Long = 15 * 1024 * 1024
}