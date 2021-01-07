package com.vjezba.androidjetpackgithub.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import com.vjezba.androidjetpackgithub.R
import com.vjezba.androidjetpackgithub.databinding.FragmentLegoThemesBinding
import com.vjezba.androidjetpackgithub.ui.adapters.LegoThemeAdapter
import com.vjezba.androidjetpackgithub.ui.utilities.VerticalItemDecoration
import com.vjezba.androidjetpackgithub.ui.utilities.hide
import com.vjezba.androidjetpackgithub.ui.utilities.show
import com.vjezba.androidjetpackgithub.viewmodels.LegoThemeViewModel
import com.vjezba.data.lego.data.Result
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_languages_main.*

@AndroidEntryPoint
class LegoThemeFragment : Fragment() {

    private val viewModel : LegoThemeViewModel by viewModels()

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val binding = FragmentLegoThemesBinding.inflate(inflater, container, false)
        context ?: return binding.root

        val adapter = LegoThemeAdapter()
        binding.recyclerView.addItemDecoration(
                VerticalItemDecoration(resources.getDimension(R.dimen.margin_normal).toInt(), true) )
        binding.recyclerView.adapter = adapter

        subscribeUi(binding, adapter)
        activity?.speedDial?.visibility = View.GONE

        setHasOptionsMenu(true)
        return binding.root
    }

    private fun subscribeUi(binding: FragmentLegoThemesBinding, adapter: LegoThemeAdapter) {
        viewModel.legoThemes.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    binding.progressBar.hide()
                    result.data?.let { adapter.submitList(it) }
                }
                Result.Status.LOADING -> binding.progressBar.show()
                Result.Status.ERROR -> {
                    binding.progressBar.hide()
                    Snackbar.make(binding.root, result.message!!, Snackbar.LENGTH_LONG).show()
                }
            }
        })
    }
}