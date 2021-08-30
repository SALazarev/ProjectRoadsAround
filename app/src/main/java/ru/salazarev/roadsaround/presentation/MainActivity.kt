package ru.salazarev.roadsaround.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.auth.FirebaseAuth
import ru.salazarev.roadsaround.R

class MainActivity : AppCompatActivity() {
    lateinit var fireBaseAuth: FirebaseAuth
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setFireBase()
        setNavigation()
    }

    private fun setFireBase() {
        fireBaseAuth = FirebaseAuth.getInstance()
    }

    private fun setNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        val graphInflater = navHostFragment.navController.navInflater
        val navGraph = graphInflater.inflate(R.navigation.nav_graph)
        val destination =
            if (fireBaseAuth.currentUser != null) R.id.mainFragment else R.id.authFragment
        navGraph.startDestination = destination
        navController.graph = navGraph
    }
}