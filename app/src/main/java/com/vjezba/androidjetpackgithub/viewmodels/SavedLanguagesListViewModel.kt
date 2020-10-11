package com.vjezba.androidjetpackgithub.viewmodels

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vjezba.domain.model.SavedLanguages
import com.vjezba.domain.repository.SavedLanguagesRepository
import kotlinx.coroutines.launch

class SavedLanguagesListViewModel @ViewModelInject internal constructor(
    val savedLanguages: SavedLanguagesRepository
) : ViewModel() {
    val savedAndAllLanguages =
        savedLanguages.getSavedLanguages()

    fun deleteSelectedProgrammingLanguage(languagedId: Int) {
        viewModelScope.launch {
            savedLanguages.deleteSelectedProgrammingLanguage(languagedId)
        }
    }

}
