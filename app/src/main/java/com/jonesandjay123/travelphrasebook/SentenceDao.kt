package com.jonesandjay123.travelphrasebook

import androidx.room.*

@Dao
interface SentenceDao {
    @Query("SELECT * FROM sentences ORDER BY id ASC")
    suspend fun getAllSentences(): List<Sentence>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSentence(sentence: Sentence): Long

    @Update
    suspend fun updateSentence(sentence: Sentence)

    @Delete
    suspend fun deleteSentence(sentence: Sentence)

    @Query("DELETE FROM sentences")
    suspend fun deleteAllSentences()
}