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

package com.vjezba.data.di

import androidx.room.TypeConverter
import com.vjezba.data.database.AppDatabase
import com.vjezba.data.database.dao.LanguagesDao
import com.vjezba.data.database.dao.SavedLanguagesDAO
import com.vjezba.data.database.mapper.DbMapper
import com.vjezba.data.networking.GithubRepositoryApi
import com.vjezba.data.repository.GithubRepositoryImpl
import com.vjezba.data.repository.LanguagesRepositoryImpl
import com.vjezba.data.repository.SavedLanguagesRepositoryImpl
import com.vjezba.domain.repository.GithubRepository
import com.vjezba.domain.repository.LanguagesRepository
import com.vjezba.domain.repository.SavedLanguagesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ApplicationComponent
import java.util.Calendar

/**
 * Type converters to allow Room to reference complex data types.
 */
@Module
@InstallIn(ActivityComponent::class)
class RepositoryModule {

    @Provides
    @GithubNetwork
    fun provideAllLanguagesFromRestApiNetwork( appDatabase: AppDatabase,
                                               @GithubNetwork githubRepositoryApi: GithubRepositoryApi,
                                               dbMapper : DbMapper) : GithubRepository {
        return GithubRepositoryImpl(appDatabase, githubRepositoryApi, dbMapper)
    }

    @Provides
    @GithubNetwork
    fun provideGetAllSavedLanguages( savedLanguageDao : SavedLanguagesDAO, dbMapper : DbMapper) : SavedLanguagesRepository {
        return SavedLanguagesRepositoryImpl(savedLanguageDao, dbMapper)
    }

    @Provides
    @GithubNetwork
    fun provideGetAllLanguages(
        languagesDao : LanguagesDao,
        dbMapper : DbMapper) : LanguagesRepository {
        return LanguagesRepositoryImpl(languagesDao, dbMapper)
    }
}
