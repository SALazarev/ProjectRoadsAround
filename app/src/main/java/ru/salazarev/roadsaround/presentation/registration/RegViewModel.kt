package ru.salazarev.roadsaround.presentation.registration

import android.graphics.Bitmap
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.salazarev.roadsaround.domain.user.UserInteractor
import ru.salazarev.roadsaround.util.ImageConverter
import javax.inject.Inject


/** ViewModel для фрагмента [RegViewModel].
 * @param interactor - объект управления информацией о пользователях.
 * @param imageConverter - конвертер изображений.
 */
class RegViewModel @Inject constructor(
    private val interactor: UserInteractor,
    private val imageConverter: ImageConverter
) : ViewModel() {

    /** прихранитель изображения пользователя */
    var buffImage: Bitmap? = null

    /** Прослушивание статуса загрузки. */
    val progress = MutableLiveData<Boolean>()
    /** Прослушивание статуса выполнения. */
    val result = MutableLiveData<Boolean>()

    /**
     * Регистрация пользователя.
     * @param email - почтовый адрес пользователя.
     * @param password - пароль пользователя.
     * @param firstName - имя пользователя.
     * @param lastName - фамилия пользователя.
     * @param image - изображение пользователя.
     */
    fun registrationUser(
        email: String, password: String, firstName: String, lastName: String, image: Bitmap?
    ) {
        val byteArray =
            if (image != null) imageConverter.convert(image.copy(image.config, true)) else null

        val user = Completable.fromCallable {
            return@fromCallable interactor.registrationUser(
                email, password, firstName, lastName, byteArray
            )
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doFinally { progress.value = false }
            .doOnSubscribe { progress.value = true }

        user.subscribe(
            { result.value = true })
        { result.value = false }
    }
}
