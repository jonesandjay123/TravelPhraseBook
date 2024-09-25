package com.jonesandjay123.travelphrasebook

class FakeSentenceDao : SentenceDao {
    override suspend fun getAllSentences(): List<Sentence> {
        return listOf(
            Sentence(id = 1, chineseText = "你好", thaiText = "สวัสดี", japaneseText = "こんにちは"),
            Sentence(id = 2, chineseText = "谢谢", thaiText = "ขอบคุณ", japaneseText = "ありがとうございます")
        )
    }

    override suspend fun insertSentence(sentence: Sentence): Long {
        return 0L
    }

    override suspend fun updateSentence(sentence: Sentence) {
    }

    override suspend fun deleteSentence(sentence: Sentence) {
    }

    override suspend fun deleteAllSentences() {
        TODO("Not yet implemented")
    }
}