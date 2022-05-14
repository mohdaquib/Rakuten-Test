package com.example.github.repositories.di

import com.example.github.repositories.data.GitHubEndpoints
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Provides
    fun provideGithubService(retrofit: Retrofit): GitHubEndpoints =
        retrofit.create(GitHubEndpoints::class.java)
}