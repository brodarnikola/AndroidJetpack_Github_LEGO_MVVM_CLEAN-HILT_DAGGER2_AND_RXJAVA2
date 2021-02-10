

package com.vjezba.androidjetpackgithub.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.vjezba.data.di.lego.LegoNetwork
import com.vjezba.data.lego.repository.LegoThemeRepository

/**
 * The ViewModel for [LegoThemeFragment].
 */
class LegoThemeViewModel @ViewModelInject constructor( @LegoNetwork repository: LegoThemeRepository) : ViewModel() {

    val legoThemes= repository.themes
}
