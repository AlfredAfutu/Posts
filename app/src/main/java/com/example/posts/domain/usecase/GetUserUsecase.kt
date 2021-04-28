package com.example.posts.domain.usecase

import com.example.posts.core.domain.Result
import com.example.posts.core.domain.Usecase
import com.example.posts.domain.model.User
import com.example.posts.domain.repository.IPostRepository
import com.example.posts.domain.repository.IUserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetUserUsecase @Inject constructor(private val postRepository: IPostRepository, private val userRepository: IUserRepository) : Usecase<Unit, Result<User>>() {

    override fun run(input: Unit): Flow<Result<User>> =
        flow { emit(postRepository.getSelectedPostUserId()) }
            .flatMapLatest { userId -> userRepository.getUser(userId.orEmpty()) }
}