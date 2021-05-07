package com.vjezba.androidjetpackgithub.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vjezba.data.di.GithubNetwork
import com.vjezba.domain.model.SavedLanguages
import com.vjezba.domain.repository.SavedLanguagesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject



@HiltViewModel
class SavedLanguagesListViewModel @Inject internal constructor(
    @GithubNetwork val savedLanguages: SavedLanguagesRepository
) : ViewModel() {
    val savedAndAllLanguages =
        savedLanguages.getSavedLanguages()

    fun deleteSelectedProgrammingLanguage(languagedId: Int) {
        viewModelScope.launch {
            savedLanguages.deleteSelectedProgrammingLanguage(languagedId)
        }
    }

}
