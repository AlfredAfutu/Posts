package com.example.posts.data.repository

import com.example.posts.core.domain.Result
import com.example.posts.data.model.PostDTO
import com.example.posts.data.model.PostsDTO
import com.example.posts.data.model.Status
import com.example.posts.data.model.toDomainModel
import com.example.posts.data.source.IPostApi
import com.example.posts.data.source.PostsCache
import com.example.posts.domain.model.Post
import com.example.posts.domain.repository.IPostRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class PostRepositoryTest {
    private val api: IPostApi = mockk()
    private val cache: PostsCache = PostsCache()

    private lateinit var repository: IPostRepository

    @BeforeEach
    fun setup() {
        repository = PostRepository(api, cache)
        every { api.getAllPosts() } returns emptyFlow()
    }

    @Test
    fun `when getAllPosts is called, call api function`() {
        repository.getAllPosts()

        verify { api.getAllPosts() }
    }

    @Test
    fun `when api source function returns value with OK status, emit corresponding domain result`() {
        val postsDTO = PostsDTO(
            Status.OK,
            listOf(PostDTO("34", "12", "he say she say", "the end"), PostDTO("01", "2093", "day and night", "the end"))
        )
        every { api.getAllPosts() } returns flow { emit(postsDTO) }

        runBlocking {
            val result = repository.getAllPosts().toList()

            assert(result[0] is Result.Loading)
            assertEquals(postsDTO.posts.map { it.toDomainModel() }, (result[1] as Result.Success).data)
        }
    }

    @Test
    fun `when api source function returns value with ERROR status, emit corresponding domain result`() {
        val postsDTO = PostsDTO(Status.ERROR)
        every { api.getAllPosts() } returns flow { emit(postsDTO) }

        runBlocking {
            val result = repository.getAllPosts().toList()

            assert(result[0] is Result.Loading)
            assert(result[1] is Result.Error)
        }
    }

    @Test
    fun `when getAllPosts chain throws an exception, emit corresponding domain result`() {
        repository = PostRepository(api, mockk())
        val postsDTO = PostsDTO(Status.OK, emptyList())
        every { api.getAllPosts() } returns flow { emit(postsDTO) }


        runBlocking {
            val result = repository.getAllPosts().toList()

            assert(result[0] is Result.Loading)
            assert(result[1] is Result.Error)
        }
    }

    @Test
    fun `when selected post is called with argument passed, set postId and userId in cache`() {
        val postId = "4234"
        val userId = "2834"

        runBlocking {
            repository.setSelectedPost(postId, userId)

            assertEquals(postId, cache.selectedPostId)
            assertEquals(userId, cache.selectedPostUserId)
        }
    }

    @Test
    fun `when selected post user id is requested, return cache post user id`() {
        cache.selectedPostUserId = "23414"

        runBlocking {
            val userId = repository.getSelectedPostUserId()

            assertEquals(cache.selectedPostUserId, userId)
        }
    }

    @Test
    fun `when selected post is requested but has not been set, emit success result with null post`() {
        cache.data = PostsDTO(
            Status.OK,
            listOf(PostDTO("34", "12", "he say she say", "the end"), PostDTO("01", "2093", "day and night", "the end"))
        )

        runBlocking {
            val result = repository.getSelectedPost().toList()

            assert(result[0] is Result.Loading)
            assert(result[1] is Result.Success)
            assertEquals(null, (result[1] as Result.Success).data)
        }
    }

    @Test
    fun `when selected post is requested but cache contains value with ERROR status, emit an error result`() {
        cache.data = PostsDTO(Status.ERROR)

        runBlocking {
            val result = repository.getSelectedPost().toList()

            assert(result[0] is Result.Loading)
            assert(result[1] is Result.Error)
        }
    }

    @Test
    fun `when selected post is requested but cache selected postId does not exist in the data, emit a success result with null post`() {
        cache.data = PostsDTO(
            Status.OK,
            listOf(PostDTO("34", "12", "he say she say", "the end"), PostDTO("01", "2093", "day and night", "the end"))
        )
        cache.selectedPostId = "2493"

        runBlocking {
            val result = repository.getSelectedPost().toList()

            assert(result[0] is Result.Loading)
            assert(result[1] is Result.Success)
            assertEquals(null, (result[1] as Result.Success).data)
        }
    }

    @Test
    fun `when selected post is requested and cache selected postId exists in the data, emit a success result with correct post`() {
        cache.data = PostsDTO(
            Status.OK,
            listOf(PostDTO("34", "12", "he say she say", "the end"), PostDTO("01", "2093", "day and night", "the end"))
        )
        cache.selectedPostId = "01"
        val expectedPost = Post("01", "2093", "day and night", "the end")

        runBlocking {
            val result = repository.getSelectedPost().toList()

            assert(result[0] is Result.Loading)
            assert(result[1] is Result.Success)
            assertEquals(expectedPost, (result[1] as Result.Success).data)
        }
    }
}