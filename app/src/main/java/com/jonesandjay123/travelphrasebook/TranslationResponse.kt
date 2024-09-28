package com.jonesandjay123.travelphrasebook

import com.google.gson.annotations.SerializedName

data class TranslationResponse(
    @SerializedName("data")
    val data: TranslationData?
)

data class TranslationData(
    @SerializedName("translations")
    val translations: List<TranslationResult>?
)

data class TranslationResult(
    @SerializedName("translatedText")
    val translatedText: String,
    @SerializedName("detectedSourceLanguage")
    val detectedSourceLanguage: String?,
    @SerializedName("model")
    val model: String?
)