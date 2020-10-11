package com.vjezba.androidjetpackgithub.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.viewpager2.widget.ViewPager2
import com.vjezba.androidjetpackgithub.R
import com.vjezba.androidjetpackgithub.databinding.FragmentSavedLanguagesBinding
import com.vjezba.androidjetpackgithub.ui.adapters.ALL_GITHUBS
import com.vjezba.androidjetpackgithub.ui.adapters.SavedLanguagesAdapter
import com.vjezba.androidjetpackgithub.viewmodels.SavedLanguagesListViewModel
import dagger.hilt.android.AndroidEntryPoint


/**
 * A simple [Fragment] subclass.
 * Use the [SavedLanguagesFragment.newInstance] factory method to
 * create an instance of this fragments.
 */

@AndroidEntryPoint
class SavedLanguagesFragment : Fragment() {

    private lateinit var binding: FragmentSavedLanguagesBinding

    private val viewModel : SavedLanguagesListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSavedLanguagesBinding.inflate(inflater, container, false)
        val adapter =
            SavedLanguagesAdapter( { position: Int -> setDeleteLanguageClickListener(position) })
        binding.savedLanguageList.adapter = adapter

        binding.addLanguage.setOnClickListener {
            navigateToPlantListPage()
        }

        subscribeUi(adapter, binding)
        return binding.root
    }

    private fun setDeleteLanguageClickListener(position: Int) {
        viewModel.deleteSelectedProgrammingLanguage(position)
    }

    private fun subscribeUi(adapter: SavedLanguagesAdapter, binding: FragmentSavedLanguagesBinding) {
        viewModel.savedAndAllLanguages.observe(viewLifecycleOwner, Observer { result ->
            binding.hasLanguages = !result.isNullOrEmpty()
            adapter.submitList(result)
        })
    }

    private fun navigateToPlantListPage() {
        requireActivity().findViewById<ViewPager2>(R.id.view_pager).currentItem =
            ALL_GITHUBS
    }
}