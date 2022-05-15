package com.example.github.repositories.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.github.repositories.data.RepositoryDTO
import com.example.github.repositories.data.UserDTO
import com.example.github.repositories.data.networking.ICoroutineDispatcher
import com.example.github.repositories.data.repository.user.GithubUserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: GithubUserRepository,
    private val coroutineDispatcher: ICoroutineDispatcher
) : ViewModel() {
    private val userLiveData = MutableLiveData<UserState>()
    val user: LiveData<UserState> get() = userLiveData

    private val repositoriesLiveData = MutableLiveData<UserReposState>()
    val repositories: LiveData<UserReposState> get() = repositoriesLiveData

    fun fetchUser(username: String) {
        viewModelScope.launch(coroutineDispatcher.io()) {
            delay(1_000) // This is to simulate network latency, please don't remove!
            try {
                val response = userRepository.getUser(username)
                userLiveData.postValue(UserState.Success(response))
            } catch (exception: Exception) {
                userLiveData.postValue(exception.message?.let { UserState.Error(it) })
            }
        }
    }

    fun fetchRepositories(reposUrl: String) {
        viewModelScope.launch(coroutineDispatcher.io()) {
            delay(1_000) // This is to simulate network latency, please don't remove!
            try {
                val response = userRepository.getUserRepositories(reposUrl)
                repositoriesLiveData.postValue(UserReposState.Success(response))
            } catch (exception: Exception) {
                repositoriesLiveData.postValue(exception.message?.let { UserReposState.Error(it) })
            }
        }
    }

    fun getTitleText(twitterHandleLabel: String, username: String): String {
        return String.format("%s %s", twitterHandleLabel, username)
    }

    sealed class UserState {
        data class Success(val result: UserDTO) : UserState()
        data class Error(val error: String) : UserState()
    }

    sealed class UserReposState {
        data class Success(val result: List<RepositoryDTO>): UserReposState()
        data class Error(val error: String): UserReposState()
    }
}