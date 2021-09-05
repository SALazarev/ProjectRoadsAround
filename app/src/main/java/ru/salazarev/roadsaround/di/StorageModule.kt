package ru.salazarev.roadsaround.di

import android.media.Image
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.salazarev.roadsaround.data.DataCollectionsModel
import ru.salazarev.roadsaround.data.ImageStorageHelper
import ru.salazarev.roadsaround.data.UserRepositoryImpl
import ru.salazarev.roadsaround.domain.UserRepository
import ru.salazarev.roadsaround.models.presentation.User
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
        fun provideDataCollectionsModel(): DataCollectionsModel = DataCollectionsModel

        @Provides
        fun provideUserLiveData(): MutableLiveData<User> = MutableLiveData<User>()

        @Provides
        fun provideErrorLiveData(): MutableLiveData<String> = MutableLiveData<String>()
    }


    @Binds
    fun bindJsonWorker(repo: UserRepositoryImpl): UserRepository
}