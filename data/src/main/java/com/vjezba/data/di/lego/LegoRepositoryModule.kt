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

package com.vjezba.data.di.lego

import com.vjezba.data.database.dao.LegoSetDao
import com.vjezba.data.database.dao.LegoThemeDao
import com.vjezba.data.lego.api.LegoService
import com.vjezba.data.lego.repository.LegoSetRemoteDataSource
import com.vjezba.data.lego.repository.LegoSetRepository
import com.vjezba.data.lego.repository.LegoThemeRemoteDataSource
import com.vjezba.data.lego.repository.LegoThemeRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

/**
 * Type converters to allow Room to reference complex data types.
 */
@Module
@InstallIn(ActivityComponent::class)
class LegoRepositoryModule {

    @Provides
    @LegoNetwork
    fun provideLegoThemeRemoteDataSource(
        @LegoNetwork legoService: LegoService
    )
            =
        LegoThemeRemoteDataSource(legoService)

    @Provides
    @LegoNetwork
    fun provideLegoSetRemoteDataSource(
        @LegoNetwork legoService: LegoService
    )
            =
        LegoSetRemoteDataSource(legoService)

    @CoroutineScropeIO
    @Provides
    fun provideCoroutineScopeIO() = CoroutineScope(Dispatchers.IO)

    @Provides
    @LegoNetwork
    fun provideLegoThemeRepository(
        dao: LegoThemeDao,
        @LegoNetwork remoteSource: LegoThemeRemoteDataSource
    )
            =
        LegoThemeRepository(dao, remoteSource)

    @Provides
    @LegoNetwork
    fun provideLegoSetRepository(
        dao: LegoSetDao,
        @LegoNetwork legoSetRemoteDataSource: LegoSetRemoteDataSource
    )
            =
        LegoSetRepository(dao, legoSetRemoteDataSource)

}
