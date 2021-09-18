package ru.salazarev.roadsaround.network

/** Интерфейса авторизации пользователя. */
interface Authentication {

    /** Регистрация пользователя.
     * @param email - почта пользователя.
     * @param password - пароль пользователя.
     */
    fun registration(email: String, password: String): String

    /** Авторизация пользователя.
     * @param email - почта пользователя.
     * @param password - пароль пользователя.
     */
    fun authentication(email: String, password: String): String

    /** Сброс пароля.
     * @param email - почта пользователя.
     */
    fun resetPassword(email: String)

    /** Возвращает идентификатор пользователя.
     * @return идентификатор пользователя.
     */
    fun getUserId(): String
}