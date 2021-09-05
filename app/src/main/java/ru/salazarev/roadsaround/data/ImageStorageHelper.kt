package ru.salazarev.roadsaround.data

object ImageStorageHelper {
    const val folder = "UsersAvatar"
    fun getFileName(id: String): String = "avatar_$id"
    val jpegFileFormat = "jpg"
    val imageBuffer: Long = 250 * 250
}