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
import ru.salazarev.roadsaround.presentation.members.MembersFragment
import ru.salazarev.roadsaround.presentation.profile.ProfileFragment
import ru.salazarev.roadsaround.presentation.registration.RegFragment
import ru.salazarev.roadsaround.presentation.routes.RoutesFragment
import ru.salazarev.roadsaround.presentation.searchevent.SearchEventFragment
import javax.inject.Singleton

/**
 * Основной компонент Dagger 2, предоставляющий зависимости хранения информации и управления
 * фрагментами в приложении.
 */
@Singleton
@Subcomponent(modules = [MainModule::class, StorageModule::class])
interface MainComponent {

    /** Предоставляет зависимости в [MainActivity].
     * @param mainActivity - единственная активити в приложении.
     */
    fun inject(mainActivity: MainActivity)

    /** Предоставляет зависимости в [MainFragment].
     * @param mainFragment - фрагмент приложения.
     */
    fun inject(mainFragment: MainFragment)

    /** Предоставляет зависимости в [RegFragment].
     * @param regFragment - фрагмент приложения.
     */
    fun inject(regFragment: RegFragment)

    /** Предоставляет зависимости в [ProfileFragment].
     * @param profileFragment - фрагмент приложения.
     */
    fun inject(profileFragment: ProfileFragment)

    /** Предоставляет зависимости в [RoutesFragment].
     * @param routesFragment - фрагмент приложения.
     */
    fun inject(routesFragment: RoutesFragment)

    /** Предоставляет зависимости в [ChatFragment].
     * @param chatFragment - фрагмент приложения.
     */
    fun inject(chatFragment: ChatFragment)

    /** Предоставляет зависимости в [AuthFragment].
     * @param authFragment - фрагмент приложения.
     */
    fun inject(authFragment: AuthFragment)

    /** Предоставляет зависимости в [EditEventFragment].
     * @param editEventFragment - фрагмент приложения.
     */
    fun inject(editEventFragment: EditEventFragment)

    /** Предоставляет зависимости в [EditRoadFragment].
     * @param editRoadFragment - фрагмент приложения.
     */
    fun inject(editRoadFragment: EditRoadFragment)

    /** Предоставляет зависимости в [SearchEventFragment].
     * @param searchEventFragment - фрагмент приложения.
     */
    fun inject(searchEventFragment: SearchEventFragment)

    /** Предоставляет зависимости в [EventInformationFragment].
     * @param eventInformationFragment - фрагмент приложения.
     */
    fun inject(eventInformationFragment: EventInformationFragment)

    /** Предоставляет зависимости в [MembersFragment].
     * @param membersFragment - фрагмент приложения.
     */
    fun inject(membersFragment: MembersFragment)

    /** Билдер [MainComponent]. */
    @Subcomponent.Builder
    interface Builder {
        /** Предоставление зависимостей из [FragmentManager].
         * @param fm - менеджер фрагментов.
         */
        @BindsInstance
        fun fragmentManager(fm: FragmentManager): Builder

        /** Сборка объекта [MainComponent]. */
        fun build(): MainComponent
    }
}