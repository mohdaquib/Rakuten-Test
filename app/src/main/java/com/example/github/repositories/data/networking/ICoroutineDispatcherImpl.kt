package com.example.github.repositories.data.networking

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

class CoroutineDispatcherImpl : ICoroutineDispatcher {
    override fun io(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    override fun default(): CoroutineDispatcher {
        return Dispatchers.Default
    }

    override fun main(): CoroutineDispatcher {
        return Dispatchers.Main
    }
}