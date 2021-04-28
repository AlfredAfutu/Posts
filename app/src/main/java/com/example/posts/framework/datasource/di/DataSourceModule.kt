package com.example.posts.framework.datasource.di

import com.apollographql.apollo.ApolloClient
import com.example.posts.data.source.IPostApi
import com.example.posts.data.source.IUserApi
import com.example.posts.framework.datasource.api.PostApi
import com.example.posts.framework.datasource.api.UserApi
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DataSourceModule {

    @Provides
    fun providePostApi(graphQlClient: ApolloClient): IPostApi = PostApi(graphQlClient)

    @Provides
    fun provideUserApi(graphQlClient: ApolloClient): IUserApi = UserApi(graphQlClient)

    @Provides
    @Singleton
    fun provideGraphQlClient(): ApolloClient =
        ApolloClient.builder()
            .serverUrl("https://graphqlzero.almansi.me/api")
            .build()
}