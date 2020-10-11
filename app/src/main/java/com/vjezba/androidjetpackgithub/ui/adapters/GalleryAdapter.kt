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

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.vjezba.androidjetpackgithub.databinding.ListItemPhotoBinding
import com.vjezba.domain.model.RepositoryDetailsResponse

/**
 * Adapter for the [RecyclerView] in [GalleryFragment].
 */

class GalleryAdapter : PagingDataAdapter<RepositoryDetailsResponse, GalleryAdapter.GalleryViewHolder>(
    GalleryDiffCallback()
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        return GalleryViewHolder(
            ListItemPhotoBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        val photo = getItem(position)
        if (photo != null) {
            holder.bind(photo)
        }
    }

    class GalleryViewHolder(
        private val binding: ListItemPhotoBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setClickListener { view ->
                binding.photo?.let { photo ->
                    val uri = Uri.parse(photo.html_url)
                    val intent = Intent(Intent.ACTION_VIEW, uri)
                    view.context.startActivity(intent)
                }
            }
        }

        fun bind(item: RepositoryDetailsResponse) {
            binding.apply {
                photo = item
                executePendingBindings()
            }
        }
    }
}

private class GalleryDiffCallback : DiffUtil.ItemCallback<RepositoryDetailsResponse>() {
    override fun areItemsTheSame(oldItem: RepositoryDetailsResponse, newItem: RepositoryDetailsResponse): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: RepositoryDetailsResponse, newItem: RepositoryDetailsResponse): Boolean {
        return oldItem == newItem
    }
}
