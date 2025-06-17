package com.example.ckc_englihoo.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

// Data class cho điểm số
data class GradeItem(
    val id: String,
    val title: String,
    val description: String,
    val currentScore: Int,
    val maxScore: Int,
    val type: GradeType,
    val date: String
)

enum class GradeType {
    HOMEWORK, QUIZ, EXAM, PARTICIPATION
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentGradingTeacher(
    navController: NavController,
    studentId: String,
    studentName: String
) {
    // Sample grade data
    val gradeItems = remember {
        listOf(
            GradeItem("1", "Bài tập về nhà 1", "Unit 1: Greetings", 85, 100, GradeType.HOMEWORK, "15/11/2024"),
            GradeItem("2", "Kiểm tra 15 phút", "Grammar: Present Simple", 0, 100, GradeType.QUIZ, "18/11/2024"),
            GradeItem("3", "Bài tập về nhà 2", "Unit 2: Family", 92, 100, GradeType.HOMEWORK, "20/11/2024"),
            GradeItem("4", "Tham gia lớp học", "Participation Week 1", 0, 100, GradeType.PARTICIPATION, "22/11/2024"),
            GradeItem("5", "Kiểm tra giữa kỳ", "Mid-term Exam", 0, 100, GradeType.EXAM, "25/11/2024"),
            GradeItem("6", "Bài tập về nhà 3", "Unit 3: Daily Activities", 0, 100, GradeType.HOMEWORK, "27/11/2024")
        )
    }

    // Calculate total score
    val totalScore = gradeItems.sumOf { it.currentScore }
    val maxTotalScore = gradeItems.sumOf { it.maxScore }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF1976D2), // Dark blue
                        Color(0xFF2196F3)  // Blue
                    )
                )
            )
    ) {
        // TopAppBar
        TopAppBar(
            title = {
                Text(
                    text = "Cho điểm - $studentName",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            navigationIcon = {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )

        // Content
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Summary Card
            item {
                SummaryCard(
                    studentName = studentName,
                    totalScore = totalScore,
                    maxTotalScore = maxTotalScore
                )
            }

            // Grade Items
            items(gradeItems) { gradeItem ->
                GradeItemCard(
                    gradeItem = gradeItem,
                    onScoreUpdate = { newScore ->
                        // TODO: Update score in repository
                        println("DEBUG: Updated ${gradeItem.title} score to $newScore")
                    }
                )
            }

            // Bottom spacing
            item {
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}

@Composable
fun SummaryCard(
    studentName: String,
    totalScore: Int,
    maxTotalScore: Int
) {
    val percentage = if (maxTotalScore > 0) (totalScore * 100) / maxTotalScore else 0

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Student info
            Text(
                text = studentName,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1976D2),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Score display
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "$totalScore",
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2196F3)
                )
                Text(
                    text = "/$maxTotalScore",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF666666)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Percentage
            Text(
                text = "$percentage%",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = when {
                    percentage >= 80 -> Color(0xFF4CAF50) // Green
                    percentage >= 60 -> Color(0xFFFF9800) // Orange
                    else -> Color(0xFFF44336) // Red
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Progress bar
            LinearProgressIndicator(
                progress = { percentage / 100f },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = Color(0xFF2196F3),
                trackColor = Color(0xFFE3F2FD)
            )
        }
    }
}

@Composable
fun GradeItemCard(
    gradeItem: GradeItem,
    onScoreUpdate: (Int) -> Unit
) {
    var showScoreDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = gradeItem.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333)
                    )
                    Text(
                        text = gradeItem.description,
                        fontSize = 14.sp,
                        color = Color(0xFF666666)
                    )
                    Text(
                        text = gradeItem.date,
                        fontSize = 12.sp,
                        color = Color(0xFF999999)
                    )
                }

                // Score display and edit button
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${gradeItem.currentScore}/${gradeItem.maxScore}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2196F3)
                    )
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    IconButton(
                        onClick = { showScoreDialog = true }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit score",
                            tint = Color(0xFF2196F3)
                        )
                    }
                }
            }

            // Type indicator
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = when (gradeItem.type) {
                    GradeType.HOMEWORK -> "Bài tập về nhà"
                    GradeType.QUIZ -> "Kiểm tra"
                    GradeType.EXAM -> "Thi"
                    GradeType.PARTICIPATION -> "Tham gia"
                },
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                modifier = Modifier
                    .background(
                        color = when (gradeItem.type) {
                            GradeType.HOMEWORK -> Color(0xFF4CAF50)
                            GradeType.QUIZ -> Color(0xFFFF9800)
                            GradeType.EXAM -> Color(0xFFF44336)
                            GradeType.PARTICIPATION -> Color(0xFF9C27B0)
                        },
                        shape = RoundedCornerShape(12.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }

    // Score Edit Dialog
    if (showScoreDialog) {
        ScoreEditDialog(
            gradeItem = gradeItem,
            onDismiss = { showScoreDialog = false },
            onConfirm = { newScore ->
                onScoreUpdate(newScore)
                showScoreDialog = false
            }
        )
    }
}

@Composable
fun ScoreEditDialog(
    gradeItem: GradeItem,
    onDismiss: () -> Unit,
    onConfirm: (Int) -> Unit
) {
    var scoreText by remember { mutableStateOf(gradeItem.currentScore.toString()) }
    var isValidScore by remember { mutableStateOf(true) }

    // Validate score
    LaunchedEffect(scoreText) {
        val score = scoreText.toIntOrNull()
        isValidScore = score != null && score >= 0 && score <= gradeItem.maxScore
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Chỉnh sửa điểm",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1976D2),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Column {
                Text(
                    text = gradeItem.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF333333),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = "Điểm tối đa: ${gradeItem.maxScore}",
                    fontSize = 14.sp,
                    color = Color(0xFF666666),
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = scoreText,
                    onValueChange = { scoreText = it },
                    label = { Text("Điểm số") },
                    placeholder = { Text("Nhập điểm từ 0 đến ${gradeItem.maxScore}") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF2196F3),
                        unfocusedBorderColor = Color(0xFFBBDEFB),
                        focusedLabelColor = Color(0xFF2196F3),
                        unfocusedLabelColor = Color.Gray,
                        cursorColor = Color(0xFF2196F3)
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    isError = !isValidScore
                )

                if (!isValidScore) {
                    Text(
                        text = "Vui lòng nhập điểm từ 0 đến ${gradeItem.maxScore}",
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val score = scoreText.toIntOrNull() ?: 0
                    onConfirm(score)
                },
                enabled = isValidScore,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2196F3)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Lưu",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFF666666)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Hủy",
                    fontWeight = FontWeight.Medium
                )
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(16.dp)
    )
}
