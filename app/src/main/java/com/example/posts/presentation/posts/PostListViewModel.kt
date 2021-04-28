package com.example.posts.presentation.posts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.posts.core.domain.Result
import com.example.posts.core.presentation.SingleLiveEvent
import com.example.posts.domain.model.Post
import com.example.posts.domain.model.Posts
import com.example.posts.domain.usecase.GetAllPostsUsecase
import com.example.posts.domain.usecase.UpdateSelectedPostUsecase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class PostListViewModel @Inject constructor(private val getAllPostsUsecase: GetAllPostsUsecase, private val updateSelectedPostUsecase: UpdateSelectedPostUsecase) : ViewModel() {

    private var _posts = MutableLiveData<Result<Posts>>()
    val posts: LiveData<Result<Posts>> = _posts

    private var _isSelectedPostUpdated = SingleLiveEvent<Boolean>()
    val isSelectedPostUpdated: LiveData<Boolean> = _isSelectedPostUpdated

    init {
        getPosts()
    }

    private fun getPosts() {
        getAllPostsUsecase(Unit)
            .onEach { value -> _posts.value = value }
            .launchIn(viewModelScope)
    }

    fun updateSelectedPost(post: Post) {
        updateSelectedPostUsecase(post)
            .onEach { _isSelectedPostUpdated.value = true }
            .launchIn(viewModelScope)
    }
}