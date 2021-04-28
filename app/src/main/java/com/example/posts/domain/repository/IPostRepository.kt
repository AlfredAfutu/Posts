package com.example.posts.domain.repository

import com.example.posts.core.domain.Result
import com.example.posts.domain.model.Post
import com.example.posts.domain.model.Posts
import kotlinx.coroutines.flow.Flow

interface IPostRepository {
    fun getAllPosts(): Flow<Result<Posts>>
    fun getSelectedPost(): Flow<Result<Post?>>
    suspend fun getSelectedPostUserId(): String?
    suspend fun setSelectedPost(postId: String?, userId: String?)
}