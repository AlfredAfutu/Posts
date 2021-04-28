package com.example.posts.framework.datasource.api

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.ApolloQueryCall
import com.apollographql.apollo.api.Error
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.coroutines.toFlow
import com.example.posts.UserQuery
import com.example.posts.data.model.Status
import com.example.posts.data.model.UserDTO
import com.example.posts.data.source.IUserApi
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

internal class UserApiTest {
    private val client: ApolloClient = mockk()

    private val call: ApolloQueryCall<UserQuery.Data> = mockk()

    private val api: IUserApi = UserApi(client)

    @BeforeEach
    fun setup() {
        every { client.query(any<UserQuery>()) } returns call
        mockkStatic("com.apollographql.apollo.coroutines.CoroutinesExtensionsKt")
        every { call.toFlow() } returns emptyFlow<Response<UserQuery.Data>>()
    }

    @Test
    fun `when getAllPosts function is called, perform query on api client`() {
        val userId = "2798"
        api.getUser(userId)

        verify { client.query(UserQuery(userId)) }
    }

    @Test
    fun `when response data is null, emit a dto with ERROR status`() {
        val userId = "2972"
        val response = Response.builder<UserQuery.Data>(UserQuery(userId)).apply { data(null) }.build()
        every { call.toFlow() } returns flowOf(response)

        runBlocking {
            val dto = api.getUser(userId).first()

            assertEquals(Status.ERROR, dto.status)
            assertEquals(null, dto.name)
            assertEquals(null, dto.userName)
        }
    }

    @Test
    fun `when response has errors, emit a dto with ERROR status`() {
        val userId = "430"
        val response = Response.builder<UserQuery.Data>(UserQuery(userId)).apply { errors(listOf(Error("Internal server error"))) }.build()
        every { call.toFlow() } returns flowOf(response)

        runBlocking {
            val dto = api.getUser(userId).first()

            assertEquals(Status.ERROR, dto.status)
            assertEquals(null, dto.name)
            assertEquals(null, dto.userName)
        }
    }

    @Test
    fun `when response has no errors and data is not null, emit a dto with OK status`() {
        val data: UserQuery.Data = mockk()
        val user: UserQuery.User = UserQuery.User(name = "Burna", username = "Cupid")
        every { data.user } returns user
        val userId = "293"
        val expectedUser = UserDTO(Status.OK, "Burna", "Cupid")

        val response = Response.builder<UserQuery.Data>(UserQuery(userId)).apply { data(data) }.build()
        every { call.toFlow() } returns flowOf(response)

        runBlocking {
            val dto = api.getUser(userId).first()

            assertEquals(expectedUser, dto)
        }
    }
}