package com.vjezba.data.lego.api

import com.vjezba.data.di.LegoNetwork
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import kotlin.jvm.Throws

/**
 * A {@see RequestInterceptor} that adds an auth token to requests
 */
@LegoNetwork
class AuthInterceptor  ( @LegoNetwork private val accessToken: String) : Interceptor {
    @LegoNetwork
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder().addHeader(
                "Authorization", "key $accessToken").build()
        return chain.proceed(request)
    }
}
