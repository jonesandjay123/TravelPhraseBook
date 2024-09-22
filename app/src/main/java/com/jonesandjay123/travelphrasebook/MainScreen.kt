package com.jonesandjay123.travelphrasebook

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import java.util.Locale

@Composable
fun MainScreen(tts: TextToSpeech, sentenceDao: SentenceDao) {
    // 創建 MainViewModel 的實例
    val viewModel: MainViewModel = viewModel(
        factory = MainViewModelFactory(sentenceDao)
    )

    // 從 ViewModel 獲取句子列表
    val sentences = viewModel.sentences

    // 語言列表
    val languages = listOf("中", "泰", "日")
    var currentLanguage by remember { mutableStateOf("中") }

    // 新句子輸入
    var newSentence by remember { mutableStateOf("") }

    // 更新 TTS 語言
    LaunchedEffect(currentLanguage) {
        tts.language = when (currentLanguage) {
            "中" -> Locale.CHINESE
            "泰" -> Locale("th")
            "日" -> Locale.JAPANESE
            else -> Locale.CHINESE
        }
    }

    Scaffold(
        topBar = {
            // 語言選擇下拉選單
            LanguageDropdown(
                languages = languages,
                currentLanguage = currentLanguage,
                onLanguageSelected = { selectedLanguage ->
                    currentLanguage = selectedLanguage
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
            ) {
                // 句子列表
                SentenceList(
                    sentences = sentences,
                    currentLanguage = currentLanguage,
                    tts = tts,
                    onTranslationChanged = { sentence ->
                        viewModel.updateSentence(sentence)
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // 新增句子輸入區域
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    TextField(
                        value = newSentence,
                        onValueChange = { newSentence = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("輸入新的繁體中文句子") },
                        singleLine = true
                    )
                    Button(
                        onClick = {
                            if (newSentence.isNotBlank()) {
                                val sentence = Sentence(
                                    chineseText = newSentence
                                )
                                viewModel.addSentence(sentence)
                                newSentence = ""
                            }
                        },
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text("添加")
                    }
                }
            }
        }
    )
}