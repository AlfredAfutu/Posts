package com.example.posts.presentation.postdetail

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.posts.MainCoroutineRule
import com.example.posts.core.domain.Result
import com.example.posts.domain.model.Post
import com.example.posts.domain.model.User
import com.example.posts.domain.usecase.GetSelectedPostUsecase
import com.example.posts.domain.usecase.GetUserUsecase
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
internal class PostDetailViewModelTest {
    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutine = MainCoroutineRule()

    private val getSelectedPostUsecase: GetSelectedPostUsecase = mockk()
    private val getUserUsecase: GetUserUsecase = mockk()

    private lateinit var viewModel: PostDetailViewModel

    @Before
    fun setup() {
        every { getSelectedPostUsecase(Unit) } returns emptyFlow()
        every { getUserUsecase(Unit) } returns emptyFlow()
    }

    @Test
    fun `when viewModel is initialized, invoke both usecases`() {
        viewModel = PostDetailViewModel(getSelectedPostUsecase, getUserUsecase)

        runBlocking {
            verify { getSelectedPostUsecase(Unit) }
            verify { getUserUsecase(Unit) }
        }
    }

    @Test
    fun `when GetSelectedPostUsecase emits Error, emit same in livedata`() {
        val expected = Result.Error()
        every { getSelectedPostUsecase(Unit) } returns flowOf(expected)

        runBlocking {
            viewModel = PostDetailViewModel(getSelectedPostUsecase, getUserUsecase)

            val value = viewModel.post.getOrAwaitValue()
            assertEquals(expected, value)
        }
    }

    @Test
    fun `when GetSelectedPostUsecase emits Loading, emit same in livedata`() {
        val expected = Result.Loading
        every { getSelectedPostUsecase(Unit) } returns flowOf(expected)

        runBlocking {
            viewModel = PostDetailViewModel(getSelectedPostUsecase, getUserUsecase)

            val value = viewModel.post.getOrAwaitValue()
            assertEquals(expected, value)
        }
    }

    @Test
    fun `when GetSelectedPostUsecase emits Success, emit same in livedata`() {
        val expected = Result.Success(Post(null, null, "title", "body"))
        every { getSelectedPostUsecase(Unit) } returns flowOf(expected)

        runBlocking {
            viewModel = PostDetailViewModel(getSelectedPostUsecase, getUserUsecase)

            val value = viewModel.post.getOrAwaitValue()
            assertEquals(expected, value)
        }
    }

    @Test
    fun `when GetUserUsecase emits Error, emit same in livedata`() {
        val expected = Result.Error()
        every { getUserUsecase(Unit) } returns flowOf(expected)

        runBlocking {
            viewModel = PostDetailViewModel(getSelectedPostUsecase, getUserUsecase)

            val value = viewModel.user.getOrAwaitValue()
            assertEquals(expected, value)
        }
    }

    @Test
    fun `when GetUserUsecase emits Loading, emit same in livedata`() {
        val expected = Result.Loading
        every { getUserUsecase(Unit) } returns flowOf(expected)

        runBlocking {
            viewModel = PostDetailViewModel(getSelectedPostUsecase, getUserUsecase)

            val value = viewModel.user.getOrAwaitValue()
            assertEquals(expected, value)
        }
    }

    @Test
    fun `when GetUserUsecase emits Success, emit same in livedata`() {
        val expected = Result.Success(User("Wiz", "Kidi"))
        every { getUserUsecase(Unit) } returns flowOf(expected)

        runBlocking {
            viewModel = PostDetailViewModel(getSelectedPostUsecase, getUserUsecase)

            val value = viewModel.user.getOrAwaitValue()
            assertEquals(expected, value)
        }
    }
}