package com.vjezba.androidjetpackgithub.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.vjezba.data.di.CoroutineScropeIO
import com.vjezba.data.di.LegoNetwork
import com.vjezba.data.lego.repository.LegoSetRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel

/**
 * The ViewModel for [LegoSetsFragment].
 */
class LegoSetsViewModel @ViewModelInject constructor(@LegoNetwork private val repository: LegoSetRepository,
                                                     @CoroutineScropeIO private val ioCoroutineScope: CoroutineScope)
    : ViewModel() {

    var connectivityAvailable: Boolean = false
    var themeId: Int? = null

    val legoSets by lazy {
        repository.observePagedSets(
                connectivityAvailable, themeId, ioCoroutineScope)
    }

    /**
     * Cancel all coroutines when the ViewModel is cleared.
     */
    override fun onCleared() {
        super.onCleared()
        ioCoroutineScope.cancel()
    }
}
