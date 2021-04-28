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
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class GetAllPostsUsecaseTest {
    private val repository: IPostRepository = mockk()

    private val usecase: GetAllPostsUsecase = GetAllPostsUsecase(repository)

    @BeforeEach
    fun setup() {
        every { repository.getAllPosts() } returns emptyFlow()
    }

    @Test
    fun `when usecase is invoked, call repository function`() {
        usecase(Unit)

        verify { repository.getAllPosts() }
    }

    @Test
    fun `when repository returns Loading Result, emit same Result`() {
        val expected = Result.Loading
        every { repository.getAllPosts() } returns flowOf(expected)

        runBlocking {
            val actual = usecase(Unit).first()

            assertEquals(expected, actual)
        }
    }

    @Test
    fun `when repository returns Error Result, emit same Result`() {
        val expected = Result.Error()
        every { repository.getAllPosts() } returns flowOf(expected)

        runBlocking {
            val actual = usecase(Unit).first()

            assertEquals(expected, actual)
        }
    }

    @Test
    fun `when repository returns Success Result with body less than 120 characters, emit same Result`() {
        val expected = Result.Success(
            listOf(
                Post(id = "34", userId = "232", title = "what do you say", body = "as far i can oh oh i told you so"),
                Post(id = "09", userId = "12", title = "every morning", body = "it is a blessing")
            )
        )
        every { repository.getAllPosts() } returns flowOf(expected)

        runBlocking {
            val actual = usecase(Unit).first()

            assertEquals(expected, actual)
        }
    }

    @Test
    fun `when repository returns Success Result with body equal to 120 characters, emit same Result`() {
        val expected = Result.Success(
            listOf(
                Post(
                    id = "34", userId = "232", title = "what do you say", body = "as far i can oh oh i told you so muhiskjrgnosdjnf iehoieejkhe kjnsdf oisfjnos uihskgf " +
                            "sdkuheiuhasd sofginl oisjlfg li ou"
                ),
                Post(
                    id = "09", userId = "12", title = "every morning", body = "as far i can oh oh i told you so muhiskjrgnosdjnf iehoieejkhe kjnsdf oisfjnos uihskgf " +
                            "sdkuheiuhasd sofginl oisjlfg li ou"
                )
            )
        )
        every { repository.getAllPosts() } returns flowOf(expected)

        runBlocking {
            val actual = usecase(Unit).first()

            assertEquals(expected, actual)
        }
    }

    @Test
    fun `when repository returns Success Result with body more than 120 characters, emit Result with only the first 120 characters of the body`() {
        val result = Result.Success(
            listOf(
                Post(
                    id = "34", userId = "232", title = "what do you say", body = "as far i can oh oh i told you so muhiskjrgnosdjnf iehoieejkhe kjnsdf oisfjnos uihskgf " +
                            "sdkuheiuhasd sofginl oisjlfg li ouneiurgn jklsnd"
                ),
                Post(
                    id = "09", userId = "12", title = "every morning", body = "as far i can oh oh i told you so muhiskjrgnosdjnf iehoieejkhe kjnsdf oisfjnos uihskgf " +
                            "sdkuheiuhasd sofginl oisjlfg li oujnias"
                )
            )
        )
        val expected = Result.Success(
            listOf(
                Post(
                    id = "34", userId = "232", title = "what do you say", body = "as far i can oh oh i told you so muhiskjrgnosdjnf iehoieejkhe kjnsdf oisfjnos uihskgf " +
                            "sdkuheiuhasd sofginl oisjlfg li ou"
                ),
                Post(
                    id = "09", userId = "12", title = "every morning", body = "as far i can oh oh i told you so muhiskjrgnosdjnf iehoieejkhe kjnsdf oisfjnos uihskgf " +
                            "sdkuheiuhasd sofginl oisjlfg li ou"
                )
            )
        )
        every { repository.getAllPosts() } returns flowOf(result)

        runBlocking {
            val actual = usecase(Unit).first()

            assertEquals(expected, actual)
        }
    }
}