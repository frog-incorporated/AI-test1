package com.hexo.core.network

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

/**
 * A simple singleton for creating and providing a Retrofit instance.
 *
 * This client is configured with a default base URL. Individual modules
 * should specify full URLs on their service interfaces if they target
 * different endpoints (e.g. local Ollama host). The OkHttpClient can be
 * customized with interceptors if needed.
 */
object RetrofitClient {
    private const val DEFAULT_BASE_URL = "http://localhost/" // Placeholder; real services set full URLs

    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .build()
    }

    /**
     * Lazily initialised Retrofit instance. Most calls will override the
     * endpoint path in their service interfaces.
     */
    val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(DEFAULT_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }
}