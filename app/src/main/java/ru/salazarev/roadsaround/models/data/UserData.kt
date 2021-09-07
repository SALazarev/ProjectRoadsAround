package ru.salazarev.roadsaround.models.data

import com.google.firebase.firestore.PropertyName
import ru.salazarev.roadsaround.data.user.UsersCollectionModel

class UserData(
    @get:PropertyName(UsersCollectionModel.getUsers().getColumns().id)
    @set:PropertyName(UsersCollectionModel.getUsers().getColumns().id)
    var id: String = "",
    @get:PropertyName(UsersCollectionModel.getUsers().getColumns().firstName)
    @set:PropertyName(UsersCollectionModel.getUsers().getColumns().firstName)
    var firstName: String = "",
    @get:PropertyName(UsersCollectionModel.getUsers().getColumns().lastName)
    @set:PropertyName(UsersCollectionModel.getUsers().getColumns().lastName)
    var lastName: String = "",
    @get:PropertyName(UsersCollectionModel.getUsers().getColumns().image)
    @set:PropertyName(UsersCollectionModel.getUsers().getColumns().image)
    var image: String = ""
)