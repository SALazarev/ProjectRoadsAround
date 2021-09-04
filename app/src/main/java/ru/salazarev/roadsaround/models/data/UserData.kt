package ru.salazarev.roadsaround.models.data

import com.google.firebase.firestore.PropertyName
import ru.salazarev.roadsaround.data.DataCollections
import javax.inject.Named

class UserData(
    @get:PropertyName(DataCollections.Users.Columns.id)
    @set:PropertyName(DataCollections.Users.Columns.id)
    var id: String = "",
    @get:PropertyName(DataCollections.Users.Columns.firstName)
    @set:PropertyName(DataCollections.Users.Columns.firstName)
    var firstName: String = "",
    @get:PropertyName(DataCollections.Users.Columns.lastName)
    @set:PropertyName(DataCollections.Users.Columns.lastName)
    var lastName: String = "",
    @get:PropertyName(DataCollections.Users.Columns.image)
    @set:PropertyName(DataCollections.Users.Columns.image)
    var image: String = ""
)