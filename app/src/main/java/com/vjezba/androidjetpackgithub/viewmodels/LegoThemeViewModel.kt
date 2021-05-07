

package com.vjezba.androidjetpackgithub.viewmodels

import androidx.lifecycle.ViewModel
import com.vjezba.data.di.lego.LegoNetwork
import com.vjezba.data.lego.repository.LegoThemeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * The ViewModel for [LegoThemeFragment].
 */

@HiltViewModel
class LegoThemeViewModel @Inject constructor(@LegoNetwork repository: LegoThemeRepository) : ViewModel() {

    val legoThemes= repository.themes
}
