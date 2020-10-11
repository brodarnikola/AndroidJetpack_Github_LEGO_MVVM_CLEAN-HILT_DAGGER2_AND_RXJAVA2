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

package com.vjezba.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.vjezba.data.database.dao.LanguagesDao
import com.vjezba.data.database.mapper.DbMapper
import com.vjezba.domain.model.Languages
import com.vjezba.domain.repository.LanguagesRepository

/**
 * RepositoryResponseApi module for handling data operations.
 */
class LanguagesRepositoryImpl  constructor(
    private val languages: LanguagesDao, private val dbMapper: DbMapper)
    : LanguagesRepository   {

    override fun getLanguages() = languages.getLanguages().map { dbMapper.mapDbLanguagesToDomainLanguages(it) }

    override fun getOnlyMobilleProgrammingLanguages(onlyMobileProgrammingLanguages: String): LiveData<List<Languages>> {
        return languages.getOnlyMobileProgrammingLanguages(onlyMobileProgrammingLanguages).map { dbMapper.mapDbLanguagesToDomainLanguages(it) }
    }

    override fun getLanguage(languagesId: Int) = languages.getLanguageRepo(languagesId).map { dbMapper.mapDbLanguageToDomainLanguage(it) }

    companion object {

        // For Singleton instantiation
        @Volatile private var instance: LanguagesRepository? = null

        fun getInstance(languages: LanguagesDao, dbMapper: DbMapper) =
            instance ?: synchronized(this) {
                instance ?: LanguagesRepositoryImpl(languages, dbMapper).also { instance = it }
            }
    }
}
