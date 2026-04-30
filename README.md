# Travel PhraseBook / 旅行短語手冊

這是一款 Android 手機 App，用來預先整理旅行時可能會用到的句子，並在現場直接顯示、複製或透過手機 Text-to-Speech（TTS）播放。最初的使用情境是：出國前先把常用句準備好，旅途中遇到語言不通時，可以快速切換到日文、英文或泰文並播放給對方聽。

目前專案使用 Kotlin、Jetpack Compose、Room Database、Google Cloud Translation API 與 Android Text-to-Speech。

## 目前狀態

- Android 原生 App（Jetpack Compose）
- 本地資料庫儲存短語（Room）
- 支援中文、英文、日文、泰文欄位
- 支援 TTS 播放目前語言的文字
- 支援手動新增、編輯、刪除、排序
- 支援 JSON 匯入 / 匯出
- 支援產生「給 LLM 翻譯用」的 prompt + JSON
- 支援 Google Cloud Translation API 批次翻譯
- 支援上傳短語 JSON 到 Wear OS 手錶端
- 2026-04 更新：內建日本旅行常用句包，方便東京 / 日本旅行前快速使用

## 翻譯原理

這個 App 目前不是直接使用 LLM 翻譯。

它有兩種主要翻譯 / 補資料方式：

### 1. App 內建 API 翻譯：Google Cloud Translation API

程式碼位置：

- `ApiClient.kt`
- `TranslationService.kt`
- `TranslationRequest.kt`
- `TranslationResponse.kt`
- `MainViewModel.translateSentencesWithApi()`

流程大致是：

1. 使用者按下上方的語言 / 翻譯按鈕。
2. App 跳出確認視窗，提醒會用 Google 翻譯補齊或覆蓋翻譯。
3. App 找出目前缺少英文、日文、泰文翻譯的中文句子。
4. 呼叫 Google Cloud Translation API v2：

   ```text
   https://translation.googleapis.com/language/translate/v2
   ```

5. 把翻譯結果寫回 Room Database。

也就是說，這是傳統 Google Translate API，不是 ChatGPT / Gemini / Claude 這類 LLM。

### 2. 手動匯出給 LLM 翻譯，再貼回 App

App 也有「帶 prompt 匯出」功能。

這個功能會把目前句子整理成 JSON，前面加上一段提示詞，讓你可以複製到 ChatGPT / Gemini / Claude 請它幫忙翻譯。翻完後，再把 JSON 貼回 App 匯入。

這個流程比較像：

1. 在 App 裡新增中文句子。
2. 打開匯入 / 匯出視窗。
3. 按「帶prompt匯出」。
4. 複製內容給 LLM。
5. 把 LLM 補完翻譯的 JSON 貼回 App。
6. 按「匯入」。

所以結論是：

- App 內建自動翻譯：Google Cloud Translation API。
- LLM 翻譯：不是直接整合在 App 裡，而是透過「帶 prompt 匯出」半手動完成。
- 也可以完全手動編輯每句翻譯。

## API Key 設定

如果只使用手動新增、內建日本常用句、TTS 播放、JSON 匯入匯出，不一定需要 API key。

如果要使用 App 內建的 Google 翻譯功能，需要在專案根目錄建立 `local.properties`，加入：

```properties
CLOUD_API_KEY=你的_Google_Cloud_Translation_API_Key
```

Repo 裡有 `template.env` 可參考：

```text
CLOUD_API_KEY=YOUR_API_KEY
```

注意：不要把真正的 API key commit 進 GitHub。

## 日本旅行常用句包

2026-04 更新加入一組日本旅行常用句，適合出發前快速預載。

內容涵蓋：

- 基本溝通：不會日文、請說慢一點、請寫下來
- 交通：車站、月台、下車站、Suica / PASMO
- 飯店：預約、入住、退房、寄放行李
- 餐廳：人數、菜單、推薦、點餐、結帳、過敏 / 忌口
- 購物：信用卡、現金、免稅、尺寸、試穿
- 緊急狀況：迷路、護照遺失、手機遺失、叫救護車 / 警察

使用方式：

- 如果 App 是第一次安裝、資料庫為空，會自動預載這批句子。
- 如果 App 已經有資料，可以按畫面上的「加入日本旅行常用句」按鈕補進去。
- App 會避免用中文句子重複加入同一批預設句。

預設句位於：

```text
app/src/main/java/com/jonesandjay123/travelphrasebook/PhrasePresets.kt
```

## 主要功能

### 短語管理

- 新增中文短語
- 編輯英文 / 日文 / 泰文翻譯
- 刪除短語
- 長按拖曳排序
- 排序結果會保存到 SharedPreferences

### 語言切換

