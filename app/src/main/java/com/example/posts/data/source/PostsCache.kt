package com.example.posts.data.source

import com.example.posts.data.model.PostsDTO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostsCache @Inject constructor() {
    var data: PostsDTO? = null
    var selectedPostId: String? = null
    var selectedPostUserId: String? = null
}