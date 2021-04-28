package com.example.posts.data.repository

import com.example.posts.core.domain.Result
import com.example.posts.data.model.Status
import com.example.posts.data.model.UserDTO
import com.example.posts.data.source.IUserApi
import com.example.posts.domain.model.User
import com.example.posts.domain.repository.IUserRepository
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

internal class UserRepositoryTest {

    private val api: IUserApi = mockk()

    private val repository: IUserRepository = UserRepository(api)

    @BeforeEach
    fun setup() {
        every { api.getUser(any()) } returns emptyFlow()
    }

    @Test
    fun `when getUser is called, call api function`() {
        val userId = "2783"
        repository.getUser(userId)

        verify { api.getUser(userId) }
    }

    @Test
    fun `when api source function returns value with OK status, emit corresponding domain result`() {
        val userDTO = UserDTO(Status.OK, "Albert Einstein", "Shatta Wale")
        every { api.getUser(any()) } returns flow { emit(userDTO) }
        val expectedUser = User("Albert Einstein", "Shatta Wale")

        runBlocking {
            val result = repository.getUser("27").toList()

            assert(result[0] is Result.Loading)
            assertEquals(expectedUser, (result[1] as Result.Success).data)
        }
    }

    @Test
    fun `when api source function returns value with ERROR status, emit corresponding domain result`() {
        val userDTO = UserDTO(Status.ERROR)
        every { api.getUser(any()) } returns flow { emit(userDTO) }

        runBlocking {
            val result = repository.getUser("8972").toList()

            assert(result[0] is Result.Loading)
            assert(result[1] is Result.Error)
        }
    }
}