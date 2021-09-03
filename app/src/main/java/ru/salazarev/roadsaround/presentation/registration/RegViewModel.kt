package ru.salazarev.roadsaround.presentation.registration

import android.graphics.drawable.Drawable
import androidx.lifecycle.ViewModel
import ru.salazarev.roadsaround.domain.UserInteractor
import javax.inject.Inject

class RegViewModel @Inject constructor(private val interactor: UserInteractor): ViewModel() {

fun saveData(email: String, firstName: String, lastName: String, image: Drawable?){
    interactor.setUser(email,firstName,lastName,"image")
}
}