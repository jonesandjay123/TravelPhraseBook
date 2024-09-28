package com.jonesandjay123.travelphrasebook

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query

interface TranslationService {
    @Headers("Content-Type: application/json")
    @POST("language/translate/v2")
    fun translateText(
        @Query("key") apiKey: String,
        @Body request: TranslationRequest
    ): Call<TranslationResponse>
}