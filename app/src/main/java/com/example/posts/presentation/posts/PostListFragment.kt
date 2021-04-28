package com.example.posts.presentation.posts

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.posts.core.domain.Result
import com.example.posts.core.presentation.ViewModelFactory
import com.example.posts.databinding.FragmentPostListBinding
import com.example.posts.domain.model.Posts
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

/**
 * A fragment representing a list of Posts.
 */
class PostListFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var adapter: PostListAdapter

    private lateinit var viewModel: PostListViewModel

    private var _binding: FragmentPostListBinding? = null
    private val binding get() = _binding!!

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPostListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PostListViewModel::class.java)
        setupAdapter()
        observePosts()
        observeSelectedPostUpdated()
    }

    private fun setupAdapter() {
        binding.list.adapter = adapter
        adapter.setClickListener { post -> viewModel.updateSelectedPost(post) }
    }

    private fun observePosts() {
        viewModel.posts.observe(viewLifecycleOwner, Observer {
            handleResult(it)
        })
    }

    private fun handleResult(result: Result<Posts>) {
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
        binding.list.visibility = View.VISIBLE
        adapter.setData(emptyList())
    }

    private fun handleSuccess(data: Posts) {
        hideLoading()
        binding.list.visibility = View.VISIBLE
        adapter.setData(data)
    }

    private fun hideLoading() {
        with(binding) {
            shimmerContainer.stopShimmer()
            shimmerContainer.visibility = View.GONE
        }
    }

    private fun observeSelectedPostUpdated() {
        viewModel.isSelectedPostUpdated.observe(viewLifecycleOwner, Observer {
            val action = PostListFragmentDirections.actionPostListFragmentToPostDetailFragment()
            findNavController().navigate(action)
        })
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