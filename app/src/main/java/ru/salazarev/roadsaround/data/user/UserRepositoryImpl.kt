package ru.salazarev.roadsaround.data.user

import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import ru.salazarev.roadsaround.domain.user.UserRepository
import ru.salazarev.roadsaround.models.data.UserData
import ru.salazarev.roadsaround.models.domain.User
import java.util.concurrent.ExecutionException
import javax.inject.Inject

/**
 * Класс репозитория, реализующий интерфейс бизнес-логики и позволяющий обращаться к информации
 * о пользователях.
 *
 * @property database Модель общения с базой данных Firebase Cloud Store.
 * @property storage Модель общения с хранилищем файлов Firebase Storage.
 * @property imageHelper Предоставляет информацию о конфигурации хранилища изображений.
 * @property databaseModel Модель преобразования данных уровня хранения в формат базы данных.
 */
class UserRepositoryImpl @Inject constructor(
    private val database: FirebaseFirestore,
    private val storage: StorageReference,
    private val imageHelper: ImageStorageHelper,
    private val databaseModel: UsersCollectionModel
) : UserRepository {

    override fun setUserData(user: User) {
        val path = if (user.image != null) {
            val snapshot = Tasks.await(saveImage(user.id, user.image))
            val uri = snapshot.metadata!!.reference!!.downloadUrl
            uri.toString()
        } else ""
        saveData(UserData(user.id, user.firstName, user.lastName, path))
    }

    override fun getUsersData(idList: List<String>) = idList.map { getUserData(it) }

    override fun getUserData(id: String): User {
        val docRef = database.collection(databaseModel.getUser().collectionName)
            .document(id)
        val snapshot = Tasks.await(docRef.get())
        val userData: UserData = snapshot.toObject<UserData>()!!

        val image = if (userData.image.isNotEmpty()) {
            val path: String =
                imageHelper.let { "${it.folder}/${it.getFileName(id)}.${it.jpegFileFormat}" }
            val islandRef = storage.child(path)
            val arrayBytes = try {
                Tasks.await(islandRef.getBytes(imageHelper.imageBuffer))
            } catch (e: ExecutionException) {
                null
            }
            arrayBytes
        } else null

        return User(id, userData.firstName, userData.lastName, image)
    }

    private fun saveImage(id: String, image: ByteArray): UploadTask {
        val path: String = imageHelper
            .let { "${it.folder}/${it.getFileName(id)}.${it.jpegFileFormat}" }
        return storage.child(path).putBytes(image)
    }

    private fun saveData(userData: UserData) {
        val user = hashMapOf(
            databaseModel.getUser().getColumns().id to userData.id,
            databaseModel.getUser().getColumns().firstName to userData.firstName,
            databaseModel.getUser().getColumns().lastName to userData.lastName,
            databaseModel.getUser().getColumns().image to userData.image
        )
        database.collection(databaseModel.getUser().collectionName)
            .document(userData.id)
            .set(user)
    }
}