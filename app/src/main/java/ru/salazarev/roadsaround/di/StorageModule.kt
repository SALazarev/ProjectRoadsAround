package ru.salazarev.roadsaround.di

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import ru.salazarev.roadsaround.data.UserRepositoryImpl
import ru.salazarev.roadsaround.domain.UserRepository

@Module
interface StorageModule {
    companion object {
        private const val DATABASE_ADRESS =
            "https://roadsaround-970e9-default-rtdb.europe-west1.firebasedatabase.app/"
        private const val USER_KEY = "USER"

        @Provides
        fun provideFirebaseDatabase(): DatabaseReference =
            FirebaseDatabase
                .getInstance(DATABASE_ADRESS)
                .getReference(USER_KEY)
    }

    @Binds
    fun bindJsonWorker(repo: UserRepositoryImpl): UserRepository
}