package ru.salazarev.roadsaround.di

import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import ru.salazarev.roadsaround.R

@Module
interface AppModule {
    companion object {
        @Provides
        fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

        @Provides
        fun provideNavigationController(fm: FragmentManager, auth: FirebaseAuth): NavController {
            val navHostFragment = fm.findFragmentById(R.id.fragment_container_host) as NavHostFragment
            val navController = navHostFragment.navController
            val graphInflater = navHostFragment.navController.navInflater
            val navGraph = graphInflater.inflate(R.navigation.nav_graph)
            val destination = if (auth.currentUser != null) R.id.mainFragment else R.id.authFragment
            navGraph.startDestination = destination
            navController.graph = navGraph
            return navController
        }
    }
}