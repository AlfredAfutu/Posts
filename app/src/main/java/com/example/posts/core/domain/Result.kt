package com.example.posts.core.domain

sealed class Result<out R> {
    data class Success<out T>(val data: T) : Result<T>()
    data class Error(val exception: Throwable? = null) : Result<Nothing>()
    object Loading: Result<Nothing>()
}