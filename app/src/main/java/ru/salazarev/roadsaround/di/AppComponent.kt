package ru.salazarev.roadsaround.di

import androidx.fragment.app.FragmentManager
import dagger.BindsInstance
import dagger.Component
import dagger.Subcomponent
import ru.salazarev.roadsaround.presentation.MainActivity
import ru.salazarev.roadsaround.presentation.main.MainFragment
import ru.salazarev.roadsaround.presentation.registration.RegFragment
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class,StorageModule::class])
interface AppComponent {
    fun inject(mainActivity: MainActivity)
    fun inject(mainFragment: MainFragment)
    fun inject(regFragment: RegFragment)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun fragmentManager(fm: FragmentManager): Builder
        fun build(): AppComponent
    }
}