

package com.vjezba.androidjetpackgithub.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.vjezba.data.di.LegoNetwork
import com.vjezba.data.lego.repository.LegoThemeRepository
import javax.inject.Inject

/**
 * The ViewModel for [LegoThemeFragment].
 */
class LegoThemeViewModel @ViewModelInject constructor( @LegoNetwork repository: LegoThemeRepository) : ViewModel() {

    val legoThemes= repository.themes
}
