package com.example.github.repositories.data.repository.user

import com.example.github.repositories.data.RepositoryDTO
import com.example.github.repositories.data.UserDTO

interface GithubUserRepository {
    suspend fun getUser(username: String): UserDTO
    suspend fun getUserRepositories(userRepo: String): List<RepositoryDTO>
}