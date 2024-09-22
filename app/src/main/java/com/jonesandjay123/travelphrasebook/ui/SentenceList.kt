package com.jonesandjay123.travelphrasebook.ui

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.jonesandjay123.travelphrasebook.Sentence

@Composable
fun SentenceList(
    sentences: List<Sentence>,
    currentLanguage: String,
    tts: TextToSpeech?,
    onTranslationChanged: (Sentence) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(sentences) { sentence ->
            SentenceItem(
                sentence = sentence,
                currentLanguage = currentLanguage,
                tts = tts,
                onTranslationChanged = onTranslationChanged
            )
        }
    }
}