package com.jonesandjay123.travelphrasebook.ui

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jonesandjay123.travelphrasebook.Sentence

@Composable
fun SentenceItem(
    sentence: Sentence,
    currentLanguage: String,
    tts: TextToSpeech?,
    onTranslationChanged: (Sentence) -> Unit,
    onDeleteSentence: (Sentence) -> Unit,
    modifier: Modifier = Modifier
) {
    var translationText by remember(sentence, currentLanguage) {
        mutableStateOf(
            when (currentLanguage) {
                "英" -> sentence.englishText ?: ""
                "日" -> sentence.japaneseText ?: ""
                "泰" -> sentence.thaiText ?: ""
                else -> ""
            }
        )
    }

    // 判断播放按钮是否可用
    val isPlayable = when (currentLanguage) {
        "中" -> sentence.chineseText.isNotBlank()
        "英", "日", "泰" -> translationText.isNotBlank()
        else -> false
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            // 播放按钮、中文文本和刪除按鈕在同一行
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 播放按鈕
                IconButton(
                    onClick = {
                        val textToSpeak = when (currentLanguage) {
                            "中" -> sentence.chineseText
                            else -> translationText
                        }
                        tts?.speak(textToSpeak, TextToSpeech.QUEUE_FLUSH, null, null)
                    },
                    enabled = isPlayable
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "播放"
                    )
                }

                // 中文文本
                Text(
                    text = sentence.chineseText,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 8.dp)
                )

                // 刪除按鈕
                IconButton(
                    onClick = { onDeleteSentence(sentence) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "刪除"
                    )
                }
            }

            // 翻譯內容輸入框(非中文時顯示)
            if (currentLanguage != "中") {
                OutlinedTextField(
                    value = translationText,
                    onValueChange = {
                        translationText = it

                        // 更新語句的翻譯內容
                        when (currentLanguage) {
                            "英" -> sentence.englishText = translationText
                            "日" -> sentence.japaneseText = translationText
                            "泰" -> sentence.thaiText = translationText
                        }

                        // 通知 ViewModel 更新數據庫
                        onTranslationChanged(sentence)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                    label = { Text("語句譯文") }
                )
            }
        }
    }
}
