package com.vjezba.data.lego.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PagedList
import com.vjezba.data.database.dao.LegoSetDao
import com.vjezba.data.database.model.LegoSet
import com.vjezba.data.di.lego.LegoNetwork
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject

class LegoSetPageDataSourceFactory @Inject constructor(
    private val themeId: Int? = null,
    @LegoNetwork private val dataSource: LegoSetRemoteDataSource,
    private val dao: LegoSetDao,
    private val scope: CoroutineScope) : DataSource.Factory<Int, LegoSet>() {

    private val liveData = MutableLiveData<LegoSetPageDataSource>()

    override fun create(): DataSource<Int, LegoSet> {
        val source = LegoSetPageDataSource(
            themeId,
            dataSource,
            dao,
            scope
        )
        liveData.postValue(source)
        return source
    }

    companion object {
        private const val PAGE_SIZE = 100

        fun pagedListConfig() = PagedList.Config.Builder()
                .setInitialLoadSizeHint(PAGE_SIZE)
                .setPageSize(PAGE_SIZE)
                .setEnablePlaceholders(true)
                .build()
    }

}