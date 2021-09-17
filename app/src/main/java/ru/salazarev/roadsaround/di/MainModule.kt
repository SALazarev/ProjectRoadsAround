package ru.salazarev.roadsaround.di

import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import ru.salazarev.roadsaround.R
import javax.inject.Named
import javax.inject.Singleton

/** Модуль Dagger 2, предоставляющий зависимости управления фрагментами. */
@Module
interface MainModule {
    companion object {

        /**
         * Предоставляет [NavController], сконфигурированный для запуска приложения с различными
         * стартовыми экранами.
         * @param fm - основной менеджер фрагментов.
         * @param auth - объект авторизации в системе Firebase.
         */
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

        /**
         * Предоставляет вложенный [NavController], предназначенный для управления основными
         * экранами приложения.
         * @param fm - второстепенный менеджер фрагментов.
         */
        @Provides
        @Singleton
        @Named("secondNavController")
        fun provideSecondNavigationController(fm: FragmentManager): NavController {
            val navHostFragment =
                fm.findFragmentById(R.id.fragment_container_second) as NavHostFragment
            return navHostFragment.navController
        }

    }
}