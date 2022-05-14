package com.example.github.repositories.data.repository.search

import com.example.github.repositories.data.Response

interface SearchRepository {
    suspend fun searchRepositories(q: String, sort: String, order: String): Response
}