package com.example.ckc_englihoo.Screen.Exercises.MatchImageComponents

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.example.ckc_englihoo.Screen.Exercises.MatchImageItem
import com.example.ckc_englihoo.Screen.Exercises.DragState

@Composable
fun WordCardsSection(
    items: List<MatchImageItem>,
    selectedWord: String?,
    isSubmitted: Boolean,
    userMatches: Map<String, String>,
    onWordSelected: (String) -> Unit
) {
    val usedWords = userMatches.values.toSet()

    // Wordwall style - horizontal scrollable layout
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // 4 cột x 3 hàng để button rộng hơn
        LazyVerticalGrid(
            columns = GridCells.Fixed(4), // 4 cột để button rộng hơn
            horizontalArrangement = Arrangement.spacedBy(8.dp), // Spacing đồng đều
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp), // Height cao hơn cho 3 hàng
            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 6.dp) // Padding vừa phải
        ) {
            items(items) { item ->
                val isUsed = usedWords.contains(item.id)
                val isSelected = selectedWord == item.id

                WordCard(
                    item = item,
                    isUsed = isUsed,
                    isSelected = isSelected,
                    isSubmitted = isSubmitted,
                    onWordSelected = onWordSelected
                )
            }
        }
    }
}

@Composable
fun WordCard(
    item: MatchImageItem,
    isUsed: Boolean,
    isSelected: Boolean,
    isSubmitted: Boolean,
    onWordSelected: (String) -> Unit
) {
    // Màu sắc khác nhau cho 10 đáp án như Wordwall
    val cardColors = listOf(
        Color(0xFF2196F3), // Blue
        Color(0xFF4CAF50), // Green
        Color(0xFFFF9800), // Orange
        Color(0xFF9C27B0), // Purple
        Color(0xFFF44336), // Red
        Color(0xFF00BCD4), // Cyan
        Color(0xFF8BC34A), // Light Green
        Color(0xFFFF5722), // Deep Orange
        Color(0xFF673AB7), // Deep Purple
        Color(0xFF607D8B)  // Blue Grey
    )

    // Lấy màu dựa trên index của item
    val itemIndex = item.id.hashCode() % cardColors.size
    val itemColor = cardColors[kotlin.math.abs(itemIndex)]

    // Wordwall style - kích thước đồng đều
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(42.dp) // Height đồng đều
            .alpha(if (isUsed && !isSelected) 0.7f else 1f)
            .clickable {
                if (!isSubmitted) {
                    onWordSelected(item.id)
                }
            },
        colors = CardDefaults.cardColors(
            containerColor = when {
                isSelected -> itemColor.copy(alpha = 0.9f)
                isUsed -> itemColor.copy(alpha = 0.6f)
                else -> itemColor
            }
        ),
        shape = RoundedCornerShape(10.dp), // Border radius đồng đều
        elevation = CardDefaults.cardElevation(
            defaultElevation = when {
                isSelected -> 6.dp
                else -> 3.dp // Shadow đồng đều
            }
        ),
        border = when {
            isSelected -> BorderStroke(1.5.dp, Color.White)
            else -> null
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 6.dp, vertical = 6.dp), // Padding rộng hơn
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = item.word,
                fontSize = 12.sp, // Font size đồng đều
                fontWeight = FontWeight.Bold,
                color = if (isUsed && !isSelected) Color(0xFF757575) else Color.White,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}
