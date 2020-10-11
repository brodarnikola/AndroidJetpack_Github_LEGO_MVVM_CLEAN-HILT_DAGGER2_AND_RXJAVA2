/*
 * Copyright 2020 Google LLC
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

import androidx.paging.PagingSource
import com.vjezba.data.networking.GithubRepositoryApi
import com.vjezba.data.networking.model.RepositoryDetailsResponseApi

private const val GITHUB_REPOSITORY_STARTING_PAGE_INDEX = 1

class GithubRepositorySource(
    private val service: GithubRepositoryApi,
    private val query: String
) : PagingSource<Int, RepositoryDetailsResponseApi>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, RepositoryDetailsResponseApi> {
        val page = params.key ?: GITHUB_REPOSITORY_STARTING_PAGE_INDEX
        return try {
            val response = service.searchGithubRepository(query, page, params.loadSize)
            val repositories = response.items
            LoadResult.Page(
                data = repositories,
                prevKey = if (page == GITHUB_REPOSITORY_STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (page == response.total_count) null else page + 1
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }
}
