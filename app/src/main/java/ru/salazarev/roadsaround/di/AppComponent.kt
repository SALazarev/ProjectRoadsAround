package ru.salazarev.roadsaround.di

import dagger.Component
import ru.salazarev.roadsaround.presentation.MainActivity
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun inject(mainActivity: MainActivity)
}