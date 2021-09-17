package ru.salazarev.roadsaround.di.app

import android.content.Context
import android.content.res.Resources
import dagger.Module
import dagger.Provides
import ru.salazarev.roadsaround.App

@Module
interface AppModule {
    companion object {
        @AppScope
        @Provides
        fun provideAppContext(app: App): Context = app.applicationContext

        @AppScope
        @Provides
        fun provideAppResources(app: App): Resources = app.resources
    }

}