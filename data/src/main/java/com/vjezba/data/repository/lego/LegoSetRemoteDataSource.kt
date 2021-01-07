package com.vjezba.data.lego.repository

import com.vjezba.data.di.LegoNetwork
import com.vjezba.data.lego.api.BaseDataSource
import com.vjezba.data.lego.api.LegoService
import javax.inject.Inject

/**
 * Works with the Lego API to get data.
 */
class LegoSetRemoteDataSource @Inject constructor( @LegoNetwork private val service: LegoService) : BaseDataSource() {

    suspend fun fetchSets(page: Int, pageSize: Int? = null, themeId: Int? = null)
            = getResult { service.getSets(page, pageSize, themeId, "-year") }

    suspend fun fetchSet(id: String)
            = getResult { service.getSet(id) }
}
