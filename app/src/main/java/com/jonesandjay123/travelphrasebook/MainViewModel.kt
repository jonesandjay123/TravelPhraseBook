package com.jonesandjay123.travelphrasebook

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val sentenceDao: SentenceDao, application: Application) : AndroidViewModel(application) {
    val sentences = mutableStateListOf<Sentence>()

    var isLoading by mutableStateOf(true)
        private set

    private val sharedPreferences = application.getSharedPreferences("sentence_prefs", Context.MODE_PRIVATE)

    init {
        viewModelScope.launch {
            //  從數據庫加載數據
            val sentenceList = withContext(Dispatchers.IO) {
                sentenceDao.getAllSentences()
            }
            // 從 SharedPreferences 中讀取保存的順序
            val orderString = sharedPreferences.getString("sentence_order", null)
            val orderedList = if (!orderString.isNullOrEmpty()) {
                val orderIds = orderString.split(",").mapNotNull { it.toIntOrNull() }
                val sentenceMap = sentenceList.associateBy { it.id }
                orderIds.mapNotNull { sentenceMap[it] } + sentenceList.filter { it.id !in orderIds }
            } else {
                sentenceList
            }

            sentences.addAll(orderedList)
            // 數據加載完畢後，將 isLoading 設置回 false
            isLoading = false
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
            withContext(Dispatchers.IO) {
                sentenceDao.updateSentence(sentence)
            }
            // 更新本地列表中的句子
            val index = sentences.indexOfFirst { it.id == sentence.id }
            if (index != -1) {
                sentences[index] = sentence
            }
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