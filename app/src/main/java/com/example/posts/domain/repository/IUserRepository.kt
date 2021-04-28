package com.example.posts.domain.repository

import com.example.posts.core.domain.Result
import com.example.posts.domain.model.User
import kotlinx.coroutines.flow.Flow

interface IUserRepository {
    fun getUser(userId: String): Flow<Result<User>>
}