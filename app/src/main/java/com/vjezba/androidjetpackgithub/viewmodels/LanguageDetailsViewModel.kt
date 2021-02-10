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

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import com.vjezba.androidjetpackgithub.BuildConfig
import com.vjezba.data.di.GithubNetwork
import com.vjezba.domain.repository.LanguagesRepository
import com.vjezba.domain.repository.SavedLanguagesRepository
import kotlinx.coroutines.launch

/**
 * The ViewModel used in [LanguageDetailFragment].
 */
class LanguageDetailsViewModel @AssistedInject constructor(
    @GithubNetwork languageRepository: LanguagesRepository,
    @GithubNetwork private val savedLanguagesRepository: SavedLanguagesRepository,
    @Assisted private val languagesId: Int
) : ViewModel() {

    val isSavedLanguage = savedLanguagesRepository.isLanguageSaved(languagesId)
    val languageDetails = languageRepository.getLanguage(languagesId)

    fun saveProgrammingLanguage() {
        viewModelScope.launch {
            savedLanguagesRepository.createSavedLanguage(languagesId)
        }
    }

    fun hasValidUnsplashKey() = (BuildConfig.UNSPLASH_ACCESS_KEY != "null")

    @AssistedInject.Factory
    interface AssistedFactory {
        fun create(languagesId: Int): LanguageDetailsViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: AssistedFactory,
            languagesId: Int
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return assistedFactory.create(languagesId) as T
            }
        }
    }
}
