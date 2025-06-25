package com.example.ckc_englihoo.Screen.Exercises.MatchImageComponents

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import coil.compose.AsyncImage
import com.example.ckc_englihoo.Screen.Exercises.MatchImageItem
import com.example.ckc_englihoo.Screen.Exercises.DragState

@Composable
fun DropZoneSection(
    items: List<MatchImageItem>,
    userMatches: Map<String, String>,
    selectedWord: String?,
    isSubmitted: Boolean,
    showCheckAnimation: Boolean = false,
    checkResults: Map<String, Boolean> = emptyMap(),
    onImageSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)
    ) {
        // 2 cột x 5 hàng cho 10 đáp án - layout đồng đều
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(10.dp), // Giảm spacing để đồng đều
            verticalArrangement = Arrangement.spacedBy(10.dp), // Spacing đồng đều
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 100.dp), // Đảm bảo height tối thiểu
            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp)
        ) {
            items(items) { item ->
                val matchedWordId = userMatches[item.id]
                val matchedWord = items.find { it.id == matchedWordId }
                val isCorrect = if (isSubmitted) {
                    matchedWordId == item.id
                } else false

                DropZoneCard(
                    item = item,
                    matchedWord = matchedWord,
                    isSubmitted = isSubmitted,
                    isCorrect = isCorrect,
                    showCheckAnimation = showCheckAnimation,
                    checkResults = checkResults,
                    selectedWord = selectedWord,
                    onImageSelected = onImageSelected
                )
            }
        }
    }
}

@Composable
fun DropZoneCard(
    item: MatchImageItem,
    matchedWord: MatchImageItem?,
    isSubmitted: Boolean,
    isCorrect: Boolean,
    showCheckAnimation: Boolean = false,
    checkResults: Map<String, Boolean> = emptyMap(),
    selectedWord: String?,
    onImageSelected: (String) -> Unit
) {
    val borderColor = when {
        showCheckAnimation && checkResults[item.id] == true -> Color(0xFF4CAF50)
        showCheckAnimation && checkResults[item.id] == false -> Color(0xFFF44336)
        isSubmitted && isCorrect -> Color(0xFF4CAF50)
        isSubmitted && !isCorrect -> Color(0xFFF44336)
        matchedWord != null -> Color(0xFF2196F3)
        else -> Color(0xFFBDBDBD)
    }

    val backgroundColor = when {
        showCheckAnimation && checkResults[item.id] == true -> Color(0xFFE8F5E8)
        showCheckAnimation && checkResults[item.id] == false -> Color(0xFFFFEBEE)
        matchedWord != null -> Color(0xFFE3F2FD)
        else -> Color(0xFFF5F5F5)
    }

    // Wordwall style - 2 cột đồng đều
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(0.88f) // Tỷ lệ cân đối hơn
            .clickable {
                if (selectedWord != null && !isSubmitted) {
                    onImageSelected(item.id)
                }
            }
        ,
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp), // Giảm border radius cho đồng đều
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp), // Shadow vừa phải
        border = BorderStroke(2.dp, borderColor) // Border vừa phải
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp) // Padding đồng đều
        ) {
            // Image - kích thước đồng đều
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                shape = RoundedCornerShape(8.dp), // Giảm border radius
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp) // Shadow nhẹ hơn
            ) {
                AsyncImage(
                    model = item.imageUrl,
                    contentDescription = item.word,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(8.dp)) // Spacing đồng đều

            // Drop zone - kích thước đồng đều
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp), // Height đồng đều
                colors = CardDefaults.cardColors(
                    containerColor = backgroundColor
                ),
                border = BorderStroke(1.5.dp, borderColor), // Border đồng đều
                shape = RoundedCornerShape(24.dp), // Pill shape đồng đều
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp) // Shadow nhẹ
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    if (matchedWord != null) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = matchedWord.word,
                                fontSize = 16.sp, // Tăng font size cho 2 cột
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1976D2),
                                textAlign = TextAlign.Center,
                                maxLines = 1
                            )

                            // Show check/cross icon during animation
                            if (showCheckAnimation && checkResults.containsKey(item.id)) {
                                Spacer(modifier = Modifier.width(8.dp)) // Tăng spacing
                                Icon(
                                    imageVector = if (checkResults[item.id] == true) Icons.Default.Check else Icons.Default.Close,
                                    contentDescription = null,
                                    tint = if (checkResults[item.id] == true) Color(0xFF4CAF50) else Color(0xFFF44336),
                                    modifier = Modifier.size(24.dp) // Tăng icon size
                                )
                            }
                        }
                    } else {
                        Text(
                            text = if (selectedWord != null) "Nhấn để chọn" else "Chọn từ trước",
                            fontSize = 14.sp, // Tăng font size cho 2 cột
                            fontWeight = FontWeight.Medium,
                            color = if (selectedWord != null) Color(0xFF2196F3) else Color(0xFF757575),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}
