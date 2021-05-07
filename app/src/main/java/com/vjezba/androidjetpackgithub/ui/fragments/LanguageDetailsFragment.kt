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

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.app.ShareCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.vjezba.androidjetpackgithub.R
import com.vjezba.androidjetpackgithub.databinding.FragmentLanguageDetailsBinding
import com.vjezba.androidjetpackgithub.viewmodels.LanguageDetailsViewModel
import com.vjezba.domain.model.Languages
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_languages_main.*
import kotlinx.android.synthetic.main.fragment_language_details.*

/**
 * A fragments representing a single Plant detail screen.
 */

@AndroidEntryPoint
class LanguageDetailsFragment : Fragment() {

    private val args: LanguageDetailsFragmentArgs by navArgs()

    private lateinit var binding: FragmentLanguageDetailsBinding

    private val languageDetailsViewModel : LanguageDetailsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentLanguageDetailsBinding.inflate(inflater, container, false)

        activity?.speedDial?.visibility = View.GONE
        binding.galleryNav.setOnClickListener { navigateToGallery() }

        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        languageDetailsViewModel.isLanguageSaved(args.languagesId)
            .observe(viewLifecycleOwner, { isLanguageSaved ->
                if (isLanguageSaved != null && isLanguageSaved) {
                    fab.hide()
                } else {
                    fab.show()
                    fab.setOnClickListener {
                        hideAppBarFab(fab)
                        languageDetailsViewModel.saveProgrammingLanguage(args.languagesId)
                        Snackbar.make(
                            binding.root,
                            R.string.saved_language_successfully,
                            Snackbar.LENGTH_LONG
                        )
                            .show()
                    }
                }
            })

        setDetailsAboutLanguage()

        activity?.toolbar?.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.action_share -> {
                    createShareIntent()
                    true
                }
                else -> false
            }
        }
    }

    private fun setDetailsAboutLanguage() {

        languageDetailsViewModel.languageDetails(args.languagesId)
            .observe(viewLifecycleOwner, Observer { languages ->
                Glide.with(requireContext())
                    .load(languages.imageUrl)
                    .placeholder(R.color.sunflower_gray_50)
                    //.error(R.drawable.ic_detail_share)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(detail_image)

                language_detail_name.text = "" + languages.name
                binding.languageCreatedBy.text = languages?.createdBy
                binding.languageCreatedAt.text = ""+ languages?.createdAt
                language_description.text = "" + languages.description
            })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_language_details, menu)
    }

    private fun navigateToGallery() {
        languageDetailsViewModel.languageDetails(args.languagesId)
            .observe(viewLifecycleOwner,  { languages ->
                val direction =
                    LanguageDetailsFragmentDirections.actionLanguageDetailFragmentToGalleryFragment(
                        languages.name
                    )
                findNavController().navigate(direction)
            })
    }

    // Helper function for calling a share functionality.
    // Should be used when user presses a share button/menu item.
    @Suppress("DEPRECATION")
    private fun createShareIntent() {

       languageDetailsViewModel.languageDetails(args.languagesId).observe(viewLifecycleOwner, { languages ->
            val shareText = if (languages == null) {
                ""
            } else {
                getString(R.string.share_text_language_details, languages.name)
            }

           val shareIntent = ShareCompat.IntentBuilder.from(requireActivity())
               .setText(shareText)
               .setType("text/plain")
               .createChooserIntent()
               .addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
           startActivity(shareIntent)
        })
    }

    // FloatingActionButtons anchored to AppBarLayouts have their visibility controlled by the scroll position.
    // We want to turn this behavior off to hide the FAB when it is clicked.
    //
    // This is adapted from Chris Banes' Stack Overflow answer: https://stackoverflow.com/a/41442923
    private fun hideAppBarFab(fab: FloatingActionButton) {
        //val params = fab.layoutParams as CoordinatorLayout.LayoutParams
        //val behavior = params.behavior as FloatingActionButton.Behavior
        //behavior.isAutoHideEnabled = false
        fab.hide()
    }

    fun interface Callback {
        fun add(plant: Languages?)
    }
}
