package com.jonesandjay123.travelphrasebook

import android.app.Application
import android.content.Context
import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val sentenceDao: SentenceDao, application: Application) : AndroidViewModel(application) {
    val sentences = mutableStateListOf<Sentence>()
    private val sharedPreferences = application.getSharedPreferences("sentence_prefs", Context.MODE_PRIVATE)

    init {
        viewModelScope.launch {
            val sentenceList = withContext(Dispatchers.IO) {
                sentenceDao.getAllSentences()
            }
            // 从 SharedPreferences 中读取保存的顺序
            val orderString = sharedPreferences.getString("sentence_order", null)
            val orderedList = if (!orderString.isNullOrEmpty()) {
                val orderIds = orderString.split(",").mapNotNull { it.toIntOrNull() }
                val sentenceMap = sentenceList.associateBy { it.id }
                orderIds.mapNotNull { sentenceMap[it] } + sentenceList.filter { it.id !in orderIds }
            } else {
                sentenceList
            }

            sentences.addAll(orderedList)
        }
    }

    // 保存排序顺序的方法
    private fun saveSentenceOrder() {
        val orderList = sentences.map { it.id }
        val orderString = orderList.joinToString(",")
        sharedPreferences.edit().putString("sentence_order", orderString).apply()
    }

    // 在句子顺序发生变化时调用
    fun onSentenceOrderChanged() {
        saveSentenceOrder()
    }

    fun addSentence(sentence: Sentence) {
        viewModelScope.launch {
            val newId = withContext(Dispatchers.IO) {
                sentenceDao.insertSentence(sentence)
            }
            sentence.id = newId.toInt()
            sentences.add(sentence)
            saveSentenceOrder() // 保存排序顺序
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
            saveSentenceOrder() // 保存排序顺序
        }
    }

    fun importSentences(jsonText: String) {
        // 解析 JSON，更新数据库
        // 注意需要在 IO 线程中执行数据库操作
    }

    fun exportSentences(): String {
        // 获取当前的句子列表，序列化为 JSON 字符串并返回
        return ""
    }

    fun exportSentencesWithPrompt(): String {
        // 获取当前的句子列表，添加 prompt，序列化为 JSON 字符串并返回
        return ""
    }
}