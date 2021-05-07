package com.vjezba.androidjetpackgithub.viewmodels

import androidx.lifecycle.ViewModel
import com.vjezba.data.di.lego.CoroutineScropeIO
import com.vjezba.data.di.lego.LegoNetwork
import com.vjezba.data.lego.repository.LegoSetRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import javax.inject.Inject

/**
 * The ViewModel for [LegoSetsFragment].
 */

@HiltViewModel
class LegoSetsViewModel @Inject constructor(@LegoNetwork private val repository: LegoSetRepository,
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
