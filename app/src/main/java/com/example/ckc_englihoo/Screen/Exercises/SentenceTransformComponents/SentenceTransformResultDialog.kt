package com.example.ckc_englihoo.Screen.Exercises.SentenceTransformComponents

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SentenceTransformResultDialog(
    correctAnswers: Int,
    totalQuestions: Int,
    onPlayAgain: () -> Unit,
    onExit: () -> Unit
) {
    val percentage = (correctAnswers * 100) / totalQuestions
    val resultColor = when {
        percentage >= 80 -> SentenceTransformColors.Correct
        percentage >= 60 -> Color(0xFFFF9800) // Orange
        else -> SentenceTransformColors.Incorrect
    }
    
    val resultMessage = when {
        percentage >= 80 -> "Xuất sắc! 🎉"
        percentage >= 60 -> "Khá tốt! 👍"
        else -> "Cần cố gắng thêm! 💪"
    }

    AlertDialog(
        onDismissRequest = onExit,
        containerColor = Color.White,
        shape = RoundedCornerShape(20.dp),
        title = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Kết quả bài tập",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = SentenceTransformColors.Primary,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Score circle
                Card(
                    modifier = Modifier.size(120.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = resultColor.copy(alpha = 0.1f)
                    ),
                    border = BorderStroke(4.dp, resultColor),
                    shape = RoundedCornerShape(60.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "$percentage%",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = resultColor
                            )
                            Text(
                                text = "$correctAnswers/$totalQuestions",
                                fontSize = 16.sp,
                                color = resultColor
                            )
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = resultMessage,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = resultColor,
                    textAlign = TextAlign.Center
                )
            }
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Bạn đã hoàn thành bài tập đảo ngữ!",
                    fontSize = 16.sp,
                    color = SentenceTransformColors.Text,
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp
                )
                
                if (percentage < 80) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Hãy thử lại để đạt kết quả tốt hơn nhé!",
                        fontSize = 14.sp,
                        color = SentenceTransformColors.TextSecondary,
                        textAlign = TextAlign.Center
                    )
                }
            }
        },
        confirmButton = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Play again button
                Button(
                    onClick = onPlayAgain,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SentenceTransformColors.Primary
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Làm lại",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                }
                
                // Exit button
                OutlinedButton(
                    onClick = onExit,
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = SentenceTransformColors.Primary
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(
                        width = 2.dp
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.ExitToApp,
                            contentDescription = "Exit",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Thoát",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        },
        dismissButton = null
    )
}
