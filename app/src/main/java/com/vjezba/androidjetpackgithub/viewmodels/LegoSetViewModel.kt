

package com.vjezba.androidjetpackgithub.viewmodels

import androidx.lifecycle.ViewModel
import com.vjezba.data.di.lego.LegoNetwork
import com.vjezba.data.lego.repository.LegoSetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * The ViewModel used in [LegoSetFragment].
 */

@HiltViewModel
class LegoSetViewModel @Inject constructor(@LegoNetwork repository: LegoSetRepository) : ViewModel() {

    lateinit var id: String

    val legoSet by lazy { repository.observeSet(id) }

}
