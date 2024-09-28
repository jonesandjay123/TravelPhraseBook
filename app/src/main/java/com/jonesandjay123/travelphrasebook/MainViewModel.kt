package com.jonesandjay123.travelphrasebook

import android.app.Application
import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(private val sentenceDao: SentenceDao, application: Application) : AndroidViewModel(application) {

    private val apiKey = BuildConfig.API_KEY

    var isTranslating by mutableStateOf(false)
        private set

    val sentences = mutableStateListOf<Sentence>()

    var isLoading by mutableStateOf(true)
        private set

    private val sharedPreferences = application.getSharedPreferences("sentence_prefs", Context.MODE_PRIVATE)

    init {
        viewModelScope.launch {
            //  從數據庫加載數據
            val sentenceList = sentenceDao.getAllSentences()
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

    fun translateSentencesWithApi() {
        viewModelScope.launch {
            isTranslating = true
            withContext(Dispatchers.IO) {
                try {
                    val sentencesToTranslate = sentences.map { it.chineseText }

                    // 定義目標語言列表
                    val targetLanguages = listOf("en", "ja", "th")

                    for (targetLang in targetLanguages) {
                        val request = TranslationRequest(
                            q = sentencesToTranslate,
                            target = targetLang
                        )
                        val response = ApiClient.translationService.translateText(apiKey, request).execute()
                        if (response.isSuccessful) {
                            val translationResponse = response.body()
                            val translations = translationResponse?.data?.translations
                            if (translations != null && translations.size == sentences.size) {
                                // 更新句子的翻譯
                                for (i in sentences.indices) {
                                    when (targetLang) {
                                        "en" -> sentences[i].englishText = translations[i].translatedText
                                        "ja" -> sentences[i].japaneseText = translations[i].translatedText
                                        "th" -> sentences[i].thaiText = translations[i].translatedText
                                    }
                                    // 更新數據庫
                                    sentenceDao.updateSentence(sentences[i])
                                }
                            } else {
                                // 處理錯誤：翻譯數量不匹配
                                withContext(Dispatchers.Main) {
                                    Toast.makeText(getApplication(), "翻譯結果數量不匹配", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            // 處理錯誤：響應不成功
                            withContext(Dispatchers.Main) {
                                Toast.makeText(getApplication(), "翻譯失敗：${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } catch (e: Exception) {
                    // 處理異常
                    withContext(Dispatchers.Main) {
                        Toast.makeText(getApplication(), "翻譯過程中發生錯誤：${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            isTranslating = false
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
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                try {
                    val gson = Gson()
                    val type = object : com.google.gson.reflect.TypeToken<List<ExportSentence>>() {}.type
                    val importList: List<ExportSentence> = gson.fromJson(jsonText, type)

                    // 清空數據庫和本地列表
                    sentenceDao.deleteAllSentences()
                    sentences.clear()

                    // 插入新的句子，並按照順序添加到本地列表
                    importList.sortedBy { it.order }.forEach { exportSentence ->
                        val sentence = Sentence(
                            chineseText = exportSentence.zh,
                            englishText = exportSentence.en.ifEmpty { null },
                            japaneseText = exportSentence.jp.ifEmpty { null },
                            thaiText = exportSentence.th.ifEmpty { null }
                        )
                        val newId = sentenceDao.insertSentence(sentence)
                        sentence.id = newId.toInt()
                        sentences.add(sentence)
                    }

                    // 保存新的句子顺序
                    saveSentenceOrder()
                } catch (e: Exception) {
                    // 處理解析錯誤
                    withContext(Dispatchers.Main) {
                        Toast.makeText(getApplication(), "導入失敗，JSON格式不正確", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    fun exportSentences(): String {
        // 獲取當前的句子列表，序列化為 JSON 字符串返回
        val exportList = sentences.mapIndexed { index, sentence ->
            ExportSentence(
                order = index + 1, // 顺序从 1 开始
                zh = sentence.chineseText,
                en = sentence.englishText ?: "",
                jp = sentence.japaneseText ?: "",
                th = sentence.thaiText ?: ""
            )
        }
        val gson = Gson()
        return gson.toJson(exportList)
    }

    fun exportSentencesWithPrompt(): String {
        val prompt = """
        我是一個繁體中文母語使用者，以下是我想要請你幫我翻譯成對應各國語言句子的json表格。
        zh是我當前需要你翻譯的繁體句型依據、en代表英文、jp代表日文、th是泰文。請以貼近口語表達的方式幫我進行翻譯，並把填好完整的json回傳給我，謝謝。
         """.trimIndent()
        val jsonData = exportSentences()
        return prompt + "\n" +  jsonData
    }

    // ExportSentence
    data class ExportSentence(
        val order: Int,
        val zh: String,
        val en: String,
        val jp: String,
        val th: String
    )
}