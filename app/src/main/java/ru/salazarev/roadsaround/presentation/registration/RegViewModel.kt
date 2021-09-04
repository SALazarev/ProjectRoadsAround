package ru.salazarev.roadsaround.presentation.registration

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import ru.salazarev.roadsaround.domain.UserInteractor
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class RegViewModel @Inject constructor(private val interactor: UserInteractor): ViewModel() {

    lateinit var image: Bitmap

fun saveData(email: String, firstName: String, lastName: String){
    val stream = ByteArrayOutputStream()
    image.compress(Bitmap.CompressFormat.PNG, 100, stream)
    val byteArray: ByteArray = stream.toByteArray()
    image.recycle()
    interactor.setUser(email, firstName, lastName,byteArray)
}
}