package com.example.posts.presentation.postdetail

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.posts.core.domain.Result
import com.example.posts.core.presentation.ViewModelFactory
import com.example.posts.databinding.FragmentPostDetailBinding
import com.example.posts.domain.model.Post
import com.example.posts.domain.model.User
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

/**
 * A fragment representing a Post detail.
 */
class PostDetailFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private lateinit var viewModel: PostDetailViewModel

    private var _binding: FragmentPostDetailBinding? = null
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentPostDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PostDetailViewModel::class.java)
        observeSelectedPost()
        observeUser()
    }

    private fun observeSelectedPost() {
        viewModel.post.observe(viewLifecycleOwner, Observer {
            handleSelectedPostResult(it)
        })
    }

    private fun handleSelectedPostResult(result: Result<Post?>) {
        when (result) {
            is Result.Loading -> setPostTitleAndBody("", "")
            is Result.Error -> setPostTitleAndBody("", "")
            is Result.Success -> setPostTitleAndBody(result.data?.title.orEmpty(), result.data?.body.orEmpty())
        }
    }

    private fun setPostTitleAndBody(title: String, body: String) {
        binding.title.text = title
        binding.body.text = body
    }

    private fun observeUser() {
        viewModel.user.observe(viewLifecycleOwner, Observer {
            handleUserResult(it)
        })
    }

    private fun handleUserResult(result: Result<User>) {
        when (result) {
            is Result.Loading -> showLoading()
            is Result.Error -> handleError()
            is Result.Success -> handleSuccess(result.data)
        }
    }

    private fun showLoading() {
        with(binding) {
            shimmerContainer.startShimmer()
            shimmerContainer.visibility = View.VISIBLE
        }
    }

    private fun handleError() {
        hideLoading()
        with(binding) {
            name.visibility = View.VISIBLE
            userName.visibility = View.VISIBLE
        }
    }

    private fun handleSuccess(data: User) {
        hideLoading()
        with(binding) {
            name.visibility = View.VISIBLE
            userName.visibility = View.VISIBLE
        }
        setUserDetail(data.name, data.userName)
    }

    private fun hideLoading() {
        with(binding) {
            shimmerContainer.stopShimmer()
            shimmerContainer.visibility = View.GONE
        }
    }

    private fun setUserDetail(name: String, userName: String) {
        with(binding) {
            this.name.text = name
            this.userName.text = userName
        }
    }

    override fun onStop() {
        super.onStop()
        binding.shimmerContainer.stopShimmer()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}