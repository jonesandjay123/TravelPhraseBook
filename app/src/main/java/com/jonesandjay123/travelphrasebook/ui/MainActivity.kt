package com.jonesandjay123.travelphrasebook.ui

import android.os.Bundle
import android.speech.tts.TextToSpeech
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.room.Room
import com.jonesandjay123.travelphrasebook.AppDatabase
import com.jonesandjay123.travelphrasebook.ui.theme.TravelPhraseBookTheme
import java.util.*

class MainActivity : ComponentActivity() {
    private lateinit var tts: TextToSpeech
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 初始化 TextToSpeech
        tts = TextToSpeech(this) { status ->
            if (status != TextToSpeech.ERROR) {
                tts.language = Locale.CHINESE
            }
        }

        // 初始化資料庫
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "phrasebook-db"
        ).build()

        setContent {
            TravelPhraseBookTheme {
                MainScreen(tts = tts, sentenceDao = db.sentenceDao())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tts.stop()
        tts.shutdown()
    }
}