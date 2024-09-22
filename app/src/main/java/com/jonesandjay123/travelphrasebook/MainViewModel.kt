package com.jonesandjay123.travelphrasebook

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val sentenceDao: SentenceDao) : ViewModel() {
    val sentences = mutableStateListOf<Sentence>()

    init {
        viewModelScope.launch {
            val sentenceList = withContext(Dispatchers.IO) {
                sentenceDao.getAllSentences()
            }
            if (sentenceList.isEmpty()) {
                // 添加测试数据
                val testSentences = listOf(
                    Sentence(chineseText = "你好", thaiText = "สวัสดี", japaneseText = "こんにちは"),
                    Sentence(chineseText = "謝謝", thaiText = "ขอบคุณ", japaneseText = "ありがとうございます")
                )
                testSentences.forEach { sentence ->
                    sentenceDao.insertSentence(sentence)
                }
                sentences.addAll(testSentences)
            } else {
                sentences.addAll(sentenceList)
            }
        }
    }

    fun addSentence(sentence: Sentence) {
        viewModelScope.launch {
            val id = sentenceDao.insertSentence(sentence)
            sentence.id = id.toInt() // 假設 id 是 Long，Sentence.id 是 Int
            sentences.add(sentence)
        }
    }

    fun updateSentence(sentence: Sentence) {
        viewModelScope.launch {
            sentenceDao.updateSentence(sentence)
            // 根據需要更新本地列表中的句子
        }
    }

    fun deleteSentence(sentence: Sentence) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                sentenceDao.deleteSentence(sentence)
            }
            sentences.remove(sentence)
        }
    }
}