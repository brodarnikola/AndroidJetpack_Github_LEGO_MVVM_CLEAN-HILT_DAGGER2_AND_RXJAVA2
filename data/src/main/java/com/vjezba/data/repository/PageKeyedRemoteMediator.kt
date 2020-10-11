package com.vjezba.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.LoadType.*
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.vjezba.data.database.AppDatabase
import com.vjezba.data.database.dao.LanguagesRemoteKeyDao
import com.vjezba.data.database.mapper.DbMapper
import com.vjezba.data.database.model.LanguagesRepoDb
import com.vjezba.data.database.model.LanguagesRemoteKeyDb
import com.vjezba.data.networking.GithubRepositoryApi
import retrofit2.HttpException
import java.io.IOException
import java.io.InvalidObjectException

private const val GITHUB_STARTING_PAGE_INDEX = 1

@OptIn(ExperimentalPagingApi::class)
class PageKeyedRemoteMediator(
    private val db: AppDatabase,
    private val redditApi: GithubRepositoryApi,
    private val subredditName: String,
    private val dbMapper: DbMapper
) : RemoteMediator<Int, LanguagesRepoDb>() {

    private val languageReposDao = db.languageReposDAO()
    private val languagesRemoteKeyDao: LanguagesRemoteKeyDao = db.languageRemoteKeyDAO()

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, LanguagesRepoDb>
    ): MediatorResult {
        try {
            // Get the closest item from PagingState that we want to load data around.
            val page = when (loadType) {
                REFRESH -> { val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: GITHUB_STARTING_PAGE_INDEX }
                PREPEND ->  {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    if (remoteKeys == null) {
                        // The LoadType is PREPEND so some data was loaded before,
                        // so we should have been able to get remote keys
                        // If the remoteKeys are null, then we're an invalid state and we have a bug
                        throw InvalidObjectException("Remote key and the prevKey should not be null")
                    }
                    // If the previous key is null, then we can't request more data
                    val prevKey = remoteKeys.prevKey
                    if (prevKey == null) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }
                    remoteKeys.prevKey
                }
                APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    if (remoteKeys == null || remoteKeys.nextKey == null) {
                        throw InvalidObjectException("Remote key should not be null for $loadType")
                    }
                    remoteKeys.nextKey
                }
            }

            val data = redditApi.searchGithubRepositoryByProgrammingLanguage(
                subredditName,
                page, //loadKey?.toInt() ?: 0,
                state.config.pageSize
                /*limit = when (loadType) {
                    REFRESH -> state.config.initialLoadSize
                    else -> state.config.pageSize
                }*/
            )

            val items = data.items //.children.map { it.data } //.map { it.data }ve

            val endOfPaginationReached = items.isEmpty()

            db.withTransaction {
                // clear all tables in the database
//                if (loadType == REFRESH) {
//                    languageReposDao.deleteGithubRepositoriesWithoutParameter()
//                    languagesRemoteKeyDao.deleteBySubredditWithoutParameter()
//                }
                val prevKey = if (page == GITHUB_STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (endOfPaginationReached) null else page + 1
                val keys = items.map {
                    LanguagesRemoteKeyDb(repoId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                languagesRemoteKeyDao.insertAll(keys)
                val mapList = dbMapper.mapApiResponseGithubToGithubDb(items)
                languageReposDao.insertAll(mapList)
            }

            return MediatorResult.Success(endOfPaginationReached = items.isEmpty())
        } catch (e: IOException) {
            return MediatorResult.Error(e)
        } catch (e: HttpException) {
            return MediatorResult.Error(e)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, LanguagesRepoDb>): LanguagesRemoteKeyDb? {
        // Get the last page that was retrieved, that contained items.
        // From that last page, get the last item
        return state.pages.lastOrNull() { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { repo ->
                // Get the remote keys of the last item retrieved
                db.languageRemoteKeyDAO().remoteKeyByPost(repo.id)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, LanguagesRepoDb>): LanguagesRemoteKeyDb? {
        // Get the first page that was retrieved, that contained items.
        // From that first page, get the first item
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { repo ->
                // Get the remote keys of the first items retrieved
                db.languageRemoteKeyDAO().remoteKeyByPost(repo.id)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(
        state: PagingState<Int, LanguagesRepoDb>
    ): LanguagesRemoteKeyDb? {
        // The paging library is trying to load data after the anchor position
        // Get the item closest to the anchor position
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { repoId ->
                db.languageRemoteKeyDAO().remoteKeyByPost(repoId)
            }
        }
    }


}
