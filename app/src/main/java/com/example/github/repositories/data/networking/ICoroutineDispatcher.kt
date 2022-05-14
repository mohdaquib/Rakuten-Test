package com.example.github.repositories.data.networking

import kotlinx.coroutines.CoroutineDispatcher

interface ICoroutineDispatcher {
    fun io(): CoroutineDispatcher
    fun default(): CoroutineDispatcher
    fun main(): CoroutineDispatcher
}