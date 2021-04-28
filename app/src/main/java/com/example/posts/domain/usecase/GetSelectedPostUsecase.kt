package com.example.posts.domain.usecase

import com.example.posts.core.domain.Result
import com.example.posts.core.domain.Usecase
import com.example.posts.domain.model.Post
import com.example.posts.domain.repository.IPostRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSelectedPostUsecase @Inject constructor(private val repository: IPostRepository) : Usecase<Unit, Result<Post?>>() {

    override fun run(input: Unit): Flow<Result<Post?>> = repository.getSelectedPost()
}