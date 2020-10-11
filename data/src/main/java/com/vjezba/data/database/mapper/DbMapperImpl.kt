/*
 * Copyright (c) 2020 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * Notwithstanding the foregoing, you may not use, copy, modify, merge, publish,
 * distribute, sublicense, create a derivative work, and/or sell copies of the
 * Software in any work that is designed, intended, or marketed for pedagogical or
 * instructional purposes related to programming, coding, application development,
 * or information technology.  Permission for such use, copying, modification,
 * merger, publication, distribution, sublicensing, creation of derivative works,
 * or sale is expressly withheld.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.vjezba.data.database.mapper

import androidx.paging.PagingData
import androidx.paging.map
import com.vjezba.data.database.model.LanguagesRepoDb
import com.vjezba.data.database.model.SavedAndAllLanguagesDb
import com.vjezba.data.networking.model.RepositoryDetailsResponseApi
import com.vjezba.domain.model.*
import kotlin.collections.map

class DbMapperImpl : DbMapper {


    override fun mapDbLanguagesToDomainLanguages(languages: List<com.vjezba.data.database.model.LanguagesDb>): List<Languages> {
        return languages.map {
            with(it) {
                Languages(
                    languageId,
                    name,
                    description,
                    createdBy,
                    createdAt,
                    typeLanguage,
                    imageUrl
                )
            }
        }
    }

    override fun mapDbLanguageToDomainLanguage(language: com.vjezba.data.database.model.LanguagesDb): Languages {
        return with(language) {
            Languages(
                languageId,
                name,
                description,
                createdBy,
                createdAt,
                typeLanguage,
                imageUrl
            )
        }
    }

    override fun mapDbSavedLanguagesToDomainSavedLanguages(listLanguages: List<SavedAndAllLanguagesDb>): List<com.vjezba.domain.model.SavedAndAllLanguages> {
        return listLanguages.map {
            with(it) {
                com.vjezba.domain.model.SavedAndAllLanguages(
                    Languages(languagesDb.languageId, languagesDb.name, languagesDb.description, languagesDb.createdBy, languagesDb.createdAt, languagesDb.typeLanguage, languagesDb.imageUrl),
                    savedLanguageDbs.map {
                        with(it) {
                            SavedLanguages(
                                languagesId, savedLanguageDate, lastVisitedLanguageDate
                            )
                        }
                    }
                )
            }
        }
    }

    override fun mapApiResponseGithubToDomainGithub(responseApi: PagingData<RepositoryDetailsResponseApi>): PagingData<RepositoryDetailsResponse> {
        return responseApi.map {
            with(it) {
                RepositoryDetailsResponse(
                    id,
                    RepositoryOwnerResponse(ownerApi.avatarUrl),
                    name,
                    description,
                    html_url
                )
            }
        }
    }

    override fun mapApiResponseGithubToGithubDb(responseApi: List<RepositoryDetailsResponseApi>): List<LanguagesRepoDb> {
        return responseApi.map {
            with(it) {
                LanguagesRepoDb(
                    id,
                    ownerApi.avatarUrl,
                    name,
                    description,
                    html_url,
                    language,
                    stars,
                    forks
                )
            }
        }
    }

    override fun mapPagingRepositoryDetailsResponseDbToPagingRepositoryDetailsResponse(responseApi: PagingData<LanguagesRepoDb>): PagingData<RepositoryDetailsResponse> {
        return responseApi.map {
            with(it) {
                RepositoryDetailsResponse(
                    id,
                    RepositoryOwnerResponse(avatarUrl),
                    name,
                    description,
                    html_url,
                    language,
                    stars,
                    forks
                )
            }
        }
    }

}
