package com.example.posts.data.repository

import com.example.posts.core.domain.Result
import com.example.posts.data.model.toDomainResult
import com.example.posts.data.source.IUserApi
import com.example.posts.domain.model.User
import com.example.posts.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

class UserRepository(private val api: IUserApi) : IUserRepository {

    override fun getUser(userId: String): Flow<Result<User>> =
        api.getUser(userId)
            .map { it.toDomainResult() }
            .onStart { emit(Result.Loading) }
            .catch { exception -> emit(Result.Error(exception)) }
}