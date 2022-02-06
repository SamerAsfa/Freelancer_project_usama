package com.example.myapplication.myapplication.di

import com.example.myapplication.myapplication.data.UserDataProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class ApplicationModule {

    @Provides
    fun fireBase(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    fun fireBaseStorage(): FirebaseStorage {
        return FirebaseStorage.getInstance()
    }

    @Provides
    fun provideUserDataProvider(): UserDataProvider {
        return UserDataProvider(fireBase(),fireBaseStorage())
    }
}