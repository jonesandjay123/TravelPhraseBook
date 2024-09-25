package com.jonesandjay123.travelphrasebook.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.jonesandjay123.travelphrasebook.Sentence

@Composable
fun ImportExportDialog(
    onDismissRequest: () -> Unit,
    sentences: List<Sentence>,
    onImport: (String) -> Unit,
    onExport: () -> String,
    onExportWithPrompt: () -> String
) {
    var jsonText by remember { mutableStateOf("") }
    var showConfirmDialog by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                // 標題行，包含標題按關閉按鈕
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "導入/導出",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = onDismissRequest) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "關閉"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = jsonText,
                    onValueChange = { jsonText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .verticalScroll(rememberScrollState()),
                    placeholder = { Text("在此處貼上或編輯 JSON 數據") },
                    singleLine = false,
                    maxLines = Int.MAX_VALUE
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    TextButton(onClick = {
                        // 導出邏輯
                        jsonText = onExport()
                    }) {
                        Text("匯出")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(onClick = {
                        // 帶 prompt 的導出邏輯
                        jsonText = onExportWithPrompt()
                    }) {
                        Text("帶prompt匯出")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(onClick = {
                        // 顯示確認對話框
                        showConfirmDialog = true
                    }) {
                        Text("匯入")
                    }
                }
            }
        }
    }

    // 確認對話框
    if (showConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = { Text("確認匯入") },
            text = { Text("這將導致當前的數據被覆蓋，是否繼續？") },
            confirmButton = {
                TextButton(onClick = {
                    showConfirmDialog = false
                    onDismissRequest()
                    onImport(jsonText)
                }) {
                    Text("確定")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("取消")
                }
            }
        )
    }
}