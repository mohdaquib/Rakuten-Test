package com.example.github.repositories.di

import android.app.Application
import android.content.Context
import com.example.github.repositories.data.LocalDataStore
import com.example.github.repositories.data.RepositoryDTO
import com.example.github.repositories.ui.RepositoryAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent

@Module(includes = [LocalDataStoreModule::class])
@InstallIn(FragmentComponent::class)
object RepositoryAdapterModule {

    @Provides
    fun provideContext(application: Application) = application.applicationContext

    @Provides
    fun provideRepositoryAdapter(
        context: Context,
        localDataStore: LocalDataStore
    ): RepositoryAdapter =
        RepositoryAdapter(context, localDataStore)
}