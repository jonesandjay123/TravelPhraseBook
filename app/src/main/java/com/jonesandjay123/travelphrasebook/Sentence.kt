package com.jonesandjay123.travelphrasebook

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sentences")
data class Sentence(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val chineseText: String,
    var englishText: String? = null,
    var japaneseText: String? = null,
    var thaiText: String? = null
)