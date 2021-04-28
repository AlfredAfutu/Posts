package com.example.posts.framework.datasource.api

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.coroutines.toFlow
import com.example.posts.UserQuery
import com.example.posts.data.model.Status
import com.example.posts.data.model.UserDTO
import com.example.posts.data.source.IUserApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserApi(private val client: ApolloClient) : IUserApi {

    override fun getUser(userId: String): Flow<UserDTO> =
        client.query(UserQuery(userId)).toFlow()
            .map { mapResponseToDTO(it) }

    private fun mapResponseToDTO(response: Response<UserQuery.Data>): UserDTO {
        return if (response.data == null || response.hasErrors()) {
            UserDTO(status = Status.ERROR)
        } else {
            val user = response.data!!.user
            UserDTO(status = Status.OK, name = user?.name, userName = user?.username)
        }
    }
}