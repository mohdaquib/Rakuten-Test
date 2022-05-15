package com.example.github.repositories.ui

import androidx.lifecycle.ViewModel
import com.example.github.repositories.data.LocalDataStore
import com.example.github.repositories.data.RepositoryDTO
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val localDataStore: LocalDataStore) :
    ViewModel() {

    fun getBookmarks() = localDataStore.getBookmarks()

    fun bookmarkRepo(repositoryDTO: RepositoryDTO, bookmarked: Boolean) {
        localDataStore.bookmarkRepo(repositoryDTO, bookmarked)
    }
}