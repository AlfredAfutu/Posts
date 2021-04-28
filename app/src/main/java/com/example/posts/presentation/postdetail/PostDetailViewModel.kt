package com.example.posts.presentation.postdetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.posts.core.domain.Result
import com.example.posts.domain.model.Post
import com.example.posts.domain.model.User
import com.example.posts.domain.usecase.GetSelectedPostUsecase
import com.example.posts.domain.usecase.GetUserUsecase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class PostDetailViewModel @Inject constructor(private val getSelectedPostUsecase: GetSelectedPostUsecase, private val getUserUsecase: GetUserUsecase) : ViewModel() {

    private var _post = MutableLiveData<Result<Post?>>()
    val post: LiveData<Result<Post?>> = _post

    private var _user = MutableLiveData<Result<User>>()
    val user: LiveData<Result<User>> = _user
    
    init {
        getPost()
        getUser()
    }

    private fun getPost() {
        getSelectedPostUsecase(Unit)
            .onEach { value -> _post.value = value }
            .launchIn(viewModelScope)
    }

    private fun getUser() {
        getUserUsecase(Unit)
            .onEach { value -> _user.value = value }
            .launchIn(viewModelScope)
    }
}