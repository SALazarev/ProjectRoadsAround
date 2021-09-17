package ru.salazarev.roadsaround.domain.user

import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Single
import ru.salazarev.roadsaround.models.domain.User
import ru.salazarev.roadsaround.network.Authentication
import javax.inject.Inject

/**
 * Класс, устанавливающий взаимодействие между уровнями хранения данных и представления.
 *  Предназначен для работы с пользователями.
 *
 *  @property userRepository - интерактор работы с информацией о пользователях.
 *  @property authentication - предоставляет информацию о статусе авторизации
 *  и об авторизованном пользователе.
 */
class UserInteractor @Inject constructor(
    private val userRepository: UserRepository,
    private val authentication: Authentication
) {

    /**
     * Регистрация пользователя.
     * @param email - почта пользователя.
     * @param password - пароль пользователя.
     * @param firstName - имя пользователя.
     * @param lastName - фамилия пользователя.
     * @param image - изображение пользователя.
     */
    fun registrationUser(
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        image: ByteArray?
    ) {
        val id = authentication.registration(email, password)
        val user = User(id, firstName, lastName, image)
        userRepository.setUserData(user)
    }

    /**
     * Получение информации о пользователе.
     * @param id - идентификатор пользователя
     * @return объект прослушивания состояния получения информации о пользователе.
     */
    fun getUserData(id: String = authentication.getUserId()): Single<User> = Single.fromCallable {
        userRepository.getUserData(id)
    }

    /**
     * Авторизация пользователя в системе.
     * @param email - почта пользователя.
     * @param password - пароль пользователя.
     * @return объект прослушивания состояния авторизации пользователя.
     */
    fun userAuthentication(email: String, password: String): Completable {
        return Completable.fromCallable { authentication.authentication(email, password) }
    }

    /**
     * Сброс пароля пользователя.
     * @param email - почта пользователя.
     * @return объект прослушивания состояния авторизации пользователя.
     */
    fun resetUserPassword(email: String): Single<Boolean> {
        return Single.fromCallable { authentication.resetPassword(email) }
    }
}