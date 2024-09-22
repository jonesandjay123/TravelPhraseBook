package com.jonesandjay123.travelphrasebook.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.jonesandjay123.travelphrasebook.Sentence

@Preview(showBackground = true)
@Composable
fun SentenceItemPreview() {
    val sentence = Sentence(
        chineseText = "你好",
        thaiText = "สวัสดี",
        japaneseText = "こんにちは"
    )
    SentenceItem(
        sentence = sentence,
        currentLanguage = "泰",
        tts = null,
        onTranslationChanged = {},
        onDeleteSentence = {}
    )
}
