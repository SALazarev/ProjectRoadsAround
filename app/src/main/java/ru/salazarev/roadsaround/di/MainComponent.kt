package ru.salazarev.roadsaround.di

import androidx.fragment.app.FragmentManager
import dagger.BindsInstance
import dagger.Subcomponent
import ru.salazarev.roadsaround.presentation.MainActivity
import ru.salazarev.roadsaround.presentation.authentification.AuthFragment
import ru.salazarev.roadsaround.presentation.chat.ChatFragment
import ru.salazarev.roadsaround.presentation.editevent.EditEventFragment
import ru.salazarev.roadsaround.presentation.editroad.EditRoadFragment
import ru.salazarev.roadsaround.presentation.eventinformation.EventInformationFragment
import ru.salazarev.roadsaround.presentation.main.MainFragment
import ru.salazarev.roadsaround.presentation.profile.ProfileFragment
import ru.salazarev.roadsaround.presentation.registration.RegFragment
import ru.salazarev.roadsaround.presentation.routes.RoutesFragment
import ru.salazarev.roadsaround.presentation.searchevent.SearchEventFragment
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
    fun inject(editEventFragment: EditEventFragment)
    fun inject(editRoadFragment: EditRoadFragment)
    fun inject(searchEventFragment: SearchEventFragment)
    fun inject(eventInformationFragment: EventInformationFragment)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance
        fun fragmentManager(fm: FragmentManager): Builder
        fun build(): MainComponent
    }
}