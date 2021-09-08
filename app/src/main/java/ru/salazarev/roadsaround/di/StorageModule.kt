package ru.salazarev.roadsaround.di

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.salazarev.roadsaround.data.chat.ChatRepositoryImpl
import ru.salazarev.roadsaround.data.user.UsersCollectionModel
import ru.salazarev.roadsaround.data.user.ImageStorageHelper
import ru.salazarev.roadsaround.data.user.UserRepositoryImpl
import ru.salazarev.roadsaround.domain.chat.ChatRepository
import ru.salazarev.roadsaround.domain.user.Authentication
import ru.salazarev.roadsaround.domain.user.UserRepository
import ru.salazarev.roadsaround.network.AuthenticationImpl
import javax.inject.Singleton

@Module
interface StorageModule {
    companion object {
        @Singleton
        @Provides
        fun provideFirebaseDatabase(): FirebaseFirestore = Firebase.firestore

        @Singleton
        @Provides
        fun provideFirebaseStorage(): StorageReference = Firebase.storage.reference

        @Singleton
        @Provides
        fun provideImageStorageHelper(): ImageStorageHelper = ImageStorageHelper

        @Singleton
        @Provides
        fun provideDataCollectionsModel(): UsersCollectionModel = UsersCollectionModel
    }

    @Binds
    fun bindUserRepository(repo: UserRepositoryImpl): UserRepository

    @Binds
    fun bindChatRepository(repo: ChatRepositoryImpl): ChatRepository

    @Binds
    fun bindAuthentication(auth: AuthenticationImpl): Authentication
}