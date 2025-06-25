package com.example.ckc_englihoo.Screen.Exercises.CategorizeComponents

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ckc_englihoo.Screen.Exercises.CategorizeItem
import com.example.ckc_englihoo.Screen.Exercises.CategorySlot

@Composable
fun CategorySlotsSection(
    categories: List<CategorySlot>,
    items: List<CategorizeItem>,
    userCategories: Map<String, String>,
    selectedWord: String?,
    isSubmitted: Boolean,
    onCategorySelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
    ) {
        categories.forEach { category ->
            Spacer(modifier = Modifier.height(16.dp))
            
            // Category title
            Text(
                text = category.name,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2196F3),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            // Category slots - 4 slots per category (1 row)
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp), // 1 row
                contentPadding = PaddingValues(4.dp)
            ) {
                items(4) { index -> // 4 slots per category
                    val wordsInCategory = userCategories.filter { it.value == category.id }.keys.toList()
                    val wordInSlot = wordsInCategory.getOrNull(index)
                    
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(35.dp)
                            .clickable {
                                if (!isSubmitted && selectedWord != null && wordInSlot == null) {
                                    onCategorySelected(category.id)
                                }
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = if (wordInSlot != null) {
                                if (isSubmitted) {
                                    val item = items.find { it.text == wordInSlot }
                                    val isCorrect = when (category.id) {
                                        "noun" -> item?.category == "Noun (n)"
                                        "verb" -> item?.category == "Verb (v)"
                                        "adjective" -> item?.category == "Adjective (adj)"
                                        else -> false
                                    }
                                    if (isCorrect) Color(0xFFE8F5E8) else Color(0xFFFFEBEE)
                                } else {
                                    // Lấy màu của đáp án
                                    val item = items.find { it.text == wordInSlot }
                                    item?.color?.copy(alpha = 0.3f) ?: Color(0xFFE3F2FD)
                                }
                            } else {
                                Color.White
                            }
                        ),
                        border = BorderStroke(
                            width = 2.dp,
                            color = if (selectedWord != null && wordInSlot == null) {
                                Color(0xFF2196F3)
                            } else {
                                Color(0xFFE0E0E0)
                            }
                        ),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = if (selectedWord != null && wordInSlot == null) 4.dp else 1.dp
                        ),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            if (wordInSlot != null) {
                                Text(
                                    text = wordInSlot,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.Black,
                                    textAlign = TextAlign.Center,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}
