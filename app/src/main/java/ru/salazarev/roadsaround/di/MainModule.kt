package ru.salazarev.roadsaround.di

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import ru.salazarev.roadsaround.R
import javax.inject.Named
import javax.inject.Singleton

@Module
interface MainModule {
    companion object {
        @Provides
        @Singleton
        fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

        @Provides
        @Singleton
        @Named("mainNavController")
        fun provideMainNavigationController(
            fm: FragmentManager,
            auth: FirebaseAuth
        ): NavController {
            val navHostFragment =
                fm.findFragmentById(R.id.fragment_container_host) as NavHostFragment
            val navController = navHostFragment.navController
            val graphInflater = navHostFragment.navController.navInflater
            val navGraph = graphInflater.inflate(R.navigation.nav_graph)
            val destination = if (auth.currentUser != null) R.id.mainFragment else R.id.authFragment
            navGraph.startDestination = destination
            navController.graph = navGraph
            return navController
        }

        @Provides
        @Singleton
        @Named("secondNavController")
        fun provideSecondNavigationController(fm: FragmentManager): NavController {
            val navHostFragment =
                fm.findFragmentById(R.id.fragment_container_second) as NavHostFragment
            return navHostFragment.navController
        }

        @Provides
        fun provideSavedStateHandle():SavedStateHandle = SavedStateHandle()
    }
}