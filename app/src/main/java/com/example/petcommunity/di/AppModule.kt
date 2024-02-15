package com.example.petcommunity.di

import android.content.Context
import com.example.petcommunity.data.local_data.DataStoreManager
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideFireBaseAuth():FirebaseAuth{
        return FirebaseAuth.getInstance()
    }

    @Singleton
    @Provides
    fun provideDataStoreManager(@ApplicationContext context: Context):DataStoreManager{
        return DataStoreManager(context)
    }
}