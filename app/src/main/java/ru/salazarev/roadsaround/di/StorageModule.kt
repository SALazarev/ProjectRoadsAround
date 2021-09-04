package ru.salazarev.roadsaround.di

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
import ru.salazarev.roadsaround.data.UserRepositoryImpl
import ru.salazarev.roadsaround.domain.UserRepository
import ru.salazarev.roadsaround.models.presentation.User

@Module
interface StorageModule {
    companion object {
        @Provides
        fun provideFirebaseDatabase(): FirebaseFirestore = Firebase.firestore

        @Provides
        fun provideFirebaseStorage(): StorageReference = Firebase.storage.reference

        @Provides
        fun provideUserLiveData(): MutableLiveData<User> = MutableLiveData<User>()
    }


    @Binds
    fun bindJsonWorker(repo: UserRepositoryImpl): UserRepository
}