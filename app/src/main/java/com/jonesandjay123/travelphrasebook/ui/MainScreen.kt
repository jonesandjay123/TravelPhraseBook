package com.jonesandjay123.travelphrasebook.ui

import android.app.Application
import android.content.Context
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ImportExport
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
    // 獲取 SharedPreferences 實例
    val sharedPreferences = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)

    // 初始化 currentLanguage，讀取已保存的語言設置
    var currentLanguage by remember {
        mutableStateOf(
            sharedPreferences.getString("current_language", "中") ?: "中"
        )
    }

    var newSentence by remember { mutableStateOf("") }

    var showDialog by remember { mutableStateOf(false) }

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

                    // 導入／匯出鈕
                    IconButton(onClick = { showDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.ImportExport,
                            contentDescription = "導入/導出"
                        )
                    }

                    LanguageDropdown(
                        languages = languages,
                        currentLanguage = currentLanguage,
                        onLanguageSelected = { selectedLanguage ->
                            currentLanguage = selectedLanguage
                            // 保存選擇的語言到 SharedPreferences
                            sharedPreferences.edit().putString("current_language", selectedLanguage).apply()
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
                // 如果需要顯示對話框
                if (showDialog) {
                    ImportExportDialog(
                        onDismissRequest = { showDialog = false },
                        sentences = sentences,
                        onImport = { jsonText ->
                            // 解析並導入數據的邏輯
                            viewModel.importSentences(jsonText)
                        },
                        onExport = {
                            // 導出數據的邏輯
                            viewModel.exportSentences()
                        },
                        onExportWithPrompt = {
                            // 带 prompt 導出數據的邏輯
                            viewModel.exportSentencesWithPrompt()
                        }
                    )
                }

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
