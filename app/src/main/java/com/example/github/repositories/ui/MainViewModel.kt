package com.example.github.repositories.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.github.repositories.data.ORDER
import com.example.github.repositories.data.QUERY
import com.example.github.repositories.data.RepositoryDTO
import com.example.github.repositories.data.SORT
import com.example.github.repositories.data.networking.ICoroutineDispatcher
import com.example.github.repositories.data.repository.search.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
    private val coroutineDispatcher: ICoroutineDispatcher
) : ViewModel() {
    private val repositoriesLiveData = MutableLiveData<SearchReposState>()
    val repositories: LiveData<SearchReposState> get() = repositoriesLiveData

    private val refreshReposLiveData = MutableLiveData<RefreshRepoState>()
    val refreshRepos: LiveData<RefreshRepoState> get() = refreshReposLiveData

    fun fetchItems() {
        viewModelScope.launch(coroutineDispatcher.main()) {
            repositoriesLiveData.value = SearchReposState.Loading
            delay(1_000) // This is to simulate network latency, please don't remove!
            withContext(coroutineDispatcher.io()) {
                try {
                    val response = searchRepository.searchRepositories(QUERY, SORT, ORDER)
                    repositoriesLiveData.postValue(SearchReposState.Success(response.items))
                } catch (exception: Exception) {
                    repositoriesLiveData.postValue(SearchReposState.Error(exception))
                }
            }
        }
    }

    fun refresh() {
        viewModelScope.launch(coroutineDispatcher.main()) {
            delay(1_000) // This is to simulate network latency, please don't remove!
            withContext(coroutineDispatcher.io()) {
                try {
                    val response = searchRepository.searchRepositories(QUERY, SORT, ORDER)
                    refreshReposLiveData.postValue(RefreshRepoState.Success(response.items))
                } catch (exception: Exception) {
                    refreshReposLiveData.postValue(RefreshRepoState.Error(exception))
                }
            }
        }
    }

    sealed class SearchReposState {
        object Loading : SearchReposState()
        data class Success(val list: List<RepositoryDTO>) : SearchReposState()
        data class Error(val error: Exception) : SearchReposState()
    }

    sealed class RefreshRepoState {
        data class Success(val list: List<RepositoryDTO>) : RefreshRepoState()
        data class Error(val error: Exception) : RefreshRepoState()
    }
}