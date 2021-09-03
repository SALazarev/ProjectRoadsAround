package ru.salazarev.roadsaround.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import ru.salazarev.roadsaround.R
import ru.salazarev.roadsaround.di.DaggerAppComponent
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
        DaggerAppComponent.builder().fragmentManager(supportFragmentManager).build().inject(this)
    }

}