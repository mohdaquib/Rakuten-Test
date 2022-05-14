package com.example.github.repositories.data.repository.search

import com.example.github.repositories.data.GitHubEndpoints
import com.example.github.repositories.data.Response
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(private val service: GitHubEndpoints) :
    SearchRepository {
    override suspend fun searchRepositories(q: String, sort: String, order: String): Response {
        return service.searchRepositories(q, sort, order)
    }
}