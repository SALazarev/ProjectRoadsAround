package ru.salazarev.roadsaround.util

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import java.io.ByteArrayOutputStream
import javax.inject.Inject

/** Класс конвертации изображений.
 * @param resources - объект работы с сресурсами приложения.
 * */
class ImageConverter @Inject constructor(private val resources: Resources) {

    /** Преобразование изображения типа [ByteArray] в [Drawable]. */
    fun convert(byteArray: ByteArray): Drawable = BitmapDrawable(
        resources,
        BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    )

    /** Преобразование изображения типа [Bitmap] в [ByteArray]. */
    fun convert(image: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray = stream.toByteArray()
        image.recycle()
        return byteArray
    }
}