package com.example.posts.data.source

import com.example.posts.data.model.PostsDTO
import kotlinx.coroutines.flow.Flow

interface IPostApi {
    fun getAllPosts(): Flow<PostsDTO>
}