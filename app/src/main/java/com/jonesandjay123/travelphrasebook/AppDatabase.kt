package com.jonesandjay123.travelphrasebook

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Sentence::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sentenceDao(): SentenceDao
}
