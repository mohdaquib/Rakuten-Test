package com.example.github.repositories.di

import com.example.github.repositories.data.LocalDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object LocalDataStoreModule {
    @Provides
    fun provideLocalDataStore(): LocalDataStore = LocalDataStore.instance
}