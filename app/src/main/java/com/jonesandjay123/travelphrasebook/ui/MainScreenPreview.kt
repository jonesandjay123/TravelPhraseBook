package com.jonesandjay123.travelphrasebook.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.jonesandjay123.travelphrasebook.FakeSentenceDao
import com.jonesandjay123.travelphrasebook.ui.theme.TravelPhraseBookTheme

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    TravelPhraseBookTheme {
        MainScreen(
            tts = null, // 预览时可以使用 null
            sentenceDao = FakeSentenceDao()
        )
    }
}