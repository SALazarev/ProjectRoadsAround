package ru.salazarev.roadsaround.data.user

object ImageStorageHelper {
    const val folder = "UsersAvatar"
    fun getFileName(id: String): String = "avatar_$id"
    const val jpegFileFormat = "jpg"
    const val imageBuffer: Long = 250 * 250
}