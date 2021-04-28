package com.example.posts.domain.model

typealias Posts = List<Post>

data class Post(val id: String?, val userId: String?, val title: String, val body: String)

data class User(val name: String, val userName: String)