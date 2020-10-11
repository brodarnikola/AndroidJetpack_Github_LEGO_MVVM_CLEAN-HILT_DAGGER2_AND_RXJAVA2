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

package com.vjezba.androidjetpackgithub.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.vjezba.androidjetpackgithub.R
import com.vjezba.androidjetpackgithub.ui.adapters.GalleryAdapter
import com.vjezba.androidjetpackgithub.databinding.FragmentGalleryBinding
import com.vjezba.androidjetpackgithub.viewmodels.GalleryViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_languages_main.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class GalleryFragment : Fragment() {

    private var progressBarRepos: ProgressBar? = null
    private var languageListRepository: RecyclerView? = null

    private val adapter = GalleryAdapter()
    private val args: GalleryFragmentArgs by navArgs()
    private var searchJob: Job? = null

    private val viewModel : GalleryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentGalleryBinding.inflate(inflater, container, false)
        context ?: return binding.root

        progressBarRepos = binding.progressBarRepositories
        languageListRepository = binding.languageListRepos

        binding.languageListRepos.adapter = adapter
        search(args.languageName)

        activity?.speedDial?.visibility = View.GONE
        activity?.toolbar?.title = getString(R.string.gallery_title) + ": " + args.languageName

        adapter.addLoadStateListener { loadState ->
            binding.languageListRepos.isVisible = loadState.source.refresh is LoadState.NotLoading
            // Show loading spinner during initial load or refresh.
            binding.progressBarRepositories.isVisible = loadState.source.refresh is LoadState.Loading

            // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                Toast.makeText(
                    requireContext(),
                    "\uD83D\uDE28 Wooops ${it.error}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        return binding.root
    }

    private fun search(query: String) {
        // Make sure we cancel the previous job before creating a new one
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.searchGithubRepositoryByProgrammingLanguage(query).collectLatest {
                adapter.submitData(it)
            }
        }
    }
}
