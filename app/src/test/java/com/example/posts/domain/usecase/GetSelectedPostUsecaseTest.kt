package com.example.posts.domain.usecase

import com.example.posts.core.domain.Result
import com.example.posts.domain.model.Post
import com.example.posts.domain.repository.IPostRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class GetSelectedPostUsecaseTest {
    private val repository: IPostRepository = mockk()

    private val usecase: GetSelectedPostUsecase = GetSelectedPostUsecase(repository)

    @BeforeEach
    fun setup() {
        every { repository.getSelectedPost() } returns emptyFlow()
    }

    @Test
    fun `when usecase is invoked, call repository function`() {
        usecase(Unit)

        verify { repository.getSelectedPost() }
    }

    @Test
    fun `when repository returns Loading Result, emit same Result`() {
        val expected = Result.Loading
        every { repository.getSelectedPost() } returns flowOf(expected)

        runBlocking {
            val actual = usecase(Unit).first()

            Assertions.assertEquals(expected, actual)
        }
    }

    @Test
    fun `when repository returns Error Result, emit same Result`() {
        val expected = Result.Error()
        every { repository.getSelectedPost() } returns flowOf(expected)

        runBlocking {
            val actual = usecase(Unit).first()

            Assertions.assertEquals(expected, actual)
        }
    }

    @Test
    fun `when repository returns Success Result, emit same Result`() {
        val expected = Result.Success(Post(id = "98374", userId = "3678", title = "davido", body = "baddest"))
        every { repository.getSelectedPost() } returns flowOf(expected)

        runBlocking {
            val actual = usecase(Unit).first()

            Assertions.assertEquals(expected, actual)
        }
    }
}