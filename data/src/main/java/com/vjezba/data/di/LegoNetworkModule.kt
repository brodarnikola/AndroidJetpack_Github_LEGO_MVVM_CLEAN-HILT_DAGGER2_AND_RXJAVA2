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

package com.vjezba.data.di

import com.google.gson.Gson
import com.vjezba.data.BuildConfig
import com.vjezba.data.database.AppDatabase
import com.vjezba.data.lego.api.AuthInterceptor
import com.vjezba.data.lego.api.LegoService
import com.vjezba.data.lego.repository.LegoSetRemoteDataSource
import com.vjezba.data.lego.repository.LegoThemeRemoteDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@InstallIn(ApplicationComponent::class)
@Module
class LegoNetworkModule {


    @Provides
    @LegoNetwork
    fun provideLoggingInterceptor() =
        HttpLoggingInterceptor().apply { level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE }

    /*@Provides
    @Singleton
    @LegoNetwork
    fun provideOkHttpClient(@LegoNetwork interceptor: HttpLoggingInterceptor): OkHttpClient =
         OkHttpClient.Builder().addInterceptor(interceptor)
             .addNetworkInterceptor(StethoInterceptor())
             .build()*/

    @Provides
    @Singleton
    @LegoNetwork
    fun provideTestOkHttpClient(@LegoNetwork interceptor: HttpLoggingInterceptor): OkHttpClient {
        val okHttpClient = OkHttpClient()
        return okHttpClient.newBuilder() .addInterceptor(
            AuthInterceptor(
                "9bb7cbfb4e7551e06d2c743ad53f1a5a"
                //BuildConfig.API_DEVELOPER_TOKEN
            )
        ).build()
    }

    /*@Provides
    @Singleton
    @LegoNetwork
    fun providePrivateOkHttpClient(
        @LegoNetwork upstreamClient: OkHttpClient
    ): OkHttpClient {
        return upstreamClient.newBuilder()
            .addInterceptor(
                AuthInterceptor(
                    "9bb7cbfb4e7551e06d2c743ad53f1a5a"
                    //BuildConfig.API_DEVELOPER_TOKEN
                )
            ).build()
    }*/

    @Provides
    @Singleton
    @LegoNetwork
    fun provideGson(): Gson = Gson()

    @Provides
    @Singleton
    @LegoNetwork
    fun provideGsonConverterFactory( @LegoNetwork gson: Gson): GsonConverterFactory =
        GsonConverterFactory.create(gson)




    @Singleton
    @Provides
    @LegoNetwork
    fun provideLegoRetrofit(
        @LegoNetwork gson:  Gson,
        @LegoNetwork  client: OkHttpClient): Retrofit.Builder {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(LegoService.ENDPOINT)
            .addConverterFactory(GsonConverterFactory.create(gson))
    }

    @Singleton
    @Provides
    @LegoNetwork
    fun provideLegoService(
        @LegoNetwork retrofit: Retrofit.Builder): LegoService {
        return retrofit
            .build()
            .create(LegoService::class.java)
    }













    @Singleton
    @Provides
    @LegoNetwork
    fun provideLegoThemeRemoteDataSource(
        @LegoNetwork legoService: LegoService)
            =
        LegoThemeRemoteDataSource(legoService)

    @Singleton
    @Provides
    @LegoNetwork
    fun provideLegoSetRemoteDataSource(
        @LegoNetwork legoService: LegoService)
            =
        LegoSetRemoteDataSource(legoService)

    @Singleton
    @Provides
    @LegoNetwork
    fun provideLegoSetDao(db: AppDatabase) = db.legoSetDao()


    @Singleton
    @Provides
    @LegoNetwork
    fun provideLegoThemeDao(db: AppDatabase) = db.legoThemeDao()

    @CoroutineScropeIO
    @Provides
    fun provideCoroutineScopeIO() = CoroutineScope(Dispatchers.IO)









    /*@Singleton
    @Provides
    @LegoNetwork
    fun provideLegoService(@LegoNetwork okhttpClient: OkHttpClient,
                           @LegoNetwork converterFactory: GsonConverterFactory
    ) = provideService(okhttpClient, converterFactory, LegoService::class.java)


    @Singleton
    @Provides
    @LegoNetwork
    fun provideLegoServiceRestApi(
        @LegoNetwork retrofit: Retrofit.Builder): LegoService {
        return retrofit
            .build()
            .create(LegoService::class.java)
    }

    private fun createRetrofit(
        @LegoNetwork okhttpClient: OkHttpClient,
        @LegoNetwork converterFactory: GsonConverterFactory
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(LegoService.ENDPOINT)
            .client(okhttpClient)
            .addConverterFactory(converterFactory)
            .build()
    }

    private fun <T> provideService(@LegoNetwork okhttpClient: OkHttpClient,
                                   @LegoNetwork converterFactory: GsonConverterFactory,
                                   @LegoNetwork clazz: Class<T>): T {
        return createRetrofit(okhttpClient, converterFactory).create(clazz)
    }
*/




}
