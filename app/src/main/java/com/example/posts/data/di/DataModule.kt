package com.example.posts.data.di

import com.example.posts.data.repository.PostRepository
import com.example.posts.data.repository.UserRepository
import com.example.posts.data.source.IPostApi
import com.example.posts.data.source.IUserApi
import com.example.posts.data.source.PostsCache
import com.example.posts.domain.repository.IPostRepository
import com.example.posts.domain.repository.IUserRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DataModule {

    @Provides
    @Singleton
    fun providePostRepository(api: IPostApi, cache: PostsCache): IPostRepository = PostRepository(api, cache)

    @Provides
    @Singleton
    fun provideUserRepository(api: IUserApi): IUserRepository = UserRepository(api)
}