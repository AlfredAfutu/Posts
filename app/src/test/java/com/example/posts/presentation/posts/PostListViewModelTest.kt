package com.example.posts.presentation.posts

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.posts.MainCoroutineRule
import com.example.posts.core.domain.Result
import com.example.posts.domain.model.Post
import com.example.posts.domain.usecase.GetAllPostsUsecase
import com.example.posts.domain.usecase.UpdateSelectedPostUsecase
import com.example.posts.getOrAwaitValue
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
internal class PostListViewModelTest {
    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutine = MainCoroutineRule()

    private val getAllPostsUsecase: GetAllPostsUsecase = mockk()
    private val updateSelectedPostUsecase: UpdateSelectedPostUsecase = mockk()

    private lateinit var viewModel: PostListViewModel

    @Before
    fun setup() {
        every { getAllPostsUsecase(Unit) } returns emptyFlow()
        every { updateSelectedPostUsecase(any()) } returns emptyFlow()
    }

    @Test
    fun `when viewModel is initialized, invoke getAllPostsUsecase`() {
        viewModel = PostListViewModel(getAllPostsUsecase, updateSelectedPostUsecase)

        runBlocking {
            verify { getAllPostsUsecase(Unit) }
        }
    }

    @Test
    fun `when GetAllPostsUsecase emits Error, emit same in livedata`() {
        val expected = Result.Error()
        every { getAllPostsUsecase(Unit) } returns flowOf(expected)

        runBlocking {
            viewModel = PostListViewModel(getAllPostsUsecase, updateSelectedPostUsecase)

            val value = viewModel.posts.getOrAwaitValue()
            assertEquals(expected, value)
        }
    }

    @Test
    fun `when GetAllPostsUsecase emits Loading, emit same in livedata`() {
        val expected = Result.Loading
        every { getAllPostsUsecase(Unit) } returns flowOf(expected)

        runBlocking {
            viewModel = PostListViewModel(getAllPostsUsecase, updateSelectedPostUsecase)

            val value = viewModel.posts.getOrAwaitValue()
            assertEquals(expected, value)
        }
    }

    @Test
    fun `when GetAllPostsUsecase emits Success, emit same in livedata`() {
        val expected = Result.Success(listOf(Post(null, null, "title", "body")))
        every { getAllPostsUsecase(Unit) } returns flowOf(expected)

        runBlocking {
            viewModel = PostListViewModel(getAllPostsUsecase, updateSelectedPostUsecase)

            val value = viewModel.posts.getOrAwaitValue()
            assertEquals(expected, value)
        }
    }

    @Test
    fun `when post is selected, update selected post`() {
        val post = Post("383", "345", "title", "body")

        runBlocking {
            viewModel = PostListViewModel(getAllPostsUsecase, updateSelectedPostUsecase)
            viewModel.updateSelectedPost(post)

            verify { updateSelectedPostUsecase(post) }
        }
    }

    @Test
    fun `when selected post is update, emit event from livedata`() {
        val post = Post("383", "345", "title", "body")
        every { updateSelectedPostUsecase(any()) } returns flowOf(Unit)

        runBlocking {
            viewModel = PostListViewModel(getAllPostsUsecase, updateSelectedPostUsecase)
            viewModel.updateSelectedPost(post)

            val value = viewModel.isSelectedPostUpdated.getOrAwaitValue()
            assertEquals(true, value)
        }
    }
}