/*
 * Copyright 2020 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vjezba.androidjetpackgithub.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vjezba.androidjetpackgithub.R
import com.vjezba.data.Post
import com.vjezba.data.networking.model.RepositoryDetailsResponseApi


/**
 * Adapter for the [RecyclerView] in [GalleryFragment].
 */

class ReposRxJava2FlatMapAdapter :
    RecyclerView.Adapter<ReposRxJava2FlatMapAdapter.ReposFlatMapHolder>() {

    private val TAG = "RecyclerAdapter"

    private var posts: MutableList<Post> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReposFlatMapHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_rxjava2_flatmap_repos, null, false)
        return ReposFlatMapHolder(view)
    }

    override fun onBindViewHolder(holder: ReposFlatMapHolder, position: Int) {
        val photo = posts.get(position)
        if (photo != null) {
            holder.bind(photo)
        }
    }


    override fun getItemCount(): Int {
        return posts.size
    }

    fun setPosts(mPosts: MutableList<Post>) {
        posts = mPosts
        notifyDataSetChanged()
    }

    fun updatePost(post: Post) {
        val position = posts.indexOf(post)
        posts.set(position, post)
        notifyItemChanged(posts.indexOf(post))
    }

    fun getPosts(): List<Post?>? {
        return posts
    }

    class ReposFlatMapHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var title: TextView
        var numComments: TextView
        var progressBar: ProgressBar
        fun bind(post: Post) {
            title.setText(post.title)
            if (post.comments  == null) {
                showProgressBar(true)
                numComments.text = ""
            } else {
                showProgressBar(false)
                numComments.setText("" + post.comments!!.size)
            }
        }

        private fun showProgressBar(showProgressBar: Boolean) {
            if (showProgressBar) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        }

        init {
            title = itemView.findViewById(R.id.title)
            numComments = itemView.findViewById(R.id.num_comments)
            progressBar = itemView.findViewById(R.id.progress_bar)
        }
    }


}

