package com.vjezba.androidjetpackgithub.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.vjezba.androidjetpackgithub.R
import com.vjezba.androidjetpackgithub.databinding.FragmentViewPagerBinding
import com.vjezba.androidjetpackgithub.ui.adapters.ALL_GITHUBS
import com.vjezba.androidjetpackgithub.ui.adapters.GithubPagerAdapter
import com.vjezba.androidjetpackgithub.ui.adapters.SAVED_GITHUB_REPOSITORIES
import kotlinx.android.synthetic.main.activity_languages_main.*


/**
 * A simple [Fragment] subclass.
 * Use the [HomeViewPagerFragment.newInstance] factory method to
 * create an instance of this fragments.
 */
class HomeViewPagerFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentViewPagerBinding.inflate(inflater, container, false)
        val tabLayout = binding.tabs
        val viewPager = binding.viewPager

        activity?.speedDial?.visibility = View.VISIBLE

        viewPager.adapter =
            GithubPagerAdapter(this)

        // Set the icon and text for each tab
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.setIcon(getTabIcon(position))
            tab.text = getTabTitle(position)
        }.attach()

        //(activity as AppCompatActivity).setSupportActionBar(binding.toolbar)

        (activity as AppCompatActivity?)?.setSupportActionBar(activity?.toolbar)

        //activity?.windowManager.setSupportActionBar(findViewById(R.id.toolbar))
        //supportActionBar?.setDisplayHomeAsUpEnabled(true)

        return binding.root
    }

    private fun getTabIcon(position: Int): Int {
        return when (position) {
            SAVED_GITHUB_REPOSITORIES -> R.drawable.garden_tab_selector
            ALL_GITHUBS -> R.drawable.plant_list_tab_selector
            else -> throw IndexOutOfBoundsException()
        }
    }

    private fun getTabTitle(position: Int): String? {
        return when (position) {
            SAVED_GITHUB_REPOSITORIES -> "Saved language".toUpperCase()
            ALL_GITHUBS -> "All languagess".toUpperCase()
            else -> null
        }
    }
}