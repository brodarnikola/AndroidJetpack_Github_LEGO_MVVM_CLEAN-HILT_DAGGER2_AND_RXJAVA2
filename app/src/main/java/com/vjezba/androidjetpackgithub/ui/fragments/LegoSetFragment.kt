package com.vjezba.androidjetpackgithub.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.google.android.material.snackbar.Snackbar
import com.vjezba.androidjetpackgithub.R
import com.vjezba.androidjetpackgithub.databinding.FragmentLegoSetBinding
import com.vjezba.androidjetpackgithub.di.injectViewModel
import com.vjezba.androidjetpackgithub.ui.adapters.bindImageFromUrl
import com.vjezba.androidjetpackgithub.ui.utilities.*
import com.vjezba.androidjetpackgithub.viewmodels.LegoSetViewModel
import com.vjezba.androidjetpackgithub.viewmodels.LegoThemeViewModel
import com.vjezba.data.database.model.LegoSet
import  com.vjezba.data.lego.data.Result
import dagger.hilt.android.AndroidEntryPoint

/**
 * A fragment representing a single LegoSet detail screen.
 */
@AndroidEntryPoint
class LegoSetFragment : Fragment() {

    private val viewModel : LegoSetViewModel by viewModels()

    private val args: LegoSetFragmentArgs by navArgs()
    private lateinit var set: LegoSet

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        viewModel.id = args.id

        val binding = DataBindingUtil.inflate<FragmentLegoSetBinding>(
                inflater, R.layout.fragment_lego_set, container, false).apply {
            lifecycleOwner = this@LegoSetFragment
            fab.setOnClickListener { _ -> set.url?.let { intentOpenWebsite(requireActivity(), it) } }
        }

        subscribeUi(binding)

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_language_details, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @Suppress("DEPRECATION")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_share -> {
                intentShareText(requireActivity(), getString(R.string.share_lego_set, set.name, set.url ?: ""))
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun subscribeUi(binding: FragmentLegoSetBinding) {
        viewModel.legoSet.observe(viewLifecycleOwner, Observer { result ->
            when (result.status) {
                Result.Status.SUCCESS -> {
                    binding.progressBar.hide()
                    result.data?.let { bindView(binding, it) }
                }
                Result.Status.LOADING -> binding.progressBar.show()
                Result.Status.ERROR -> {
                    binding.progressBar.hide()
                    Snackbar.make(binding.coordinatorLayout, result.message!!, Snackbar.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun bindView(binding: FragmentLegoSetBinding, legoSet: LegoSet) {
        legoSet.apply {
            setTitle(name)
            bindImageFromUrl(binding.image, imageUrl)
            binding.name.text = name
            binding.year.text = getString(R.string.year, year ?: 0)
            binding.numParts.text = getString(R.string.number_of_parts, numParts ?: 0)
            set = legoSet
        }
    }
}
