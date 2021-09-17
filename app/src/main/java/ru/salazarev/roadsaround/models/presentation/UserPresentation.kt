package ru.salazarev.roadsaround.models.presentation

import android.graphics.drawable.Drawable

/**
 * Модель пользователя уровня представления.
 * @param id - идентификатор пользователя.
 * @param name - имя и фамилия пользователя.
 * @param image - изображение пользователя.
 */
class UserPresentation(val id: String, val name: String, val image: Drawable?)