package com.vjezba.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vjezba.data.database.model.LegoTheme

/**
 * The Data Access Object for the LegoTheme class.
 */
@Dao
interface LegoThemeDao {

    @Query("SELECT * FROM themes ORDER BY id DESC")
    fun getLegoThemes(): LiveData<List<LegoTheme>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(plants: List<LegoTheme>)
}
