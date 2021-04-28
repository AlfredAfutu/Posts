package com.example.posts.presentation.di

import androidx.lifecycle.ViewModel
import com.example.posts.core.presentation.ViewModelKey
import com.example.posts.presentation.postdetail.PostDetailFragment
import com.example.posts.presentation.postdetail.PostDetailViewModel
import com.example.posts.presentation.posts.PostListAdapter
import com.example.posts.presentation.posts.PostListFragment
import com.example.posts.presentation.posts.PostListViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.multibindings.IntoMap

@Module
abstract class PostListFragmentViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(PostListViewModel::class)
    abstract fun bindPostListViewModel(viewModel: PostListViewModel): ViewModel
}

@Module
object PostListFragmentModule {
    @Provides
    fun provideListAdapter(): PostListAdapter = PostListAdapter()
}

@Module
abstract class PostDetailFragmentViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(PostDetailViewModel::class)
    abstract fun bindPostDetailViewModel(viewModel: PostDetailViewModel): ViewModel
}

@Module
interface PostDetailFragmentBuildersModule {
    @ContributesAndroidInjector(modules = [PostDetailFragmentViewModelModule::class])
    fun contributePostDetailActivity(): PostDetailFragment
}

@Module
interface PostListFragmentBuildersModule {
    @ContributesAndroidInjector(modules = [PostListFragmentModule::class, PostListFragmentViewModelModule::class])
    fun contributePostListActivity(): PostListFragment
}