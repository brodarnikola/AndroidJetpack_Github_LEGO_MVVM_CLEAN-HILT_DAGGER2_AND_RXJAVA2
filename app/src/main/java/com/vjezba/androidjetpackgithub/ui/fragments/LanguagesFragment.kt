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

package com.vjezba.androidjetpackgithub.ui.fragments

import android.os.Bundle
import android.view.*
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.vjezba.androidjetpackgithub.R
import com.vjezba.androidjetpackgithub.databinding.FragmentLanguagesBinding
import com.vjezba.androidjetpackgithub.ui.adapters.LanguagesAdapter
import com.vjezba.androidjetpackgithub.viewmodels.LanguagesListViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LanguagesFragment : Fragment() {

    private val viewModel: LanguagesListViewModel by viewModels()

    var menuItemFinal: MenuItem? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentLanguagesBinding.inflate(inflater, container, false)
        context ?: return binding.root

        val adapter =
            LanguagesAdapter()
        binding.languagesList.adapter = adapter
        subscribeUi(adapter)

        setHasOptionsMenu(true)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_language_list, menu)
        with(viewModel) {
            if (isFiltered()) {
                menu.get(0).title = getString(R.string.menu_filter_none)
                menuItemFinal = menu[0]
            } else {
                menu.get(0).title = getString(R.string.menu_filter_by_language)
                menuItemFinal = menu[0]
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.filter_zone -> {
                updateData()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun subscribeUi(adapter: LanguagesAdapter) {
        viewModel.languages.observe(viewLifecycleOwner,  Observer { plants ->
            adapter.submitList(plants)
        })
    }

    private fun updateData() {
        with(viewModel) {
            if (isFiltered()) {
                clearFilterForMobileProgrammingLanguages()
                menuItemFinal?.title = getString(R.string.menu_filter_by_language)
            } else {
                setOnlyMobileProgrammingLanguages("Mobile")
                menuItemFinal?.title = getString(R.string.menu_filter_none)
            }
        }
    }
}
