package com.example.posts.data.repository

import com.example.posts.core.domain.Result
import com.example.posts.data.model.PostsDTO
import com.example.posts.data.model.Status
import com.example.posts.data.model.toDomainModel
import com.example.posts.data.model.toDomainResult
import com.example.posts.data.source.IPostApi
import com.example.posts.data.source.PostsCache
import com.example.posts.domain.model.Post
import com.example.posts.domain.model.Posts
import com.example.posts.domain.repository.IPostRepository
import kotlinx.coroutines.flow.*

class PostRepository(private val api: IPostApi, private val cache: PostsCache) : IPostRepository {

    override fun getAllPosts(): Flow<Result<Posts>> =
        api.getAllPosts()
            .onEach { cache.data = it }
            .map { cache.data!! }
            .map { it.toDomainResult() }
            .onStart { emit(Result.Loading) }
            .catch { exception -> emit(Result.Error(exception)) }

    override fun getSelectedPost(): Flow<Result<Post?>> =
        flowOf(cache.data!!)
            .map { mapCacheToPost(it, cache.selectedPostId) }
            .onStart { emit(Result.Loading) }
            .catch { exception -> emit(Result.Error(exception)) }

    private fun mapCacheToPost(cache: PostsDTO, postId: String?): Result<Post?> {
        return when (cache.status) {
            Status.OK -> Result.Success(cache.posts.find { it.id == postId }?.toDomainModel())
            Status.ERROR -> Result.Error()
        }
    }

    override suspend fun setSelectedPost(postId: String?, userId: String?) {
        cache.selectedPostId = postId
        cache.selectedPostUserId = userId
    }

    override suspend fun getSelectedPostUserId(): String? = cache.selectedPostUserId
}