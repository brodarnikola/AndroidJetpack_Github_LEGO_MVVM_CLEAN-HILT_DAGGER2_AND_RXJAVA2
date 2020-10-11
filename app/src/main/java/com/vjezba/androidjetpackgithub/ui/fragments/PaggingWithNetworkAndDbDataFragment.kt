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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.vjezba.androidjetpackgithub.R
import com.vjezba.androidjetpackgithub.databinding.FragmentPaggingNetworkAndDbDataBinding
import com.vjezba.androidjetpackgithub.ui.adapters.languagerepos.ReposAdapter
import com.vjezba.androidjetpackgithub.ui.adapters.languagerepos.ReposLoadStateAdapter
import com.vjezba.androidjetpackgithub.viewmodels.PaggingWithNetworkAndDbDataViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_languages_main.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import java.io.InvalidObjectException


@ExperimentalCoroutinesApi
@AndroidEntryPoint
class PaggingWithNetworkAndDbDataFragment : Fragment() {

    private val viewModel : PaggingWithNetworkAndDbDataViewModel by viewModels()
    private val args: PaggingWithNetworkAndDbDataFragmentArgs by navArgs()

    private val adapter = ReposAdapter()

    private var progressBarRepos: ProgressBar? = null
    private var languageListRepository: RecyclerView? = null

    private var searchJob: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentPaggingNetworkAndDbDataBinding.inflate(inflater, container, false)
        context ?: return binding.root

        activity?.toolbar?.title = getString(R.string.gallery_title) + ": " + args.languageName
        activity?.speedDial?.visibility = View.GONE

        progressBarRepos = binding.progressBarRepositories
        languageListRepository = binding.languageListRepos


        // add dividers between RecyclerView's row items
        val decoration = DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        binding.languageListRepos.addItemDecoration(decoration)

        binding.languageListRepos.adapter = adapter.withLoadStateHeaderAndFooter(
            header = ReposLoadStateAdapter { adapter.retry() },
            footer = ReposLoadStateAdapter { adapter.retry() }
        )

        adapter.addLoadStateListener { loadState ->
            binding.languageListRepos.isVisible = loadState.source.refresh is LoadState.NotLoading
            // Show loading spinner during initial load or refresh.
            binding.progressBarRepositories.isVisible = loadState.source.refresh is LoadState.Loading
            // Show the retry state if initial load or refresh fails.
            binding.retryButton.isVisible = loadState.source.refresh is LoadState.Error

            // Toast on any error, regardless of whether it came from RemoteMediator or PagingSource
            val errorState = loadState.source.append as? LoadState.Error
                ?: loadState.source.prepend as? LoadState.Error
                ?: loadState.append as? LoadState.Error
                ?: loadState.prepend as? LoadState.Error
            errorState?.let {
                if( it.error != InvalidObjectException("") ) {
                    Toast.makeText(
                        requireContext(),
                        "\uD83D\uDE28 Wooops ${it.error}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }


        return binding.root
    }

    override fun onResume() {
        super.onResume()

        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            viewModel.searchRepo(args.languageName).collectLatest {
                adapter.submitData(it)
            }
        }

        // Scroll to top when the list is refreshed from network.
        lifecycleScope.launch {
            adapter.loadStateFlow
                // Only emit when REFRESH LoadState for RemoteMediator changes.
                .distinctUntilChangedBy { it.refresh }
                // Only react to cases where Remote REFRESH completes i.e., NotLoading.
                .filter { it.refresh is LoadState.NotLoading }
                .collect { languageListRepository?.scrollToPosition(0) }
        }

    }


}