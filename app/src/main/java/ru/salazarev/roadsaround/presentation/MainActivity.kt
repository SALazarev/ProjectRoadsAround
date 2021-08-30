package ru.salazarev.roadsaround.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.auth.FirebaseAuth
import ru.salazarev.roadsaround.R

class MainActivity : AppCompatActivity() {
    lateinit var fireBaseAuth: FirebaseAuth
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        setFireBase()
    }

    private fun setFireBase() {
        fireBaseAuth = FirebaseAuth.getInstance()
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) navController.navigate(R.id.action_authFragment_to_mainFragment)
    }
}