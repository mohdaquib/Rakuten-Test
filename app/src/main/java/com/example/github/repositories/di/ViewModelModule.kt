package com.example.github.repositories.di

import com.example.github.repositories.data.repository.search.SearchRepository
import com.example.github.repositories.data.repository.search.SearchRepositoryImpl
import com.example.github.repositories.data.repository.user.GithubUserRepository
import com.example.github.repositories.data.repository.user.GithubUserRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class ViewModelModule {
    @Binds
    abstract fun bindsSearchRepository(searchRepositoryImpl: SearchRepositoryImpl): SearchRepository
    @Binds
    abstract fun bindsGithubUserRepository(githubUserRepository: GithubUserRepositoryImpl): GithubUserRepository
}