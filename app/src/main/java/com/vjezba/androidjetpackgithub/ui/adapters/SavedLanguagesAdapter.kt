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
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vjezba.androidjetpackgithub.R
import com.vjezba.androidjetpackgithub.databinding.ListItemSavedLanguagesBinding
import com.vjezba.androidjetpackgithub.ui.fragments.HomeViewPagerFragmentDirections
import com.vjezba.androidjetpackgithub.viewmodels.SavedAndAllLanguagesViewModel
import com.vjezba.domain.model.SavedAndAllLanguages
import com.vjezba.domain.model.SavedLanguages

class SavedLanguagesAdapter(
    val clickListener: (Int) -> Unit, ) :
    ListAdapter<SavedAndAllLanguages, SavedLanguagesAdapter.ViewHolder>(
        LanguageDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item_saved_languages,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener, position)
    }

    class ViewHolder(
        private val binding: ListItemSavedLanguagesBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setClickListener { view ->
                binding.viewModel?.languagesId?.let { languagesId ->
                    navigateToLanguageDetails(languagesId, view)
                }
            }
        }

        private fun navigateToLanguageDetails(languagesId: Int, view: View) {
            val direction = HomeViewPagerFragmentDirections.actionViewPagerFragmentToLanguageDetailsFragment(languagesId)
            //val direction = HomeViewPagerFragmentDirections.actionViewPagerFragmentToGithubRepoDetailFragment(languagesId)
            view.findNavController().navigate(direction)
        }

        fun bind(savedLanguage: SavedAndAllLanguages, clickListener: (Int) -> Unit, position: Int) {
            with(binding) {
                viewModel = SavedAndAllLanguagesViewModel(savedLanguage)
                binding.ivDeleteLanguage.setOnClickListener {
                    clickListener( savedLanguage.languages.languageId )
                }
                executePendingBindings()
            }
        }
    }
}

private class LanguageDiffCallback : DiffUtil.ItemCallback<SavedAndAllLanguages>() {

    override fun areItemsTheSame(
        oldItem: SavedAndAllLanguages,
        newItem: SavedAndAllLanguages
    ): Boolean {
        return oldItem.languages.languageId == newItem.languages.languageId
    }

    override fun areContentsTheSame(
        oldItem: SavedAndAllLanguages,
        newItem: SavedAndAllLanguages
    ): Boolean {
        return oldItem.languages == newItem.languages
    }
}
