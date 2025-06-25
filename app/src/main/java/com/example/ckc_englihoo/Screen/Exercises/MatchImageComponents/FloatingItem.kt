package com.example.ckc_englihoo.Screen.Exercises.MatchImageComponents

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.example.ckc_englihoo.Screen.Exercises.MatchImageItem // Thêm import

@Composable
fun FloatingItem(
    item: MatchImageItem, // Sửa thành MatchImageItem
    position: Offset,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .offset(
                x = (position.x / LocalDensity.current.density).dp - 30.dp,
                y = (position.y / LocalDensity.current.density).dp - 20.dp
            )
            .width(60.dp)
            .height(40.dp),
        colors = CardDefaults.cardColors(
            containerColor = item.color.copy(alpha = 0.9f)
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = item.word,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}
