package com.vjezba.data.lego.repository

import com.vjezba.data.database.dao.LegoThemeDao
import com.vjezba.data.di.LegoNetwork
import com.vjezba.data.lego.data.resultLiveData
import com.vjezba.data.lego.repository.LegoThemeRemoteDataSource
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository module for handling data operations.
 */
@Singleton
class LegoThemeRepository @Inject constructor(@LegoNetwork private val dao: LegoThemeDao,
                                              @LegoNetwork private val remoteSource: LegoThemeRemoteDataSource
) {

    val themes = resultLiveData(
        databaseQuery = { dao.getLegoThemes() },
        networkCall = { remoteSource.fetchData() },
        saveCallResult = { dao.insertAll(it.results) })

}
