package com.vjezba.data.lego.api

import com.vjezba.data.di.lego.LegoNetwork
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import kotlin.jvm.Throws

/**
 * A {@see RequestInterceptor} that adds an auth token to requests
 */

class AuthInterceptor  ( private val accessToken: String) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder().addHeader(
                "Authorization", "key $accessToken").build()
        return chain.proceed(request)
    }
}
