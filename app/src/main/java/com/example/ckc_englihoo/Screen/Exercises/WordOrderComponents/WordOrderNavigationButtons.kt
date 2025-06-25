package com.example.ckc_englihoo.Screen.Exercises.WordOrderComponents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WordOrderNavigationButtons(
    currentQuestionIndex: Int,
    totalQuestions: Int,
    onPrevious: () -> Unit,
    onNext: () -> Unit
) {
    // Chỉ có navigation buttons row - Trước/Sau
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Navigation buttons - luôn có thể bấm để di chuyển qua lại
            // Previous button - giống như Đảo ngữ
            OutlinedButton(
                onClick = onPrevious,
                enabled = currentQuestionIndex > 0,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = if (currentQuestionIndex == 0) Color.Gray else Color(0xFF2196F3)
                ),
                border = BorderStroke(
                    2.dp,
                    if (currentQuestionIndex == 0) Color.Gray else Color(0xFF2196F3)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "Trước",
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            // Next button - giống như Đảo ngữ
            OutlinedButton(
                onClick = onNext,
                enabled = currentQuestionIndex < totalQuestions - 1,
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = if (currentQuestionIndex == totalQuestions - 1) Color.Gray else Color(0xFF2196F3)
                ),
                border = BorderStroke(
                    2.dp,
                    if (currentQuestionIndex == totalQuestions - 1) Color.Gray else Color(0xFF2196F3)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "Sau",
                        fontWeight = FontWeight.Medium
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }

