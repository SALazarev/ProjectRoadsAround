package ru.salazarev.roadsaround.presentation

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import ru.salazarev.roadsaround.App
import ru.salazarev.roadsaround.R
import javax.inject.Inject
import javax.inject.Named

/** Главная активити приложения */
class MainActivity : AppCompatActivity() {

    /** Объект аутентификации в Firebase. */
    @Inject
    lateinit var fireBaseAuth: FirebaseAuth

    /** Основной контроллер управления фрагментами. */
    @Inject
    @Named("mainNavController")
    lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        App.appComponent.getMainComponentBuilder().fragmentManager(supportFragmentManager).build()
            .inject(this)
    }

    /** Проверка наличия разрешения
     * @param permission - запрашиваемое разрешение.
     */
    fun hasPermission(permission: String): Boolean =
        ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

}