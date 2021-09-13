package ru.salazarev.roadsaround.presentation

import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import ru.salazarev.roadsaround.App
import ru.salazarev.roadsaround.R
import javax.inject.Inject
import javax.inject.Named

class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var fireBaseAuth: FirebaseAuth

    @Inject
    @Named("mainNavController")
    lateinit var navController: NavController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        App.appComponent.getMainComponentBuilder().fragmentManager(supportFragmentManager).build()
            .inject(this)
    }

    fun hasPermission(permission: String): Boolean =
            ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

}