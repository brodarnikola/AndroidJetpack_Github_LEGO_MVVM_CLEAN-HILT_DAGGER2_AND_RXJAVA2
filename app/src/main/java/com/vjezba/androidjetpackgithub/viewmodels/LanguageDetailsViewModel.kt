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

import androidx.lifecycle.*
import com.vjezba.androidjetpackgithub.BuildConfig
import com.vjezba.data.di.GithubNetwork
import com.vjezba.domain.model.Languages
import com.vjezba.domain.repository.LanguagesRepository
import com.vjezba.domain.repository.SavedLanguagesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * The ViewModel used in [LanguageDetailFragment].
 */

@HiltViewModel
class LanguageDetailsViewModel @Inject constructor(
    @GithubNetwork val languageRepository: LanguagesRepository,
    @GithubNetwork private val savedLanguagesRepository: SavedLanguagesRepository
) : ViewModel() {

    fun isLanguageSaved(languagesId: Int) : LiveData<Boolean> {
        return savedLanguagesRepository.isLanguageSaved(languagesId)
    }

    fun languageDetails(languagesId: Int) : LiveData<Languages> {
        return languageRepository.getLanguage(languagesId)
    }


    fun saveProgrammingLanguage(languagesId: Int) {
        viewModelScope.launch {
            savedLanguagesRepository.createSavedLanguage(languagesId)
        }
    }

    fun hasValidUnsplashKey() = (BuildConfig.UNSPLASH_ACCESS_KEY != "null")

}
