package com.example.posts.domain.usecase

import com.example.posts.core.domain.Usecase
import com.example.posts.domain.model.Post
import com.example.posts.domain.repository.IPostRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateSelectedPostUsecase @Inject constructor(private val repository: IPostRepository): Usecase<Post, Unit>() {

    override fun run(input: Post): Flow<Unit> = flow { emit(repository.setSelectedPost(input.id, input.userId)) }
}