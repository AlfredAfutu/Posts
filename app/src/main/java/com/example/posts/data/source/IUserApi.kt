package com.example.posts.data.source

import com.example.posts.data.model.UserDTO
import kotlinx.coroutines.flow.Flow

interface IUserApi {
    fun getUser(userId: String): Flow<UserDTO>
}