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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.vjezba.androidjetpackgithub.R
import com.vjezba.androidjetpackgithub.databinding.FragmentLanguageDetailsBinding
import com.vjezba.androidjetpackgithub.viewmodels.LanguageDetailsViewModel
import com.vjezba.domain.model.Languages
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_languages_main.*
import javax.inject.Inject

/**
 * A fragments representing a single Plant detail screen.
 */

@AndroidEntryPoint
class LanguageDetailsFragment : Fragment() {

    private val args: LanguageDetailsFragmentArgs by navArgs()

    @Inject
    lateinit var languageDetailsViewModelFactory: LanguageDetailsViewModel.AssistedFactory

    private val languageDetailsViewModel: LanguageDetailsViewModel by viewModels {
        LanguageDetailsViewModel.provideFactory(
            languageDetailsViewModelFactory,
            args.languagesId
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<FragmentLanguageDetailsBinding>(
            inflater,
            R.layout.fragment_language_details,
            container,
            false
        ).apply {
            viewModel = languageDetailsViewModel
            lifecycleOwner = viewLifecycleOwner
            callback =
                Callback { repoDetails ->
                    repoDetails?.let {
                        hideAppBarFab(fab)
                        languageDetailsViewModel.saveProgrammingLanguage()
                        Snackbar.make(
                            root,
                            R.string.saved_language_successfully,
                            Snackbar.LENGTH_LONG
                        )
                            .show()
                    }
                }

            activity?.speedDial?.visibility = View.GONE
            galleryNav.setOnClickListener { navigateToGallery() }

            var isToolbarShown = false

            // scroll change listener begins at Y = 0 when image is fully collapsed
            /*languageDetailScrollview.setOnScrollChangeListener(
                NestedScrollView.OnScrollChangeListener { _, _, scrollY, _, _ ->

                    // User scrolled past image to height of toolbar and the title text is
                    // underneath the toolbar, so the toolbar should be shown.
                    val shouldShowToolbar = scrollY > toolbar.height

                    // The new state of the toolbar differs from the previous state; update
                    // appbar and toolbar attributes.
                    if (isToolbarShown != shouldShowToolbar) {
                        isToolbarShown = shouldShowToolbar

                        // Use shadow animator to add elevation if toolbar is shown
                        appbar.isActivated = shouldShowToolbar

                        // Show the plant name if toolbar is shown
                        toolbarLayout.isTitleEnabled = shouldShowToolbar
                    }
                }
            )*/

            /*toolbar.setNavigationOnClickListener { view ->
                view.findNavController().navigateUp()
            }*/

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
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_language_details, menu)
    }

    private fun navigateToGallery() {
        languageDetailsViewModel.languageDetails.value?.let { language ->
            val direction =
                LanguageDetailsFragmentDirections.actionLanguageDetailFragmentToGalleryFragment(
                    language.name
                )
            findNavController().navigate(direction)
        }
    }

    // Helper function for calling a share functionality.
    // Should be used when user presses a share button/menu item.
    @Suppress("DEPRECATION")
    private fun createShareIntent() {
        val shareText = languageDetailsViewModel.languageDetails.value.let { plant ->
            if (plant == null) {
                ""
            } else {
                getString(R.string.share_text_language_details, plant.name)
            }
        }
        val shareIntent = ShareCompat.IntentBuilder.from(requireActivity())
            .setText(shareText)
            .setType("text/plain")
            .createChooserIntent()
            .addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT or Intent.FLAG_ACTIVITY_MULTIPLE_TASK)
        startActivity(shareIntent)
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
