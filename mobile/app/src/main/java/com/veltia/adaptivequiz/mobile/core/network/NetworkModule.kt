package com.veltia.adaptivequiz.mobile.core.network

import com.veltia.adaptivequiz.mobile.BuildConfig
import com.veltia.adaptivequiz.mobile.data.remote.AdaptiveQuizApi
import com.veltia.adaptivequiz.mobile.data.repository.AdaptiveQuizRepositoryImpl
import com.veltia.adaptivequiz.mobile.domain.repository.AdaptiveQuizRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object NetworkModule {
    fun repository(): AdaptiveQuizRepository {
        val logging = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BASIC else HttpLoggingInterceptor.Level.NONE
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build()
        val api = Retrofit.Builder()
            .baseUrl(BuildConfig.API_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AdaptiveQuizApi::class.java)
        return AdaptiveQuizRepositoryImpl(api)
    }
}
