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
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.vjezba.androidjetpackgithub.R
import com.vjezba.data.Post


/**
 * Adapter for the [RecyclerView] in [GalleryFragment].
 */

class RxJava2SwitchMapAdapter constructor( mOnPostClickListener: OnPostClickListener) :
    RecyclerView.Adapter<RxJava2SwitchMapAdapter.ReposFlatMapHolder>() {

    private val TAG = "RecyclerAdapter"

    private var posts: MutableList<Post> = mutableListOf()

    private var onPostClickListener: OnPostClickListener? = null

    init {
        onPostClickListener = mOnPostClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReposFlatMapHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_rxjava2_switchmap, null, false)
        return ReposFlatMapHolder(view, onPostClickListener)
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

    fun getPosts(): MutableList<Post> {
        return posts
    }

    class ReposFlatMapHolder(itemView: View, mOnPostClickListener: OnPostClickListener?) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var title: TextView

        var onPostClickListener: OnPostClickListener? = null

        fun bind(post: Post) {
            title.setText(post.title)
        }

        init {
            title = itemView.findViewById(R.id.title)
            onPostClickListener = mOnPostClickListener
            itemView.setOnClickListener(this)
        }

        override fun onClick(view: View?) {
            onPostClickListener?.onPostClick(bindingAdapterPosition)
        }
    }

    interface OnPostClickListener {
        fun onPostClick(position: Int)
    }

}

