package ru.salazarev.roadsaround.di.app

import dagger.BindsInstance
import dagger.Component
import ru.salazarev.roadsaround.App
import ru.salazarev.roadsaround.di.MainComponent
import ru.salazarev.roadsaround.util.ImageConverter

@AppScope
@Component(modules = [AppModule::class])
interface AppComponent {
    fun getMainComponentBuilder(): MainComponent.Builder

    fun inject(imageConverter: ImageConverter)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun app(app: App): Builder
        fun build(): AppComponent
    }

}