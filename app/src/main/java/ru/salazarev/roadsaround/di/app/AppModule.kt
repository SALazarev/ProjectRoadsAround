package ru.salazarev.roadsaround.di.app

import android.content.Context
import android.content.res.Resources
import dagger.Module
import dagger.Provides
import ru.salazarev.roadsaround.App


/** Модуль Dagger 2, предоставляющий базовые зависимости в приложении. */
@Module
interface AppModule {

    companion object {

        /** Предоставляет конекст приложения.
         * @param app - основной класс приложения.
         */
        @AppScope
        @Provides
        fun provideAppContext(app: App): Context = app.applicationContext

        /** Предоставляет объект доступа к ресурсам приложения.
         * @param app - основной класс приложения.
         */
        @AppScope
        @Provides
        fun provideAppResources(app: App): Resources = app.resources
    }

}