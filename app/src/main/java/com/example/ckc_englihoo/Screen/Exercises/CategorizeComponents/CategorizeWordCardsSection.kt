package com.example.ckc_englihoo.Screen.Exercises.CategorizeComponents

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ckc_englihoo.Screen.Exercises.CategorizeItem

@Composable
fun CategorizeWordCardsSection(
    items: List<CategorizeItem>,
    selectedWord: String?,
    userCategories: Map<String, String>,
    isSubmitted: Boolean,
    onWordSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // 5 cột x 2 hàng để hiển thị 10 từ
        LazyVerticalGrid(
            columns = GridCells.Fixed(4), // 5 cột để hiển thị 10 từ
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp), // Height để chứa 2 hàng
            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 6.dp)
        ) {
            items(items) { item ->
                val isSelected = selectedWord == item.text
                val isUsed = userCategories.containsKey(item.text)
                val isCorrect = if (isSubmitted) {
                    val assignedCategory = userCategories[item.text]
                    when (assignedCategory) {
                        "noun" -> item.category == "Noun (n)"
                        "verb" -> item.category == "Verb (v)"
                        "adjective" -> item.category == "Adjective (adj)"
                        else -> false
                    }
                } else false

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(42.dp)
                        .alpha(if (isUsed && !isSelected) 0.7f else 1f)
                        .clickable {
                            if (!isSubmitted) {
                                onWordSelected(item.text)
                            }
                        },
                    colors = CardDefaults.cardColors(
                        containerColor = when {
                            isSubmitted && isCorrect -> Color(0xFF4CAF50)
                            isSubmitted && !isCorrect -> Color(0xFFF44336)
                            isSelected -> item.color.copy(alpha = 0.9f)
                            isUsed -> item.color.copy(alpha = 0.6f)
                            else -> item.color
                        }
                    ),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = if (isSelected) 8.dp else 4.dp
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = item.text,
                            color = if (isUsed && !isSelected) Color(0xFF757575) else Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            maxLines = 2
                        )
                    }
                }
            }
        }
    }
}
