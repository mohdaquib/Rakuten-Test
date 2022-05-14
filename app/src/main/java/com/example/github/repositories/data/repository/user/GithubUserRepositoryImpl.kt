package com.example.github.repositories.data.repository.user

import com.example.github.repositories.data.GitHubEndpoints
import com.example.github.repositories.data.RepositoryDTO
import com.example.github.repositories.data.UserDTO
import javax.inject.Inject

class GithubUserRepositoryImpl @Inject constructor(private val service: GitHubEndpoints) :
    GithubUserRepository {
    override suspend fun getUser(username: String): UserDTO {
        return service.getUser(username)
    }

    override suspend fun getUserRepositories(userRepo: String): List<RepositoryDTO> {
        return service.getUserRepositories(userRepo)
    }
}