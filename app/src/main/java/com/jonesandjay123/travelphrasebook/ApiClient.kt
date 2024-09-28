package com.jonesandjay123.travelphrasebook

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private const val BASE_URL = "https://translation.googleapis.com/"

    private val client: OkHttpClient by lazy {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()
    }

    val translationService: TranslationService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client) // 添加 OkHttpClient
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(TranslationService::class.java)
    }
}