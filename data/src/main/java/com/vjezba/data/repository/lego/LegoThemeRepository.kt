package com.vjezba.data.lego.repository

import com.vjezba.data.database.dao.LegoThemeDao
import com.vjezba.data.di.lego.LegoNetwork
import com.vjezba.data.lego.data.resultLiveData
import javax.inject.Inject

/**
 * Repository module for handling data operations.
 */

class LegoThemeRepository @Inject constructor(  private val dao: LegoThemeDao,
                                                  @LegoNetwork private val remoteSource: LegoThemeRemoteDataSource
) {

    val themes = resultLiveData(
        databaseQuery = { dao.getLegoThemes() },
        networkCall = { remoteSource.fetchData() },
        saveCallResult = { dao.insertAll(it.results) })

}
