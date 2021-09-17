package ru.salazarev.roadsaround.models.data

import com.google.firebase.firestore.PropertyName
import ru.salazarev.roadsaround.data.user.UsersCollectionModel

/**
 * Модель пользователя уровня хранения данных.
 * @param id - идентификатор пользователя.
 * @param firstName - имя пользователя.
 * @param lastName - фамилия пользователя.
 * @param image - изображение пользователя.
 */
class UserData(
    @get:PropertyName(UsersCollectionModel.getUser().getColumns().id)
    @set:PropertyName(UsersCollectionModel.getUser().getColumns().id)
    var id: String = "",
    @get:PropertyName(UsersCollectionModel.getUser().getColumns().firstName)
    @set:PropertyName(UsersCollectionModel.getUser().getColumns().firstName)
    var firstName: String = "",
    @get:PropertyName(UsersCollectionModel.getUser().getColumns().lastName)
    @set:PropertyName(UsersCollectionModel.getUser().getColumns().lastName)
    var lastName: String = "",
    @get:PropertyName(UsersCollectionModel.getUser().getColumns().image)
    @set:PropertyName(UsersCollectionModel.getUser().getColumns().image)
    var image: String = ""
)