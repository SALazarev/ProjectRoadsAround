package ru.salazarev.roadsaround.models.data

import com.google.firebase.firestore.PropertyName
import ru.salazarev.roadsaround.data.DataCollectionsModel

class UserData(
    @get:PropertyName(DataCollectionsModel.getUsers().getColumns().id)
    @set:PropertyName(DataCollectionsModel.getUsers().getColumns().id)
    var id: String = "",
    @get:PropertyName(DataCollectionsModel.getUsers().getColumns().firstName)
    @set:PropertyName(DataCollectionsModel.getUsers().getColumns().firstName)
    var firstName: String = "",
    @get:PropertyName(DataCollectionsModel.getUsers().getColumns().lastName)
    @set:PropertyName(DataCollectionsModel.getUsers().getColumns().lastName)
    var lastName: String = "",
    @get:PropertyName(DataCollectionsModel.getUsers().getColumns().image)
    @set:PropertyName(DataCollectionsModel.getUsers().getColumns().image)
    var image: String = ""
)