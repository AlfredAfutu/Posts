package com.example.posts.data.model

import com.example.posts.core.domain.Result
import com.example.posts.domain.model.Post
import com.example.posts.domain.model.Posts

data class PostsDTO(val status: Status, val posts: List<PostDTO> = emptyList())

data class PostDTO(val id: String? = null, val userId: String? = null, val title: String? = null, val body: String? = null)

fun PostsDTO.toDomainResult(): Result<Posts> {
    return when (status) {
        Status.OK -> Result.Success(posts.map { it.toDomainModel() })
        Status.ERROR -> Result.Error()
    }
}

fun PostDTO.toDomainModel(): Post = Post(id, userId, title.orEmpty(), body.orEmpty())
