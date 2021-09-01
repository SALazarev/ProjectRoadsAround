package ru.salazarev.roadsaround.di

import androidx.fragment.app.FragmentManager
import dagger.BindsInstance
import dagger.Component
import dagger.Subcomponent
import ru.salazarev.roadsaround.presentation.MainActivity
import javax.inject.Named
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(mainActivity: MainActivity)


    @Component.Builder
    interface Builder {
        @BindsInstance
        fun fragmentManager(fm: FragmentManager): Builder
        fun build(): AppComponent
    }
}