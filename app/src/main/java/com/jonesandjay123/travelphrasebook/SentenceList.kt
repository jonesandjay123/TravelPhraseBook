package com.jonesandjay123.travelphrasebook

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SentenceList(
    sentences: List<Sentence>,
    currentLanguage: String,
    tts: TextToSpeech,
    onTranslationChanged: (Sentence) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(sentences.size) { index ->
            val sentence = sentences[index]
            SentenceItem(
                sentence = sentence,
                currentLanguage = currentLanguage,
                tts = tts,
                onTranslationChanged = onTranslationChanged
            )
        }
    }
}
