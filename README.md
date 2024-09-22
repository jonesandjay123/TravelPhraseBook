# Travel Phrasebook

A mobile application for managing and learning travel phrases in multiple languages, developed using Jetpack Compose, Room Database, and Text-to-Speech (TTS) functionality.

## Table of Contents

- [Features](https://www.notion.so/de3fec0150964696a78bc08669066d45?pvs=21)
- [Getting Started](https://www.notion.so/de3fec0150964696a78bc08669066d45?pvs=21)
    - [Prerequisites](https://www.notion.so/de3fec0150964696a78bc08669066d45?pvs=21)
    - [Installation](https://www.notion.so/de3fec0150964696a78bc08669066d45?pvs=21)
- [Usage](https://www.notion.so/de3fec0150964696a78bc08669066d45?pvs=21)
- [Adding New Languages](https://www.notion.so/de3fec0150964696a78bc08669066d45?pvs=21)
    - [1. Update the Language List](https://www.notion.so/de3fec0150964696a78bc08669066d45?pvs=21)
    - [2. Update the `Sentence` Entity](https://www.notion.so/de3fec0150964696a78bc08669066d45?pvs=21)
    - [3. Update the Database Version and Migration](https://www.notion.so/de3fec0150964696a78bc08669066d45?pvs=21)
    - [4. Modify UI Components](https://www.notion.so/de3fec0150964696a78bc08669066d45?pvs=21)
    - [5. Handle TTS Support](https://www.notion.so/de3fec0150964696a78bc08669066d45?pvs=21)
    - [6. Populate Translations](https://www.notion.so/de3fec0150964696a78bc08669066d45?pvs=21)
- [License](https://www.notion.so/de3fec0150964696a78bc08669066d45?pvs=21)

## Features

- **Multi-language Support**: Manage phrases in multiple languages (e.g., Chinese, Thai, Japanese).
- **Reorderable List**: Long-press and drag to reorder phrases.
- **Persistent Storage**: Phrases and their order are saved using Room Database and SharedPreferences.
- **Text-to-Speech**: Listen to phrases using the device's TTS engine.
- **Add/Edit/Delete Phrases**: Easily manage your phrase list.

## Getting Started

### Prerequisites

- **Android Studio**: Latest version recommended.
- **Android Device or Emulator**: Running Android API level 21 or higher.

### Installation

1. **Clone the Repository**:

    ```bash
    bash
    複製程式碼
    git clone https://github.com/yourusername/travel-phrasebook.git
    
    ```

2. **Open in Android Studio**:
    - Open Android Studio.
    - Click on `File` -> `Open...` and select the cloned repository folder.
3. **Build the Project**:
    - Click on `Build` -> `Make Project` or press `Ctrl+F9`.
4. **Run the App**:
    - Connect your Android device or start an emulator.
    - Click on `Run` -> `Run 'app'` or press `Shift+F10`.

## Usage

- **Add a New Phrase**:
    - Enter a phrase in the input field at the bottom.
    - Click the `Add` button.
- **Translate a Phrase**:
    - Tap on a phrase to edit its translation in the selected language.
- **Reorder Phrases**:
    - Long-press on a phrase and drag it to a new position.
- **Delete a Phrase**:
    - Tap the delete icon (🗑️) on the right side of a phrase.

## Adding New Languages

If you wish to add support for new languages (e.g., Korean, German), follow the steps below to update the application accordingly.

### 1. Update the Language List

In the `MainScreen.kt` file, update the `languages` list to include the new language codes.

```kotlin
kotlin
複製程式碼
val languages = listOf("中", "泰", "日", "韩", "德") // Added Korean and German

```

### 2. Update the `Sentence` Entity

In the `Sentence.kt` file, add new nullable fields for the translations of the new languages.

```kotlin
kotlin
複製程式碼
@Entity(tableName = "sentences")
data class Sentence(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    val chineseText: String,
    var thaiText: String? = null,
    var japaneseText: String? = null,
    var koreanText: String? = null,   // Added for Korean
    var germanText: String? = null    // Added for German
)

```

### 3. Update the Database Version and Migration

Since the `Sentence` entity has changed, you need to update the database version and provide a migration strategy.

### 3.1 Update the Database Version

In the `AppDatabase.kt` file, increment the database version.

```kotlin
kotlin
複製程式碼
@Database(entities = [Sentence::class], version = 2) // Incremented version
abstract class AppDatabase : RoomDatabase() {
    abstract fun sentenceDao(): SentenceDao
}

```

### 3.2 Provide Migration Logic

In your `MainActivity.kt` or wherever you initialize your Room database, add migration logic.

```kotlin
kotlin
複製程式碼
val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE sentences ADD COLUMN koreanText TEXT")
        database.execSQL("ALTER TABLE sentences ADD COLUMN germanText TEXT")
    }
}

val db = Room.databaseBuilder(
    applicationContext,
    AppDatabase::class.java, "phrasebook-db"
)
    .addMigrations(MIGRATION_1_2)
    .build()

```

### 4. Modify UI Components

### 4.1 Update Language Selection Logic

In `MainScreen.kt`, update the language setting logic to handle the new languages.

```kotlin
kotlin
複製程式碼
LaunchedEffect(currentLanguage) {
    tts?.language = when (currentLanguage) {
        "中" -> Locale.CHINESE
        "泰" -> Locale("th")
        "日" -> Locale.JAPANESE
        "韩" -> Locale.KOREAN    // Added for Korean
        "德" -> Locale.GERMAN    // Added for German
        else -> Locale.CHINESE
    }
}

```

### 4.2 Update the `SentenceItem` Composable

In `SentenceItem.kt`, handle the new languages for displaying and editing translations.

```kotlin
kotlin
複製程式碼
var translationText by remember(sentence, currentLanguage) {
    mutableStateOf(
        when (currentLanguage) {
            "泰" -> sentence.thaiText ?: ""
            "日" -> sentence.japaneseText ?: ""
            "韩" -> sentence.koreanText ?: ""    // Added for Korean
            "德" -> sentence.germanText ?: ""    // Added for German
            else -> ""
        }
    )
}

// Update the translation when the user edits it
OutlinedTextField(
    value = translationText,
    onValueChange = {
        translationText = it

        when (currentLanguage) {
            "泰" -> sentence.thaiText = translationText
            "日" -> sentence.japaneseText = translationText
            "韩" -> sentence.koreanText = translationText  // Added for Korean
            "德" -> sentence.germanText = translationText  // Added for German
        }

        onTranslationChanged(sentence)
    },
    // Other parameters...
)

```

### 5. Handle TTS Support

Ensure that the Text-to-Speech engine supports the new languages.

### 5.1 Check TTS Language Availability

Before setting the language, check if the TTS engine supports it.

```kotlin
kotlin
複製程式碼
val locale = when (currentLanguage) {
    "中" -> Locale.CHINESE
    "泰" -> Locale("th")
    "日" -> Locale.JAPANESE
    "韩" -> Locale.KOREAN
    "德" -> Locale.GERMAN
    else -> Locale.CHINESE
}

val result = tts?.setLanguage(locale)
if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
    Toast.makeText(
        LocalContext.current,
        "Selected language is not supported for speech",
        Toast.LENGTH_SHORT
    ).show()
}

```

### 5.2 Prompt User to Install Language Data

If the language is not supported, prompt the user to install the necessary language data.

```kotlin
kotlin
複製程式碼
if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
    val installIntent = Intent()
    installIntent.action = TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA
    LocalContext.current.startActivity(installIntent)
}

```

### 6. Populate Translations

To populate the translations for the new languages:

- **Option 1: Manually**: Edit each phrase within the app and add the translations.
- **Option 2: Bulk Import**: Use AWS DynamoDB or another backend service to store and retrieve phrases and their translations.

### Using AWS DynamoDB

1. **Set Up AWS DynamoDB**:
    - Create a table with columns for each language.
2. **Integrate AWS SDK**:
    - Add AWS SDK dependencies to your project.
    - Implement API calls to fetch data from DynamoDB.
3. **Fetch and Update Data**:
    - On app startup, check for network connectivity.
    - If connected, fetch the latest phrases and update the local database.
4. **Handle Data Synchronization**:
    - Implement logic to handle conflicts between local changes and remote data.

## License

This project is licensed under the MIT License - see the LICENSE file for details.

---

By following these steps, you can seamlessly add support for new languages to your Travel Phrasebook application. Remember to test each new language thoroughly to ensure all features work as expected.