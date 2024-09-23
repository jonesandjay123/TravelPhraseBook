package com.jonesandjay123.travelphrasebook.ui

import android.app.Application
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jonesandjay123.travelphrasebook.MainViewModel
import com.jonesandjay123.travelphrasebook.MainViewModelFactory
import com.jonesandjay123.travelphrasebook.Sentence
import com.jonesandjay123.travelphrasebook.SentenceDao
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(tts: TextToSpeech?, sentenceDao: SentenceDao) {
    val application = LocalContext.current.applicationContext as Application
    val context = LocalContext.current
    val viewModel: MainViewModel = viewModel(
        factory = MainViewModelFactory(sentenceDao, application)
    )

    val sentences = viewModel.sentences

    val languages = listOf("中", "英", "日", "泰")
    var currentLanguage by remember { mutableStateOf("中") }

    var newSentence by remember { mutableStateOf("") }

    LaunchedEffect(currentLanguage) {
        if (tts != null) {
            val locale = when (currentLanguage) {
                "中" -> Locale.CHINESE
                "英" -> Locale.ENGLISH
                "日" -> Locale.JAPANESE
                "泰" -> Locale("th")
                else -> Locale.CHINESE
            }
            val result = tts.setLanguage(locale)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(
                    context,
                    "所選語言不支援語音合成！",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    Scaffold(
        topBar = {
            // 頂部應用欄，包含語言選擇按鈕
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
                // 新增語句輸入區域
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
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
                        modifier = Modifier.size(48.dp),
                        shape = CircleShape,
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "+",
                            fontSize = 24.sp // 增大“+”號的大小
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextField(
                        value = newSentence,
                        onValueChange = { newSentence = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("新增語句...") },
                        singleLine = true
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // 句子列表
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
                    onSentenceOrderChanged = {
                        viewModel.onSentenceOrderChanged()
                    },
                    modifier = Modifier.weight(1f) // 確保列表佔據剩餘空間
                )
            }
        }
    )
}
