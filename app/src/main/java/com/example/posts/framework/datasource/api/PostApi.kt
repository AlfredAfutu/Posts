package com.example.posts.framework.datasource.api

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.coroutines.toFlow
import com.example.posts.PostsQuery
import com.example.posts.data.model.PostDTO
import com.example.posts.data.model.PostsDTO
import com.example.posts.data.model.Status
import com.example.posts.data.source.IPostApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PostApi(private val client: ApolloClient) : IPostApi {

    override fun getAllPosts(): Flow<PostsDTO> =
        client.query(PostsQuery()).toFlow()
            .map { mapResponseToDTO(it) }

    private fun mapResponseToDTO(response: Response<PostsQuery.Data>): PostsDTO {
        return if (response.data == null || response.hasErrors()) {
            PostsDTO(status = Status.ERROR)
        } else {
            val posts = response.data!!.posts?.data?.map { PostDTO(it?.id, it?.user?.id, it?.title, it?.body) }.orEmpty()
            PostsDTO(status = Status.OK, posts = posts)
        }
    }
}