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

package com.vjezba.androidjetpackgithub.viewmodels

import com.vjezba.domain.model.SavedAndAllLanguages
import java.text.SimpleDateFormat
import java.util.Locale

class SavedAndAllLanguagesViewModel(languages: SavedAndAllLanguages) {
    private val languages = checkNotNull(languages.languages)
    private val savedLanguages = checkNotNull(languages.savedLanguages[0])

    val lastVisitedLanguageDate: String = dateFormat.format(savedLanguages.lastVisitedLanguageDate.time)
    /*val createdAt
        get() = domainSavedLanguages.savedLanguageId.toInt()*/
    val imageUrl
        get() = languages.imageUrl
    val languageName
        get() = languages.name

    val savedLanguageDate: String = dateFormat.format(savedLanguages.savedLanguageDate.time)
    val languagesId
        get() = languages.languageId

    companion object {
        private val dateFormat = SimpleDateFormat("MMM d, yyyy", Locale.US)
    }
}
