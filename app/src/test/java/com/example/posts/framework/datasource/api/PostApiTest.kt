package com.example.posts.framework.datasource.api

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.ApolloQueryCall
import com.apollographql.apollo.api.Error
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.coroutines.toFlow
import com.example.posts.PostsQuery
import com.example.posts.data.model.PostDTO
import com.example.posts.data.model.Status
import com.example.posts.data.source.IPostApi
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class PostApiTest {
    private val client: ApolloClient = mockk()

    private val call: ApolloQueryCall<PostsQuery.Data> = mockk()

    private val api: IPostApi = PostApi(client)

    @BeforeEach
    fun setup() {
        every { client.query(any<PostsQuery>()) } returns call
        mockkStatic("com.apollographql.apollo.coroutines.CoroutinesExtensionsKt")
        every { call.toFlow() } returns emptyFlow<Response<PostsQuery.Data>>()
    }

    @Test
    fun `when getAllPosts function is called, perform query on api client`() {
        api.getAllPosts()

        verify { client.query(any<PostsQuery>()) }
    }

    @Test
    fun `when response data is null, emit a dto with ERROR status`() {
        val response = Response.builder<PostsQuery.Data>(PostsQuery()).apply { data(null) }.build()
        every { call.toFlow() } returns flowOf(response)

        runBlocking {
            val dto = api.getAllPosts().first()

            assertEquals(Status.ERROR, dto.status)
            assertEquals(emptyList<PostDTO>(), dto.posts)
        }
    }

    @Test
    fun `when response has errors, emit a dto with ERROR status`() {
        val response = Response.builder<PostsQuery.Data>(PostsQuery()).apply { errors(listOf(Error("Internal server error"))) }.build()
        every { call.toFlow() } returns flowOf(response)

        runBlocking {
            val dto = api.getAllPosts().first()

            assertEquals(Status.ERROR, dto.status)
            assertEquals(emptyList<PostDTO>(), dto.posts)
        }
    }

    @Test
    fun `when response has no errors and data is not null, emit a dto with OK status`() {
        val data: PostsQuery.Data = mockk()
        val posts: PostsQuery.Posts = mockk()
        val postData: List<PostsQuery.Data1> = listOf(PostsQuery.Data1(id = "289", title = "headline", body = "body", user = PostsQuery.User(id = "209")))
        every { data.posts } returns posts
        every { posts.data } returns postData
        val expectedPosts = listOf(PostDTO(id = "289", userId = "209", title = "headline", body = "body"))

        val response = Response.builder<PostsQuery.Data>(PostsQuery()).apply { data(data) }.build()
        every { call.toFlow() } returns flowOf(response)

        runBlocking {
            val dto = api.getAllPosts().first()

            assertEquals(Status.OK, dto.status)
            assert(dto.posts.isNotEmpty())
            assertEquals(expectedPosts, dto.posts)
        }
    }
}