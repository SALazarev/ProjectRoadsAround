package ru.salazarev.roadsaround.di.app

import dagger.BindsInstance
import dagger.Component
import ru.salazarev.roadsaround.App
import ru.salazarev.roadsaround.di.MainComponent
import ru.salazarev.roadsaround.util.ImageConverter


/** Компонент Dagger 2, предоставляющий базовые зависимости в приложении. */
@AppScope
@Component(modules = [AppModule::class])
interface AppComponent {

    /** Предоставляет билдер [MainComponent]. */
    fun getMainComponentBuilder(): MainComponent.Builder

    /** Предоставляет зависимости в [ImageConverter].
     * @param imageConverter - класс преобразования изображений.
     */
    fun inject(imageConverter: ImageConverter)

    /** Предоставляет зависимости в [App].
     * @param app - основной класс приложения.
     */
    fun inject(app: App)

    /** Билдер [AppComponent]. */
    @Component.Builder
    interface Builder {

        /** Предоставление зависимостей из [App].
         * @param app - основной класс приложения.
         */
        @BindsInstance
        fun app(app: App): Builder

        /** Сборка объекта [AppComponent]. */
        fun build(): AppComponent
    }

}