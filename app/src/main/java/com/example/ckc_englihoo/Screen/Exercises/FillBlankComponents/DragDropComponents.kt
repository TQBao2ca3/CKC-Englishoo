package com.example.ckc_englihoo.Screen.Exercises.FillBlankComponents

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex



// Drop zone for answers (ô trống như trong hình) - cố định size
@Composable
fun DropZone(
    currentAnswer: String?,
    isCorrect: Boolean?,
    isSubmitted: Boolean,
    onDrop: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Cố định width dựa trên độ dài đáp án, minimum 100dp, maximum 160dp
    val dropZoneWidth = when {
        currentAnswer != null && currentAnswer.length > 6 -> 160.dp
        currentAnswer != null && currentAnswer.length > 4 -> 130.dp
        else -> 100.dp
    }

    Card(
        modifier = modifier
            .height(40.dp)
            .width(dropZoneWidth)  // Cố định width dựa trên content
            .clickable(enabled = !isSubmitted && currentAnswer != null) {
                // Allow removing answer by clicking
                onDrop()
            },
        colors = CardDefaults.cardColors(
            containerColor = when {
                isSubmitted && isCorrect == true -> Color(0xFF10B981).copy(alpha = 0.2f)
                isSubmitted && isCorrect == false -> Color(0xFFEF4444).copy(alpha = 0.2f)
                else -> Color.White
            }
        ),
        border = BorderStroke(
            width = 2.dp,
            color = when {
                isSubmitted && isCorrect == true -> Color(0xFF10B981)
                isSubmitted && isCorrect == false -> Color(0xFFEF4444)
                currentAnswer != null -> Color(0xFF2196F3)
                else -> Color.Black
            }
        ),
        shape = RoundedCornerShape(4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = currentAnswer ?: "",
                color = when {
                    isSubmitted && isCorrect == true -> Color(0xFF10B981)
                    isSubmitted && isCorrect == false -> Color(0xFFEF4444)
                    currentAnswer != null -> Color.Black
                    else -> Color.Gray
                },
                fontSize = 16.sp,  // Giảm từ 18.sp xuống 16.sp
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                maxLines = 1  // Đảm bảo chỉ 1 dòng
            )
        }
    }
}

// Sentence with drop zone (như hình: "Nam đi ___ biển.")
@Composable
fun SentenceWithDropZone(
    sentence: String,
    currentAnswer: String?,
    isCorrect: Boolean?,
    isSubmitted: Boolean,
    onDrop: () -> Unit
) {
    val parts = sentence.split("___")

    // Use Column for wrapping if sentence is too long
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Check if sentence is long and needs wrapping
        val isLongSentence = sentence.length > 20

        if (isLongSentence) {
            // For long sentences, use Column layout
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Text before blank
                if (parts.isNotEmpty() && parts[0].isNotEmpty()) {
                    Text(
                        text = parts[0].trim(),
                        fontSize = 20.sp,  // Giảm từ 24.sp xuống 20.sp
                        color = Color.Black,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center
                    )
                }

                // Drop zone
                DropZone(
                    currentAnswer = currentAnswer,
                    isCorrect = isCorrect,
                    isSubmitted = isSubmitted,
                    onDrop = onDrop
                )

                // Text after blank
                if (parts.size > 1 && parts[1].isNotEmpty()) {
                    Text(
                        text = parts[1].trim(),
                        fontSize = 20.sp,  // Giảm từ 24.sp xuống 20.sp
                        color = Color.Black,
                        fontWeight = FontWeight.Normal,
                        textAlign = TextAlign.Center
                    )
                }
            }
        } else {
            // For short sentences, use Row layout
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // Text before blank
                if (parts.isNotEmpty()) {
                    Text(
                        text = parts[0],
                        fontSize = 22.sp,  // Giảm từ 28.sp xuống 22.sp
                        color = Color.Black,
                        fontWeight = FontWeight.Normal
                    )
                }

                // Drop zone (ô trống để thả)
                DropZone(
                    currentAnswer = currentAnswer,
                    isCorrect = isCorrect,
                    isSubmitted = isSubmitted,
                    onDrop = onDrop,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )

                // Text after blank
                if (parts.size > 1) {
                    Text(
                        text = parts[1],
                        fontSize = 22.sp,  // Giảm từ 28.sp xuống 22.sp
                        color = Color.Black,
                        fontWeight = FontWeight.Normal
                    )
                }
            }
        }
    }
}



