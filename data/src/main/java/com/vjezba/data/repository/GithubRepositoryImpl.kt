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

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.vjezba.data.database.AppDatabase
import com.vjezba.data.database.mapper.DbMapper
import com.vjezba.data.networking.GithubRepositoryApi
import com.vjezba.domain.model.RepositoryDetailsResponse
import com.vjezba.domain.repository.GithubRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * RepositoryResponseApi module for handling data operations.
 */
class GithubRepositoryImpl  constructor(
    private val db: AppDatabase,
    private val service: GithubRepositoryApi,
    private val dbMapper: DbMapper?)
    : GithubRepository   {

    override fun getSearchRepositoriesResultStream(query: String): Flow<PagingData<RepositoryDetailsResponse>> {
        return Pager(
            config = PagingConfig(enablePlaceholders = false, pageSize = NETWORK_PAGE_SIZE),
            pagingSourceFactory = { GithubRepositorySource(service, query) }
        ).flow.map { dbMapper!!.mapApiResponseGithubToDomainGithub(it) }
    }

    override fun getSearchRepositoriesWithMediatorAndPaggingData(query: String): Flow<PagingData<RepositoryDetailsResponse>> {

        // appending '%' so we can allow other characters to be before and after the query string
        //val dbQuery = "%${query.replace(' ', '%')}%"
        val pagingSourceFactory = { db.languageReposDAO().getLanguageRepoWithRemoteMediatorAndPagging(query) }

        val finalQuery = "language:" + query
        val repositoryDetailsResponse = Pager(
            config = PagingConfig(NETWORK_PAGE_SIZE, enablePlaceholders = false),
            remoteMediator = PageKeyedRemoteMediator(db, service, finalQuery, dbMapper!!),
            pagingSourceFactory = pagingSourceFactory
        ).flow

        return repositoryDetailsResponse.map { dbMapper.mapPagingRepositoryDetailsResponseDbToPagingRepositoryDetailsResponse(it) }
    }

    companion object {
        private const val NETWORK_PAGE_SIZE = 50
    }


}
