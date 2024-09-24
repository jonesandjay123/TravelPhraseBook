package com.jonesandjay123.travelphrasebook.ui

import android.content.Context
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

        // 初始化資料庫
        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "phrasebook-db"
        ).build()

        // 獲取 SharedPreferences 實例，並讀取已保存的語言設置
        val sharedPreferences = getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
        val savedLanguage = sharedPreferences.getString("current_language", "中") ?: "中"

        // 根據已保存的語言設置 TTS 的語言
        val locale = when (savedLanguage) {
            "中" -> Locale.CHINESE
            "英" -> Locale.ENGLISH
            "日" -> Locale.JAPANESE
            "泰" -> Locale("th")
            else -> Locale.CHINESE
        }

        // 初始化 TextToSpeech，並在初始化完成後設置內容
        tts = TextToSpeech(this) { status ->
            if (status != TextToSpeech.ERROR) {
                tts.language = locale
            }
            // 在 TTS 初始化完成後，調用 setContent
            setContent {
                TravelPhraseBookTheme {
                    MainScreen(tts = tts, sentenceDao = db.sentenceDao())
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tts.stop()
        tts.shutdown()
    }
}