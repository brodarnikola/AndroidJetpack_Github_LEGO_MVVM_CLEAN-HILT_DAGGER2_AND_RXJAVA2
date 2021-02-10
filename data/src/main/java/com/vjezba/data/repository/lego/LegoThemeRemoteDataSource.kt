package com.vjezba.data.lego.repository

import com.vjezba.data.di.lego.LegoNetwork
import com.vjezba.data.lego.api.BaseDataSource
import com.vjezba.data.lego.api.LegoService
import javax.inject.Inject

/**
 * Works with the Lego API to get data.
 */
class LegoThemeRemoteDataSource @Inject constructor( @LegoNetwork private val service: LegoService) : BaseDataSource() {

    suspend fun fetchData() = getResult { service.getThemes(1, 1000, "-id") }

}
