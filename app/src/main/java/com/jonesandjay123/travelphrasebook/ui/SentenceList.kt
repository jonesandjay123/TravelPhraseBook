package com.jonesandjay123.travelphrasebook.ui

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.jonesandjay123.travelphrasebook.Sentence
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable

@Composable
fun SentenceList(
    sentences: MutableList<Sentence>, // 改为 MutableList
    currentLanguage: String,
    tts: TextToSpeech?,
    onTranslationChanged: (Sentence) -> Unit,
    onDeleteSentence: (Sentence) -> Unit,
    modifier: Modifier = Modifier
) {
    val reorderState = rememberReorderableLazyListState(
        onMove = { from, to ->
            sentences.add(to.index, sentences.removeAt(from.index))
        }
    )

    LazyColumn(
        state = reorderState.listState,
        modifier = modifier
            .fillMaxSize()
            .reorderable(reorderState)
            .detectReorderAfterLongPress(reorderState)
    ) {
        itemsIndexed(items = sentences, key = { _, item -> item.id }) { index, sentence ->
            ReorderableItem(reorderState, key = sentence.id) { isDragging ->
                SentenceItem(
                    sentence = sentence,
                    currentLanguage = currentLanguage,
                    tts = tts,
                    onTranslationChanged = onTranslationChanged,
                    onDeleteSentence = onDeleteSentence,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            if (isDragging) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                            else MaterialTheme.colorScheme.background
                        )
                )
            }
        }
    }
}