package ru.salazarev.roadsaround.util

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import ru.salazarev.roadsaround.App
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class ImageConverter @Inject constructor(private val resources: Resources) {

    fun convert(byteArray: ByteArray): Drawable = BitmapDrawable(
        resources,
        BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
    )

    fun convert(image: Bitmap): ByteArray{
        val stream = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.PNG, 100, stream)
        val byteArray = stream.toByteArray()
        image.recycle()
        return byteArray
    }
}