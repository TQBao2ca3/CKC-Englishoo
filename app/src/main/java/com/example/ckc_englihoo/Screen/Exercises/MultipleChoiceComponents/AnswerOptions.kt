package com.example.ckc_englihoo.Screen.Exercises.MultipleChoiceComponents

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AnswerOptions(
    options: List<String>,
    selectedAnswer: String,
    correctAnswer: String,
    showResults: Boolean,
    explanation: String,
    onAnswerSelected: (String) -> Unit,
    onExplanationToggle: (Boolean) -> Unit = {}
) {
    var showExplanation by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Options grid
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
        items(options.size) { index ->
            val option = options[index]
            val isSelected = selectedAnswer == option
            val isCorrectOption = option == correctAnswer
            val isWrongSelected = showResults && isSelected && !isCorrectOption

            // Animation for selection
            val scale by animateFloatAsState(
                targetValue = if (isSelected && !showResults) 0.95f else 1f,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                ),
                label = "scale"
            )

            // Colors based on state - Soft blue theme
            val backgroundColor = when {
                !showResults -> {
                    if (isSelected) Color(0xFF60A5FA) else Color(0xFFFAFBFF)
                }
                isCorrectOption -> Color(0xFF34D399)
                isWrongSelected -> Color(0xFFF87171)
                else -> Color(0xFFF1F5F9)
            }

            val textColor = when {
                !showResults -> {
                    if (isSelected) Color.White else Color(0xFF1E3A8A)
                }
                isCorrectOption -> Color.White
                isWrongSelected -> Color.White
                else -> Color(0xFF475569)
            }

            val borderColor = when {
                !showResults -> {
                    if (isSelected) Color(0xFF3B82F6) else Color(0xFFBFDBFE)
                }
                isCorrectOption -> Color(0xFF10B981)
                isWrongSelected -> Color(0xFFEF4444)
                else -> Color(0xFFCBD5E1)
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .scale(scale)
                    .clickable(enabled = !showResults) {
                        onAnswerSelected(option)
                    }
                    .shadow(
                        elevation = if (isSelected && !showResults) 16.dp else 8.dp,
                        shape = RoundedCornerShape(20.dp),
                        ambientColor = backgroundColor.copy(alpha = 0.3f)
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = backgroundColor
                ),
                border = BorderStroke(2.dp, borderColor),
                shape = RoundedCornerShape(20.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            brush = if (!showResults && isSelected) {
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFF3B82F6),
                                        Color(0xFF2563EB)
                                    )
                                )
                            } else if (showResults && isCorrectOption) {
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFF10B981),
                                        Color(0xFF059669)
                                    )
                                )
                            } else if (showResults && isWrongSelected) {
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFFEF4444),
                                        Color(0xFFDC2626)
                                    )
                                )
                            } else {
                                Brush.verticalGradient(
                                    colors = listOf(backgroundColor, backgroundColor)
                                )
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    ) {
                        // Option letter (A, B, C, D)
                        Card(
                            modifier = Modifier.size(36.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (!showResults && isSelected) Color.White.copy(alpha = 0.25f)
                                else if (showResults && (isCorrectOption || isWrongSelected)) Color.White.copy(alpha = 0.25f)
                                else Color(0xFF93C5FD).copy(alpha = 0.3f)
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${'A' + index}",
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textColor
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        // Option text
                        Text(
                            text = option,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = textColor,
                            textAlign = TextAlign.Center,
                            maxLines = 2,
                            modifier = Modifier.weight(1f)
                        )

                        // Result icon
                        if (showResults && (isCorrectOption || isWrongSelected)) {
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = if (isCorrectOption) Icons.Default.Check else Icons.Default.Close,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }
        }
        }

        // Show explanation toggle button when results are visible AND user answered incorrectly
        val userAnsweredIncorrectly = showResults && selectedAnswer.isNotEmpty() && selectedAnswer != correctAnswer
        if (userAnsweredIncorrectly && explanation.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))

            // Toggle button for explanation
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        showExplanation = !showExplanation
                        onExplanationToggle(showExplanation)
                    }
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(16.dp),
                        ambientColor = Color(0xFF3B82F6).copy(alpha = 0.1f)
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = if (showExplanation) Color(0xFFF0F8FF) else Color(0xFFE0F2FE)
                ),
                border = BorderStroke(2.dp, Color(0xFF3B82F6).copy(alpha = 0.4f)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "💡",
                            fontSize = 18.sp,
                            modifier = Modifier.padding(end = 8.dp)
                        )
                        Text(
                            text = if (showExplanation) "Ẩn giải thích" else "Xem giải thích",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E40AF)
                        )
                    }

                    Icon(
                        imageVector = if (showExplanation) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = Color(0xFF3B82F6),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Show explanation content when expanded
            if (showExplanation) {
                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(
                            elevation = 12.dp,
                            shape = RoundedCornerShape(20.dp),
                            ambientColor = Color(0xFF3B82F6).copy(alpha = 0.15f)
                        ),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF0F8FF)
                    ),
                    border = BorderStroke(2.dp, Color(0xFF87CEEB).copy(alpha = 0.6f)),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "Giải thích đáp án đúng:",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E40AF),
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        Text(
                            text = explanation,
                            fontSize = 15.sp,
                            color = Color(0xFF1E3A8A),
                            lineHeight = 22.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}
