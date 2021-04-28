package com.example.posts.di

import com.example.posts.presentation.di.PostDetailFragmentBuildersModule
import com.example.posts.presentation.di.PostListFragmentBuildersModule
import dagger.Module

@Module(includes = [PostListFragmentBuildersModule::class, PostDetailFragmentBuildersModule::class])
object FragmentBuildersModule