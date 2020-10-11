/*
 * Copyright 2018 Google LLC
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
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vjezba.androidjetpackgithub.databinding.ListItemLanguagesBinding
import com.vjezba.androidjetpackgithub.ui.fragments.HomeViewPagerFragmentDirections
import com.vjezba.domain.model.Languages

/**
 * Adapter for the [RecyclerView] in [PlantListFragment].
 */
class LanguagesAdapter : ListAdapter<Languages, RecyclerView.ViewHolder>(PlantDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return LanguageViewHolder(
            ListItemLanguagesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val plant = getItem(position)
        (holder as LanguageViewHolder).bind(plant)
    }

    class LanguageViewHolder(
        private val binding: ListItemLanguagesBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setClickListenerLanguages {
                binding.language?.let { language ->
                    navigateToLanguage(language, it)
                }
            }
        }

        private fun navigateToLanguage(
            language: Languages,
            view: View
        ) {
            val direction = HomeViewPagerFragmentDirections.actionViewPagerFragmentToLanguageDetailsFragment(language.languageId)
            view.findNavController().navigate(direction)
        }

        fun bind(item: Languages) {
            binding.apply {
                language = item
                executePendingBindings()
            }
        }
    }
}

private class PlantDiffCallback : DiffUtil.ItemCallback<Languages>() {

    override fun areItemsTheSame(oldItem: Languages, newItem: Languages): Boolean {
        return oldItem.languageId == newItem.languageId
    }

    override fun areContentsTheSame(oldItem: Languages, newItem: Languages): Boolean {
        return oldItem == newItem
    }
}
