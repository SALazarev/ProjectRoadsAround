package ru.salazarev.roadsaround.di

import androidx.fragment.app.FragmentManager
import dagger.BindsInstance
import dagger.Component
import dagger.Subcomponent
import ru.salazarev.roadsaround.presentation.MainActivity
import ru.salazarev.roadsaround.presentation.authentification.AuthFragment
import ru.salazarev.roadsaround.presentation.chat.ChatFragment
import ru.salazarev.roadsaround.presentation.main.MainFragment
import ru.salazarev.roadsaround.presentation.profile.ProfileFragment
import ru.salazarev.roadsaround.presentation.registration.RegFragment
import ru.salazarev.roadsaround.presentation.routes.RoutesFragment
import javax.inject.Singleton

@Singleton
@Subcomponent(modules = [MainModule::class, StorageModule::class])
interface MainComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(mainFragment: MainFragment)
    fun inject(regFragment: RegFragment)
    fun inject(profileFragment: ProfileFragment)
    fun inject(routesFragment: RoutesFragment)
    fun inject(chatFragment: ChatFragment)
    fun inject(authFragment: AuthFragment)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun fragmentManager(fm: FragmentManager): Builder
        fun build(): MainComponent
    }
}