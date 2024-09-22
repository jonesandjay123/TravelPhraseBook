package com.jonesandjay123.travelphrasebook

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class MainViewModel(private val sentenceDao: SentenceDao) : ViewModel() {
    val sentences = mutableStateListOf<Sentence>()

    init {
        viewModelScope.launch {
            val sentenceList = sentenceDao.getAllSentences()
            sentences.addAll(sentenceList)
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
}