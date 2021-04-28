package com.example.posts.domain.usecase

import com.example.posts.core.domain.Result
import com.example.posts.core.domain.Usecase
import com.example.posts.domain.model.Post
import com.example.posts.domain.model.Posts
import com.example.posts.domain.repository.IPostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import kotlin.math.min

private const val BODY_CHARACTERS = 120

class GetAllPostsUsecase @Inject constructor(private val repository: IPostRepository) : Usecase<Unit, Result<Posts>>() {

    override fun run(input: Unit): Flow<Result<Posts>> =
        repository.getAllPosts()
            .map { reduceBodyTo120Characters(it) }

    private fun reduceBodyTo120Characters(result: Result<Posts>): Result<Posts> {
        return if (result is Result.Success) {
            result.copy(data = getPostsWith120CharactersOfBody(result.data))
        } else {
            result
        }
    }

    private fun getPostsWith120CharactersOfBody(posts: Posts): List<Post> =
        posts.map { post ->
            post.copy(id = post.id, userId = post.userId, title = post.title, body = post.body.substring(0, min(post.body.length, BODY_CHARACTERS)))
        }
}