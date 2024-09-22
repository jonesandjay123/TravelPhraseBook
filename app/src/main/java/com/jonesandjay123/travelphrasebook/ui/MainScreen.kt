package com.jonesandjay123.travelphrasebook.ui

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jonesandjay123.travelphrasebook.MainViewModel
import com.jonesandjay123.travelphrasebook.MainViewModelFactory
import com.jonesandjay123.travelphrasebook.Sentence
import com.jonesandjay123.travelphrasebook.SentenceDao
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(tts: TextToSpeech?, sentenceDao: SentenceDao) {
    val viewModel: MainViewModel = viewModel(
        factory = MainViewModelFactory(sentenceDao)
    )

    val sentences = viewModel.sentences

    val languages = listOf("中", "泰", "日")
    var currentLanguage by remember { mutableStateOf("中") }

    var newSentence by remember { mutableStateOf("") }

    LaunchedEffect(currentLanguage) {
        if (tts != null) {
            tts.language = when (currentLanguage) {
                "中" -> Locale.CHINESE
                "泰" -> Locale("th")
                "日" -> Locale.JAPANESE
                else -> Locale.CHINESE
            }
        }
    }

    Scaffold(
        topBar = {
            // 顶部应用栏，包含语言选择按钮
            TopAppBar(
                title = { Text(text = "旅行短語手冊") },
                actions = {
                    LanguageDropdown(
                        languages = languages,
                        currentLanguage = currentLanguage,
                        onLanguageSelected = { selectedLanguage ->
                            currentLanguage = selectedLanguage
                        }
                    )
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                // 句子列表
                if (tts != null) {
                    SentenceList(
                        sentences = sentences,
                        currentLanguage = currentLanguage,
                        tts = tts,
                        onTranslationChanged = { sentence ->
                            viewModel.updateSentence(sentence)
                        },
                        onDeleteSentence = { sentence ->
                            viewModel.deleteSentence(sentence)
                        },
                        modifier = Modifier.weight(1f) // 确保列表占据剩余空间
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 新增句子输入区域
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    TextField(
                        value = newSentence,
                        onValueChange = { newSentence = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("新增語句...") },
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
                        Text("新增")
                    }
                }
            }
        }
    )
}