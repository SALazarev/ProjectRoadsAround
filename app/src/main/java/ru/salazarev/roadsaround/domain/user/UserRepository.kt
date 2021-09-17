package ru.salazarev.roadsaround.domain.user

import ru.salazarev.roadsaround.models.domain.User

/** Интерфейс хранилища данных о пользователях. */
interface UserRepository {

    /**
     * Предоставление информации о пользователе.
     * @param id - идентификатор пользователя.
     * @return информацию о пользователе.
     */
    fun getUserData(id: String): User

    /**
     * Отправка информации о пользователе в хранилище.
     * @param user - информация о пользователе.
     */
    fun setUserData(user: User)

    /**
     * Предоставление информации о пользователях.
     * @param idList - список идентификаторов.
     * @return списко объектов информации о пользователях.
     */
    fun getUsersData(idList: List<String>): List<User>
}