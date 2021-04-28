package com.example.posts.data.model

import com.example.posts.core.domain.Result
import com.example.posts.domain.model.User

data class UserDTO(val status: Status, val name: String? = null, val userName: String? = null)

fun UserDTO.toDomainResult(): Result<User> {
    return when (status) {
        Status.OK -> Result.Success(User(name.orEmpty(), userName.orEmpty()))
        Status.ERROR -> Result.Error()
    }
}