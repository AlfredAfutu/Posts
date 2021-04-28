package com.example.posts.core.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn

abstract class Usecase<I, O>(private val dispatcher: CoroutineDispatcher = Dispatchers.IO) {
    protected abstract fun run(input: I): Flow<O>

    operator fun invoke(input: I): Flow<O> {
        return run(input)
            .flowOn(dispatcher)
    }
}