package com.example.github.repositories

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.github.repositories.data.GitHubEndpoints
import com.example.github.repositories.data.RepositoryDTO
import com.example.github.repositories.data.UserDTO
import com.example.github.repositories.data.networking.ICoroutineDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val service: GitHubEndpoints,
private val coroutineDispatcher: ICoroutineDispatcher): ViewModel() {
    val user = MutableLiveData<UserDTO>()
    val repositories = MutableLiveData<List<RepositoryDTO>>()

    fun fetchUser(username: String) {
        // FIXME Use the proper scope
        GlobalScope.launch(coroutineDispatcher.io()) {
            delay(1_000) // This is to simulate network latency, please don't remove!
            val response = service.getUser(username).execute()
            user.postValue(response.body()!!)
        }
    }

    fun fetchRepositories(reposUrl: String) {
        GlobalScope.launch(coroutineDispatcher.io()) {
            delay(1_000) // This is to simulate network latency, please don't remove!
            val response = service.getUserRepositories(reposUrl).execute()
            repositories.postValue(response.body()!!)
        }
    }
}