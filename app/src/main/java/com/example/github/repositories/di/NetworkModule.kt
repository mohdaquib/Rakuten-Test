package com.example.github.repositories.di

import com.example.github.repositories.BuildConfig
import com.example.github.repositories.data.LocalDataStore
import com.example.github.repositories.data.networking.CoroutineDispatcherImpl
import com.example.github.repositories.data.networking.ICoroutineDispatcher
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.GITHUB_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideCoroutineDispatcher(): ICoroutineDispatcher = CoroutineDispatcherImpl()
}