上方可以切換目前顯示 / 播放語言：

- 中
- 英
- 日
- 泰

切換語言時，App 會同步設定 TTS 的語系，例如日文會使用 `Locale.JAPANESE`。

### TTS 播放

每張短語卡都有播放按鈕，會播放目前語言的文字。

播放依賴 Android 裝置本身的 Text-to-Speech 引擎。如果裝置缺少日文語音資料，可能需要到系統設定或 Google TTS 安裝語音包。

### 複製文字

每張短語卡也有複製按鈕。這是旅途中很實用的 fallback：

- 現場太吵，TTS 聽不清楚
- TTS 語音包缺失
- 對方比較適合直接看文字

### JSON 匯入 / 匯出

可以把所有短語匯出成 JSON，也可以貼入 JSON 覆蓋匯入。

⚠️ 注意：目前匯入會清空原本資料並覆蓋成新資料，App 會先跳確認視窗。

### 帶 prompt 匯出

「帶prompt匯出」會產生一段提示詞 + JSON，方便丟給 LLM 補翻譯。

這是目前最適合快速準備大量旅行句子的流程：

1. 先在中文欄位新增你想說的句子。
2. 帶 prompt 匯出。
3. 用 ChatGPT / Gemini / Claude 補日文、英文、泰文。
4. 貼回 App 匯入。
5. 出國時直接播放。

### 上傳到手錶

App 有一個上傳按鈕，會把目前短語 JSON 傳給 Wear OS 端。

相關邏輯在 `MainActivity.kt` / Compose UI 的 `onUploadToWearable` callback。若要讓手錶端完整使用，需要確認 Wear OS companion 端是否仍存在並相容。

## 專案架構簡介

```text
app/src/main/java/com/jonesandjay123/travelphrasebook/
├── ApiClient.kt                 # Retrofit client，連 Google Translation API
├── AppDatabase.kt               # Room database
├── MainViewModel.kt             # 短語資料、翻譯、匯入匯出邏輯
├── PhrasePresets.kt             # 日本旅行常用句預設資料
├── Sentence.kt                  # Room entity
├── SentenceDao.kt               # Room DAO
├── TranslationRequest.kt        # Google Translation API request model
├── TranslationResponse.kt       # Google Translation API response model
├── TranslationService.kt        # Retrofit service interface
└── ui/
    ├── MainActivity.kt          # Android entry point / TTS 初始化
    ├── MainScreen.kt            # 主畫面
    ├── SentenceItem.kt          # 單張短語卡
    ├── SentenceList.kt          # 可拖曳排序列表
    └── ImportExportDialog.kt    # JSON 匯入匯出視窗
```

## 開發環境

建議使用：

- Android Studio
- JDK 17
- Android SDK / Compile SDK 34
- Kotlin 1.9.x
- Android Gradle Plugin 8.5.x

## 如何在本機跑起來

### 1. Clone repo

```bash
git clone https://github.com/jonesandjay123/TravelPhraseBook.git
cd TravelPhraseBook
```

### 2. 用 Android Studio 開啟

在 Android Studio 選擇：

```text
File → Open → TravelPhraseBook
```

等 Gradle sync 完成。

### 3. 設定 API key（可選）

若要使用 Google 翻譯功能，在根目錄建立 `local.properties`：

```properties
CLOUD_API_KEY=你的_Google_Cloud_Translation_API_Key
```

如果只是要使用預設日本短語、手動編輯與 TTS，可以先略過。

### 4. Build / Run

在 Android Studio 裡：

```text
Run → Run 'app'
```

或用命令列：

```bash
./gradlew :app:assembleDebug
```

## 注意事項與已知限制

- Google Cloud Translation API 需要有效 API key，並可能產生 Google Cloud 費用。
- TTS 效果取決於 Android 裝置安裝的語音引擎與語音包。
- 匯入 JSON 目前是覆蓋式匯入，不是 merge。
- App 目前沒有真正的 LLM API 串接；LLM 流程是靠 prompt 匯出 / 手動貼回。
- README 舊版曾提到 AWS DynamoDB，但目前實際程式碼主要是本機 Room Database，並沒有完整後端同步流程。

## 出國前建議使用流程

1. Pull 最新版 repo。
2. 用 Android Studio 安裝到手機。
3. 打開 App，確認日本常用句已出現。
4. 切到「日」，按幾句播放測試日文 TTS。
5. 如果手機提示缺少語音資料，先安裝日文 TTS。
6. 把你個人一定會用到的句子補進去，例如飯店名稱、餐廳預約、特殊飲食需求。
7. 出門前匯出 JSON 備份一次。

## License

此專案目前未明確指定授權條款。若要公開重用或發布，建議補上正式 LICENSE。
