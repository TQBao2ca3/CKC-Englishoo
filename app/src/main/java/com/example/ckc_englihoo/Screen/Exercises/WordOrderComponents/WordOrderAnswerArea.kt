package com.example.ckc_englihoo.Screen.Exercises.WordOrderComponents

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import kotlinx.coroutines.delay

@Composable
fun WordOrderAnswerArea(
    selectedWords: List<String>,
    wordColorMap: Map<String, Color>,
    hasSubmittedAnswers: Boolean,
    questionResults: Map<Int, Boolean>,
    currentQuestionIndex: Int,
    onWordRemoved: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 120.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        border = BorderStroke(2.dp, Color(0xFFE0E0E0)),
        shape = RoundedCornerShape(12.dp)
    ) {
        if (selectedWords.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Nhấn vào từ để thêm vào câu",
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
            }
        } else {
            // FlowRow for wrapping words with scale animation and colors
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Group words into rows based on text length
                val wordRows = mutableListOf<List<String>>()
                var currentRow = mutableListOf<String>()
                var currentRowLength = 0
                
                selectedWords.forEach { wordWithId ->
                    val displayWord = wordWithId.split("#")[0]
                    val wordLength = displayWord.length
                    
                    // If adding this word would make the row too long, start a new row
                    if (currentRowLength + wordLength > 20 && currentRow.isNotEmpty()) {
                        wordRows.add(currentRow.toList())
                        currentRow.clear()
                        currentRowLength = 0
                    }
                    
                    currentRow.add(wordWithId)
                    currentRowLength += wordLength + 2 // +2 for spacing
                }
                
                if (currentRow.isNotEmpty()) {
                    wordRows.add(currentRow.toList())
                }
                
                wordRows.forEach { rowWords ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowWords.forEach { wordWithId ->
                            WordOrderWordCard(
                                wordWithId = wordWithId,
                                wordColorMap = wordColorMap,
                                hasSubmittedAnswers = hasSubmittedAnswers,
                                questionResults = questionResults,
                                currentQuestionIndex = currentQuestionIndex,
                                isSelected = true,
                                onClick = { onWordRemoved(wordWithId) }
                            )
                        }
                    }
                }
            }
        }
    }
}
