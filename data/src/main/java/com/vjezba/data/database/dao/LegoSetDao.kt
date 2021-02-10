package com.vjezba.data.database.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.paging.PagingData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vjezba.data.database.model.LegoSet
import kotlinx.coroutines.flow.Flow

/**
 * The Data Access Object for the LegoSet class.
 */
@Dao
interface LegoSetDao {

    @Query("SELECT * FROM sets WHERE themeId = :themeId ORDER BY year DESC")
    fun getLegoSets(themeId: Int): LiveData<List<LegoSet>>

    @Query("SELECT * FROM sets WHERE themeId = :themeId ORDER BY year DESC")
    fun getPagedLegoSetsByTheme(themeId: Int): DataSource.Factory<Int,LegoSet>

    @Query("SELECT * FROM sets ORDER BY year DESC")
    fun getPagedLegoSets(): DataSource.Factory<Int, LegoSet>

    @Query("SELECT * FROM sets WHERE id = :id")
    fun getLegoSet(id: String): LiveData<LegoSet>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(legoSets: List<LegoSet>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(legoSet: LegoSet)
}
