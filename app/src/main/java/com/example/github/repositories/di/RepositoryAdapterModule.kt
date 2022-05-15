package com.example.github.repositories.di

import com.example.github.repositories.data.LocalDataStore
import com.example.github.repositories.ui.RepositoryAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module(includes = [LocalDataStoreModule::class])
@InstallIn(FragmentComponent::class)
object RepositoryAdapterModule {

    @Provides
    fun provideRepositoryAdapter(
        localDataStore: LocalDataStore
    ): RepositoryAdapter =
        RepositoryAdapter(localDataStore)
}