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

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.vjezba.data.di.GithubNetwork
import com.vjezba.domain.repository.LanguagesRepository

/**
 * The ViewModel for [SavedLanguagesFragment].
 */
class LanguagesListViewModel @ViewModelInject internal constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
    @GithubNetwork languagesRepository: LanguagesRepository
) : ViewModel() {

    val languages  = getSavedOnlyMobilProgrammingLanguages().switchMap {
        if (it == NO_MOBILE_PROGRAMMING_LANGUAGE_FILTER) {
            //val d =languagesRepository.getLanguages()
            //print("Data is: " + d)
            languagesRepository.getLanguages()
        } else {
            //val d = languagesRepository.getOnlyMobilleProgrammingLanguages(it)
            //print("Data is: " + d)

            languagesRepository.getOnlyMobilleProgrammingLanguages(it)
        }
    }

    /*@OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun onPause() {
        // This is just to demo onPaused is called.
        // To save, we don't need to `set`. It is just same as set directly to textLiveData.value
        // Refer https://medium.com/@elye.project/unintuitive-livedata-savedstatehandle-3d01fbdbfc01
        savedStateHandle.set(KEY, "From onPaused")
    }*/

    fun setOnlyMobileProgrammingLanguages(selectOnlyMobileLanguages: String) {
        savedStateHandle.set(MOBILE_PROGRAMMING_LANGUAGES_KEY, selectOnlyMobileLanguages)
    }

    fun clearFilterForMobileProgrammingLanguages() {
        savedStateHandle.set(MOBILE_PROGRAMMING_LANGUAGES_KEY, NO_MOBILE_PROGRAMMING_LANGUAGE_FILTER)
    }

    fun isFiltered() = getSavedOnlyMobilProgrammingLanguages().value != NO_MOBILE_PROGRAMMING_LANGUAGE_FILTER

    private fun getSavedOnlyMobilProgrammingLanguages(): MutableLiveData<String> {
        return savedStateHandle.getLiveData(MOBILE_PROGRAMMING_LANGUAGES_KEY, NO_MOBILE_PROGRAMMING_LANGUAGE_FILTER)
    }

    companion object {
        private const val NO_MOBILE_PROGRAMMING_LANGUAGE_FILTER = ""
        private const val MOBILE_PROGRAMMING_LANGUAGES_KEY = "MOBILE_PROGRAMMING_LANGUAGES_KEY"
    }
}
