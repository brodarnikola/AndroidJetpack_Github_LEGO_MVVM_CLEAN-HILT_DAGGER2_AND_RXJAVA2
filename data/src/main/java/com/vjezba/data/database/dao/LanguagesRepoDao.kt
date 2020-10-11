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

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.vjezba.data.database.model.LanguagesRepoDb

/**
 * The Data Access Object for the Plant class.
 */
@Dao
interface LanguagesRepoDao {

    @Query("SELECT * FROM github_repositories WHERE language = :language ORDER BY stars DESC")
    fun getLanguageRepoWithRemoteMediatorAndPagging(language: String): PagingSource<Int, LanguagesRepoDb>

    /*@Query("DELETE FROM github_repositories WHERE name = :subreddit")
    suspend fun deleteGithubRepositories(subreddit: String)*/

    @Query("DELETE FROM github_repositories ")
    suspend fun deleteGithubRepositoriesWithoutParameter()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(languages: List<LanguagesRepoDb>)
}
