package com.vjezba.androidjetpackgithub.ui.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.vjezba.androidjetpackgithub.ui.fragments.LanguagesFragment
import com.vjezba.androidjetpackgithub.ui.fragments.SavedLanguagesFragment


const val SAVED_GITHUB_REPOSITORIES = 0
const val ALL_GITHUBS = 1

class GithubPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    /**
     * Mapping of the ViewPager page indexes to their respective Fragments
     */
    private val tabFragmentsCreators: Map<Int, () -> Fragment> = mapOf(
        SAVED_GITHUB_REPOSITORIES to { SavedLanguagesFragment() },
        ALL_GITHUBS to { LanguagesFragment() }
    )

    override fun getItemCount() = tabFragmentsCreators.size

    override fun createFragment(position: Int): Fragment {
        return tabFragmentsCreators[position]?.invoke() ?: throw IndexOutOfBoundsException()
    }
}
