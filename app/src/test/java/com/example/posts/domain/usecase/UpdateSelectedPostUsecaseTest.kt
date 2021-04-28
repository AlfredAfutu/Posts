package com.example.posts.domain.usecase

import com.example.posts.core.domain.Usecase
import com.example.posts.domain.model.Post
import com.example.posts.domain.repository.IPostRepository
import io.mockk.coVerify
import io.mockk.spyk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test

internal class UpdateSelectedPostUsecaseTest {
    private val repository: IPostRepository = spyk()

    private val usecase: Usecase<Post, Unit> = UpdateSelectedPostUsecase(repository)

    @Test
    fun `when usecase is invoked, set selected post in repository`() {
        val input = Post("234", "123", "title", "body")

        runBlocking {
            usecase(input).first()

            coVerify { repository.setSelectedPost(input.id, input.userId) }
        }
    }
}