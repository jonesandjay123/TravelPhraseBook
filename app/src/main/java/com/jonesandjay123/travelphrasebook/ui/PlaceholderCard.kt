package com.jonesandjay123.travelphrasebook.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun PlaceholderCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // 模擬標題
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(20.dp)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
            )
            Spacer(modifier = Modifier.height(8.dp))
            // 模擬正文
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(14.dp)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
            )
            // 其他佔位元素
            Spacer(modifier = Modifier.height(8.dp))

        }
    }
}