package com.example.posts.presentation.posts

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.posts.R
import com.example.posts.databinding.FragmentPostListItemBinding
import com.example.posts.domain.model.Post
import com.example.posts.domain.model.Posts

class PostListAdapter : RecyclerView.Adapter<PostListAdapter.ViewHolder>() {

    private val posts: MutableList<Post> = mutableListOf()

    private lateinit var clickListener: (Post) -> Unit

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.fragment_post_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val post = posts[position]
        holder.bind(post, clickListener)
    }

    override fun getItemCount(): Int = posts.size

    fun setClickListener(listener: (Post) -> Unit) {
        clickListener = listener
    }

    fun setData(posts: Posts) {
        this.posts.clear()
        this.posts.addAll(posts)
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        private val binding = FragmentPostListItemBinding.bind(view)

        fun bind(post: Post, clickListener: (Post) -> Unit) {
            with(binding) {
                title.text = post.title
                body.text = post.body
            }
            view.setOnClickListener { clickListener(post) }
        }
    }
}