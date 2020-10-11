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

import androidx.lifecycle.map
import com.vjezba.data.database.dao.SavedLanguagesDAO
import com.vjezba.data.database.mapper.DbMapper
import com.vjezba.data.database.model.SavedLanguagesDb
import com.vjezba.domain.model.SavedAndAllLanguages
import com.vjezba.domain.model.SavedLanguages
import com.vjezba.domain.repository.SavedLanguagesRepository

class SavedLanguagesRepositoryImpl constructor(
    private val savedLanguagesDAO: SavedLanguagesDAO,
    private val dbMapper: DbMapper
) : SavedLanguagesRepository {

    override suspend fun createSavedLanguage(savedLanguageId: Int) {
        val savedLanguage = SavedLanguagesDb(savedLanguageId)
        savedLanguagesDAO.insertSavedLanguage(savedLanguage)
    }

    override suspend fun deleteAllSavedProgrammingLanguages() {
        return savedLanguagesDAO.deleteAllSavedProgrammingLanguages()
    }

    override suspend fun deleteSelectedProgrammingLanguage(languagedId: Int) {
        savedLanguagesDAO.deleteSavedLanguage( languagedId )
    }

    override fun isLanguageSaved(languageId: Int) =
        savedLanguagesDAO.isLanguageSaved(languageId)

    override fun getSavedLanguages() = savedLanguagesDAO.getSavedLanguages().map { dbMapper.mapDbSavedLanguagesToDomainSavedLanguages(it) }

}
