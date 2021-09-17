package ru.salazarev.roadsaround.di

import androidx.lifecycle.SavedStateHandle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.salazarev.roadsaround.data.chat.ChatRepositoryImpl
import ru.salazarev.roadsaround.data.chat.MessagesCollectionModel
import ru.salazarev.roadsaround.data.event.EventRepositoryImpl
import ru.salazarev.roadsaround.data.event.EventsCollectionModel
import ru.salazarev.roadsaround.data.user.UsersCollectionModel
import ru.salazarev.roadsaround.data.user.ImageStorageHelper
import ru.salazarev.roadsaround.data.user.UserRepositoryImpl
import ru.salazarev.roadsaround.domain.chat.ChatRepository
import ru.salazarev.roadsaround.domain.event.EventRepository
import ru.salazarev.roadsaround.network.Authentication
import ru.salazarev.roadsaround.domain.user.UserRepository
import ru.salazarev.roadsaround.network.AuthenticationImpl
import javax.inject.Singleton

/** Модуль Dagger 2, предоставляющий зависимости хранения данных. */
@Module
interface StorageModule {
    companion object {

        /** Предоставляет объект для хранения информации во ViewModel. */
        @Provides
        fun provideSavedStateHandle(): SavedStateHandle = SavedStateHandle()

        /** Предоставляет объект для авторизации в системе Firebase. */
        @Provides
        @Singleton
        fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

        /** Предоставляет объект для доступа к базе данных Firebase Cloud Firestore. */
        @Singleton
        @Provides
        fun provideFirebaseDatabase(): FirebaseFirestore = Firebase.firestore

        /** Предоставляет объект для доступа к файлам хранилища Firebase Storage. */
        @Singleton
        @Provides
        fun provideFirebaseStorageReference(): StorageReference = Firebase.storage.reference

        /** Предоставляет объект для доступа к информации о конфигурации хранилища изображений
         * Firebase Storage.
         */
        @Singleton
        @Provides
        fun provideImageStorageHelper(): ImageStorageHelper = ImageStorageHelper

        /** Предоставляет объект, содержащий информацию о структуре модели пользователя
         *  хранилища Firebase.
         */
        @Singleton
        @Provides
        fun provideUsersCollectionsModel(): UsersCollectionModel = UsersCollectionModel

        /** Предоставляет объект, содержащий информацию о структуре модели сообщения хранилища
         * Firebase.
         */
        @Singleton
        @Provides
        fun provideMessagesCollectionsModel(): MessagesCollectionModel = MessagesCollectionModel

        /** Предоставляет объект, содержащий информацию о структуре модели события хранилища
         * Firebase.
         */
        @Singleton
        @Provides
        fun provideEventsCollectionsModel(): EventsCollectionModel = EventsCollectionModel
    }

    /** Установка связи для Dagger 2 между интерфейсом [UserRepository]
     * и реализующим его классом [UserRepositoryImpl]
     */
    @Binds
    fun bindUserRepository(repo: UserRepositoryImpl): UserRepository

    /** Установка связи для Dagger 2 между интерфейсом [ChatRepository]
     * и реализующим его классом [ChatRepositoryImpl]
     */
    @Binds
    fun bindChatRepository(repo: ChatRepositoryImpl): ChatRepository

    /** Установка связи для Dagger 2 между интерфейсом [EventRepository]
     * и реализующим его классом [EventRepositoryImpl]
     */
    @Binds
    fun bindEventRepository(repo: EventRepositoryImpl): EventRepository

    /** Установка связи для Dagger 2 между интерфейсом [Authentication]
     * и реализующим его классом [AuthenticationImpl]
     */
    @Binds
    fun bindAuthentication(auth: AuthenticationImpl): Authentication
}