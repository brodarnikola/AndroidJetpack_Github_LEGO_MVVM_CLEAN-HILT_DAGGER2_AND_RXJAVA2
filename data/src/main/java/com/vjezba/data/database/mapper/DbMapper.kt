package com.vjezba.data.database.mapper

import androidx.paging.PagingData
import com.vjezba.data.database.model.LanguagesDb
import com.vjezba.data.database.model.LanguagesRepoDb
import com.vjezba.data.database.model.SavedAndAllLanguagesDb
import com.vjezba.data.networking.model.RepositoryDetailsResponseApi
import com.vjezba.domain.model.*

interface DbMapper {

    fun mapDbLanguagesToDomainLanguages(languages: List<LanguagesDb>): List<Languages>

    fun mapDbLanguageToDomainLanguage(language: LanguagesDb): Languages

    fun mapDbSavedLanguagesToDomainSavedLanguages(language: List<SavedAndAllLanguagesDb>): List<SavedAndAllLanguages>


    fun mapApiResponseGithubToDomainGithub(responseApi: PagingData<RepositoryDetailsResponseApi>): PagingData<RepositoryDetailsResponse>


    fun mapApiResponseGithubToGithubDb(responseApi: List<RepositoryDetailsResponseApi>): List<LanguagesRepoDb>


    fun mapPagingRepositoryDetailsResponseDbToPagingRepositoryDetailsResponse(responseApi: PagingData<LanguagesRepoDb>): PagingData<RepositoryDetailsResponse>

}