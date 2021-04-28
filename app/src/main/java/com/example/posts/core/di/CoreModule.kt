package com.example.posts.core.di

import com.example.posts.core.presentation.ViewModelModule
import dagger.Module

@Module(includes = [ViewModelModule::class])
object CoreModule