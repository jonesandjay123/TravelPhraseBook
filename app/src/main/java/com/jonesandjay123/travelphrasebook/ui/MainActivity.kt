package com.jonesandjay123.travelphrasebook.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.room.Room
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
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
                    MainScreen(
                        tts = tts,
                        sentenceDao = db.sentenceDao(),
                        onUploadToWearable = { phrasesJson ->
                            uploadPhrasesToDataLayer(phrasesJson)
                        }
                    )
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tts.stop()
        tts.shutdown()
    }

    @SuppressLint("VisibleForTests")
    fun uploadPhrasesToDataLayer(phrasesJson: String) {
        // 創建 PutDataMapRequest，指定路徑為 "/phrases"
        val dataMapRequest = PutDataMapRequest.create("/phrases")
        val dataMap = dataMapRequest.dataMap
        dataMap.putString("phrases_json", phrasesJson)

        // 構建 PutDataRequest，並設置為 urgent（緊急），加快傳輸
        val putDataRequest = dataMapRequest.asPutDataRequest().setUrgent()

        // 獲取 DataClient 並上傳數據
        val dataClient = Wearable.getDataClient(this)
        val putDataTask = dataClient.putDataItem(putDataRequest)

        putDataTask.addOnSuccessListener {
            Log.d("MainActivity", "句子清單已成功上傳到 Data Layer，URI: ${it.uri}")
            Toast.makeText(this, "已成功上傳句子清單到手錶", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener { e ->
            Log.e("MainActivity", "上傳句子清單失敗：${e.message}")
            Toast.makeText(this, "上傳句子清單到手錶失敗：${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}