package com.example.posts.domain.usecase

import com.example.posts.core.domain.Result
import com.example.posts.domain.model.User
import com.example.posts.domain.repository.IPostRepository
import com.example.posts.domain.repository.IUserRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class GetUserUsecaseTest {
    private val postRepository: IPostRepository = mockk()
    private val userRepository: IUserRepository = mockk()

    private val usecase: GetUserUsecase = GetUserUsecase(postRepository, userRepository)

    @BeforeEach
    fun setup() {
        every { userRepository.getUser(any()) } returns emptyFlow()
    }

    @Test
    fun `when user repository returns a Loading result, emit same Result`() {
        val userId = "2984"
        val expectedResult = Result.Loading
        coEvery { postRepository.getSelectedPostUserId() } returns userId
        every { userRepository.getUser(any()) } returns flowOf(expectedResult)

        runBlocking {
            val actual = usecase(Unit).first()

            assertEquals(expectedResult, actual)
        }
    }

    @Test
    fun `when user repository returns an Error result, emit same Result`() {
        val userId = "2984"
        val expectedResult = Result.Error()
        coEvery { postRepository.getSelectedPostUserId() } returns userId
        every { userRepository.getUser(any()) } returns flowOf(expectedResult)

        runBlocking {
            val actual = usecase(Unit).first()

            assertEquals(expectedResult, actual)
        }
    }

    @Test
    fun `when user repository returns a Success result, emit same Result`() {
        val userId = "2984"
        val expectedResult = Result.Success(User("Adekunle", "Gold"))
        coEvery { postRepository.getSelectedPostUserId() } returns userId
        every { userRepository.getUser(any()) } returns flowOf(expectedResult)

        runBlocking {
            val actual = usecase(Unit).first()

            assertEquals(expectedResult, actual)
        }
    }
}