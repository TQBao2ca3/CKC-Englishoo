package com.example.ckc_englihoo.Screen.Exercises.WordOrderComponents

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*

// Question counter component
@Composable
fun WordOrderQuestionCounter(
    currentQuestion: Int,
    totalQuestions: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Câu $currentQuestion/$totalQuestions",
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.Black
        )
    }
}

// Main game display component
@Composable
fun WordOrderGameDisplay(
    question: WordOrderQuestion,
    selectedWords: List<String>,
    selectedWordsWithColors: List<Pair<String, Int>>,
    availableWords: List<String>,
    onWordSelected: (String, Int) -> Unit,
    onWordRemoved: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = WordOrderColors.CardBackground),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Question text
            Text(
                text = question.question,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = WordOrderColors.Text,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Selected words area (answer area)
            SelectedWordsArea(
                selectedWordsWithColors = selectedWordsWithColors,
                onWordRemoved = onWordRemoved
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Available words (scrambled words with extras)
            AvailableWordsSection(
                availableWords = availableWords,
                allWords = question.scrambledWords,
                onWordSelected = onWordSelected
            )
            

        }
    }
}

// Selected words area (where user builds the sentence)
@Composable
fun SelectedWordsArea(
    selectedWordsWithColors: List<Pair<String, Int>>,
    onWordRemoved: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp), // Khôi phục lại height cố định
        colors = CardDefaults.cardColors(
            containerColor = WordOrderColors.SelectedArea
        ),
        border = BorderStroke(
            2.dp,
            WordOrderColors.BorderLight
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        if (selectedWordsWithColors.isEmpty()) {
            // Empty state
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Nhấn vào từ bên dưới để tạo câu",
                    fontSize = 16.sp,
                    color = WordOrderColors.TextSecondary,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            // Show selected words with colors - khôi phục lại logic cũ
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Chia words thành các hàng, mỗi hàng tối đa 3 words
                    selectedWordsWithColors.chunked(3).forEach { rowWords ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            rowWords.forEach { (word, colorIndex) ->
                                AnimatedVisibility(
                                    visible = true,
                                    enter = slideInHorizontally() + fadeIn(),
                                    exit = slideOutHorizontally() + fadeOut()
                                ) {
                                    SelectedWordChip(
                                        word = word,
                                        colorIndex = colorIndex,
                                        onClick = { onWordRemoved(word) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// Individual selected word chip with original color
@Composable
fun SelectedWordChip(
    word: String,
    colorIndex: Int,
    onClick: () -> Unit
) {
    // Same colors as available words
    val wordColors = listOf(
        Color(0xFF3B82F6), // Blue
        Color(0xFF059669), // Green
        Color(0xFFDC2626), // Red
        Color(0xFFEA580C), // Orange
        Color(0xFF7C3AED), // Purple
        Color(0xFFDB2777), // Pink
        Color(0xFF0891B2), // Cyan
        Color(0xFF65A30D), // Lime
        Color(0xFFC2410C), // Orange-red
        Color(0xFF9333EA)  // Violet
    )

    val wordColor = if (colorIndex >= 0 && colorIndex < wordColors.size) {
        wordColors[colorIndex]
    } else {
        WordOrderColors.WordSelected // Fallback color
    }

    Card(
        modifier = Modifier.clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = wordColor
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Text(
            text = word,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            maxLines = 1
        )
    }
}

// Available words section
@Composable
fun AvailableWordsSection(
    availableWords: List<String>,
    allWords: List<String>, // Original scrambled words to get correct color index
    onWordSelected: (String, Int) -> Unit
) {
    Column {
        Text(
            text = "Nhấn vào từ để thêm vào câu:",
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = WordOrderColors.TextSecondary,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Words in 3 columns grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.height(160.dp)
        ) {
            items(availableWords.size) { index ->
                val word = availableWords[index]
                // Find original index in all words for correct color
                val originalIndex = allWords.indexOf(word)
                AvailableWordButton(
                    word = word,
                    wordIndex = originalIndex,
                    onClick = { onWordSelected(word, originalIndex) }
                )
            }
        }
    }
}

// Individual available word button with colors
@Composable
fun AvailableWordButton(
    word: String,
    wordIndex: Int,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }

    // Different colors for each word
    val wordColors = listOf(
        Color(0xFF3B82F6), // Blue
        Color(0xFF059669), // Green
        Color(0xFFDC2626), // Red
        Color(0xFFEA580C), // Orange
        Color(0xFF7C3AED), // Purple
        Color(0xFFDB2777), // Pink
        Color(0xFF0891B2), // Cyan
        Color(0xFF65A30D), // Lime
        Color(0xFFC2410C), // Orange-red
        Color(0xFF9333EA)  // Violet
    )

    val wordColor = wordColors[wordIndex % wordColors.size]

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                isPressed = true
                onClick()
            }
            .animateContentSize(),
        colors = CardDefaults.cardColors(
            containerColor = wordColor
        ),
        border = BorderStroke(
            1.dp,
            wordColor.copy(alpha = 0.8f)
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isPressed) 8.dp else 4.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = word,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }

    // Reset pressed state
    LaunchedEffect(isPressed) {
        if (isPressed) {
            kotlinx.coroutines.delay(100)
            isPressed = false
        }
    }
}