// Drag and drop display component
@Composable
fun FillBlankDragDropDisplay(
    question: FillBlankQuestion,
    currentAnswer: String?,
    isSubmitted: Boolean,
    isCurrentAnswerCorrect: Boolean?,
    onAnswerSelected: (String) -> Unit,
    onAnswerRemoved: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Options section ở trên (như hình: blue và red cards)
            Row(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                question.options.forEachIndexed { index, option ->
                    val isUsed = currentAnswer == option
                    val backgroundColor = if (index == 0) Color(0xFF2196F3) else Color(0xFFE53E3E)

                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                            .clickable(enabled = !isSubmitted && !isUsed) {
                                onAnswerSelected(option)
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = if (isUsed) backgroundColor.copy(alpha = 0.5f) else backgroundColor
                        ),
                        shape = RoundedCornerShape(4.dp),
                        border = BorderStroke(1.dp, Color.Black)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = option,
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            // Sentence với drop zone - layout như hình
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val parts = question.sentence.split("___")
                val textBefore = if (parts.isNotEmpty()) parts[0].trim() else ""
                val textAfter = if (parts.size > 1) parts[1].trim() else ""

                // Kiểm tra độ dài tổng để quyết định layout
                val totalLength = textBefore.length + textAfter.length
                val isShortSentence = totalLength <= 15  // Ngưỡng cho câu ngắn

                if (isShortSentence) {
                    // Layout cho câu ngắn: Tất cả cùng 1 hàng như hình
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        // Text trước blank
                        if (textBefore.isNotEmpty()) {
                            Text(
                                text = textBefore,
                                fontSize = 20.sp,
                                color = Color.Black,
                                fontWeight = FontWeight.Normal
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }

                        // Drop zone
                        Card(
                            modifier = Modifier
                                .width(120.dp)
                                .height(40.dp)
                                .clickable(enabled = !isSubmitted && currentAnswer != null) {
                                    onAnswerRemoved()
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = when {
                                    isSubmitted && isCurrentAnswerCorrect == true -> Color(0xFF10B981).copy(alpha = 0.2f)
                                    isSubmitted && isCurrentAnswerCorrect == false -> Color(0xFFEF4444).copy(alpha = 0.2f)
                                    else -> Color.White
                                }
                            ),
                            border = BorderStroke(
                                width = 2.dp,
                                color = when {
                                    isSubmitted && isCurrentAnswerCorrect == true -> Color(0xFF10B981)
                                    isSubmitted && isCurrentAnswerCorrect == false -> Color(0xFFEF4444)
                                    currentAnswer != null -> Color(0xFF2196F3)
                                    else -> Color.Black
                                }
                            ),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = currentAnswer ?: "",
                                    color = when {
                                        isSubmitted && isCurrentAnswerCorrect == true -> Color(0xFF10B981)
                                        isSubmitted && isCurrentAnswerCorrect == false -> Color(0xFFEF4444)
                                        currentAnswer != null -> Color.Black
                                        else -> Color.Gray
                                    },
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    textAlign = TextAlign.Center,
                                    maxLines = 1
                                )
                            }
                        }

                        // Text sau drop zone
                        if (textAfter.isNotEmpty()) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = textAfter,
                                fontSize = 20.sp,
                                color = Color.Black,
                                fontWeight = FontWeight.Normal
                            )
                        }
                    }
                } else {
                    // Layout cho câu dài: Text trước + drop zone ở hàng 1, text sau xuống hàng và canh giữa
                    // Hàng 1: Text trước + Drop zone
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        // Text trước blank
                        if (textBefore.isNotEmpty()) {
                            Text(
                                text = textBefore,
                                fontSize = 20.sp,
                                color = Color.Black,
                                fontWeight = FontWeight.Normal
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }

                        // Drop zone
                        Card(
                            modifier = Modifier
                                .width(120.dp)
                                .height(40.dp)
                                .clickable(enabled = !isSubmitted && currentAnswer != null) {
                                    onAnswerRemoved()
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = when {
                                    isSubmitted && isCurrentAnswerCorrect == true -> Color(0xFF10B981).copy(alpha = 0.2f)
                                    isSubmitted && isCurrentAnswerCorrect == false -> Color(0xFFEF4444).copy(alpha = 0.2f)
                                    else -> Color.White
                                }
                            ),
                            border = BorderStroke(
                                width = 2.dp,
                                color = when {
                                    isSubmitted && isCurrentAnswerCorrect == true -> Color(0xFF10B981)
                                    isSubmitted && isCurrentAnswerCorrect == false -> Color(0xFFEF4444)
                                    currentAnswer != null -> Color(0xFF2196F3)
                                    else -> Color.Black
                                }
                            ),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = currentAnswer ?: "",
                                    color = when {
                                        isSubmitted && isCurrentAnswerCorrect == true -> Color(0xFF10B981)
                                        isSubmitted && isCurrentAnswerCorrect == false -> Color(0xFFEF4444)
                                        currentAnswer != null -> Color.Black
                                        else -> Color.Gray
                                    },
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Medium,
                                    textAlign = TextAlign.Center,
                                    maxLines = 1
                                )
                            }
                        }
                    }

                    // Hàng 2: Text sau blank (xuống hàng và canh giữa)
                    if (textAfter.isNotEmpty()) {
                        Text(
                            text = textAfter,
                            fontSize = 20.sp,
                            color = Color.Black,
                            fontWeight = FontWeight.Normal,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}


