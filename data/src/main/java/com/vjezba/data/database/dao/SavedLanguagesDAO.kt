/*
 * Copyright 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vjezba.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.vjezba.data.database.model.SavedAndAllLanguagesDb
import com.vjezba.data.database.model.SavedLanguagesDb

/**
 * The Data Access Object for the [GardenPlanting] class.
 */
@Dao
interface SavedLanguagesDAO {
    @Query("SELECT * FROM saved_languages")
    fun getSavedLanguagesTest(): LiveData<List<SavedLanguagesDb>>

    @Query("SELECT EXISTS(SELECT 1 FROM saved_languages WHERE language_id = :languageId LIMIT 1)")
    fun isLanguageSaved(languageId: Int): LiveData<Boolean>

    /**
     * This query will tell Room to query both the [Plant] and [GardenPlanting] tables and handle
     * the object mapping.
     */
    @Transaction
    @Query("SELECT * FROM languages WHERE id IN (SELECT DISTINCT(language_id) FROM saved_languages)")
    fun getSavedLanguages(): LiveData<List<SavedAndAllLanguagesDb>>

    @Insert
    suspend fun insertSavedLanguage(savedLanguageDb: SavedLanguagesDb): Long

    @Query("DELETE FROM saved_languages WHERE language_id = :languagedId")
    suspend fun deleteSavedLanguage(languagedId: Int)

    @Query("DELETE FROM saved_languages")
    suspend fun deleteAllSavedProgrammingLanguages()
}
