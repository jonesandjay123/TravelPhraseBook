package com.jonesandjay123.travelphrasebook.ui

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

    val isTranslationAvailable = translationText.isNotBlank()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = sentence.chineseText,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.weight(1f)
                )

                // 删除按钮
                IconButton(
                    onClick = { onDeleteSentence(sentence) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "刪除"
                    )
                }
            }

            // 翻译内容输入框
            OutlinedTextField(
                value = translationText,
                onValueChange = {
                    translationText = it

                    // 更新句子的翻译内容
                    when (currentLanguage) {
                        "英" -> sentence.englishText = translationText
                        "日" -> sentence.japaneseText = translationText
                        "泰" -> sentence.thaiText = translationText
                    }

                    // 通知 ViewModel 更新数据库
                    onTranslationChanged(sentence)
                },
                placeholder = { Text("貼上翻譯語句") },
                modifier = Modifier.fillMaxWidth()
            )

            // 语音播放按钮
            IconButton(
                onClick = {
                    if (isTranslationAvailable && tts != null) {
                        tts.speak(translationText, TextToSpeech.QUEUE_FLUSH, null, null)
                    }
                },
                enabled = isTranslationAvailable
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "播放"
                )
            }
        }
    }
}