package com.jonesandjay123.travelphrasebook

import com.google.gson.annotations.SerializedName

data class TranslationRequest(
    @SerializedName("q")
    val q: List<String>,
    @SerializedName("target")
    val target: String,
    @SerializedName("source")
    val source: String? = null,
    @SerializedName("format")
    val format: String = "text"
)