package com.example.ckc_englihoo.Screen.Exercises.WordOrderComponents

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WordOrderInstructionsDialog(onDismiss: () -> Unit) {
    // Bottom sheet overlay - không che BottomAppBar
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom =130.dp) // Padding để không che BottomAppBar
            .background(Color.Black.copy(alpha = 0.5f))
    ) {
        // Bottom sheet content
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(0.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFF0F8FF)
            ),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Handle bar
                Box(
                    modifier = Modifier
                        .width(40.dp)
                        .height(4.dp)
                        .background(
                            Color(0xFF93C5FD),
                            RoundedCornerShape(2.dp)
                        )
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Header
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Help,
                        contentDescription = null,
                        tint = Color(0xFF3B82F6),
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Hướng dẫn sắp xếp từ",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E40AF)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Instructions
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    InstructionStep(
                        number = "1",
                        text = "Nhấn vào các từ bên dưới để tạo câu",
                        color = Color(0xFF3B82F6)
                    )
                    InstructionStep(
                        number = "2",
                        text = "Từ được chọn sẽ xuất hiện ở khu vực trên",
                        color = Color(0xFF059669)
                    )
                    InstructionStep(
                        number = "3",
                        text = "Nhấn vào từ ở trên để bỏ chọn",
                        color = Color(0xFFDC2626)
                    )
                    InstructionStep(
                        number = "4",
                        text = "Có từ thừa để làm khó - chọn đúng từ cần thiết",
                        color = Color(0xFFEA580C)
                    )
                    InstructionStep(
                        number = "5",
                        text = "Làm đủ 10 câu rồi nhấn nộp bài",
                        color = Color(0xFF7C3AED)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Close button
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3B82F6)
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Đã hiểu",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun InstructionStep(
    number: String,
    text: String,
    color: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Step number circle
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    color.copy(alpha = 0.1f),
                    RoundedCornerShape(20.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = number,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Text
        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF1E3A8A),
            modifier = Modifier.weight(1f)
        )
    }
}
