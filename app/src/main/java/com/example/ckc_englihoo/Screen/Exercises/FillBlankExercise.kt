package com.example.ckc_englihoo.Screen.Exercises

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ckc_englihoo.Screen.Exercises.FillBlankComponents.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FillBlankExercise(navController: NavController, exerciseTitle: String = "Điền từ vào chỗ trống") {
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var userAnswers by remember { mutableStateOf(mutableMapOf<Int, String>()) }
    var isSubmitted by remember { mutableStateOf(false) }
    var showExplanation by remember { mutableStateOf(false) }
    var showResultDialog by remember { mutableStateOf(false) }
    
    // Drag and drop state
    var draggedWord by remember { mutableStateOf<String?>(null) }
    var dragOffset by remember { mutableStateOf(Offset.Zero) }
    
    val questions = remember { sampleFillBlankQuestions }
    val currentQuestion = questions[currentQuestionIndex]
    val currentAnswer = userAnswers[currentQuestion.id]
    val usedAnswers = userAnswers.values.toSet()
    
    // Check if current answer is correct (only when submitted)
    val isCurrentAnswerCorrect = if (isSubmitted) {
        currentAnswer == currentQuestion.correctAnswer
    } else null

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = exerciseTitle,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
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
                    containerColor = Color(0xFF2563EB)
                )
            )
        },
        containerColor = Color(0xFF0F172A)
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFEBF4FF),
                            Color(0xFFDBEAFE),
                            Color(0xFFBFDBFE),
                            Color(0xFF93C5FD)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(20.dp)
            ) {
                // Question counter - centered black text (no X icon)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Câu ${currentQuestionIndex + 1}/${questions.size}",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Drag and drop exercise display
                FillBlankDragDropDisplay(
                    question = currentQuestion,
                    currentAnswer = currentAnswer,
                    isSubmitted = isSubmitted,
                    isCurrentAnswerCorrect = isCurrentAnswerCorrect,
                    onAnswerSelected = { answer ->
                        if (!isSubmitted) {
                            userAnswers = userAnswers.toMutableMap().apply {
                                put(currentQuestion.id, answer)
                            }
                        }
                    },
                    onAnswerRemoved = {
                        if (!isSubmitted) {
                            userAnswers = userAnswers.toMutableMap().apply {
                                remove(currentQuestion.id)
                            }
                        }
                    }
                )

                // Explanation section - chỉ hiện khi sai như MultipleChoice
                val userAnsweredIncorrectly = isSubmitted && currentAnswer != null && currentAnswer != currentQuestion.correctAnswer
                if (userAnsweredIncorrectly && currentQuestion.explanation.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))

                    // Toggle button for explanation
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                showExplanation = !showExplanation
                            },
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
                            modifier = Modifier.fillMaxWidth(),
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
                                    text = currentQuestion.explanation,
                                    fontSize = 15.sp,
                                    color = Color(0xFF1E3A8A),
                                    lineHeight = 22.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Navigation buttons like MultipleChoice - hide when explanation is showing
                if (!showExplanation) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // Submit/Continue button
                        if (!isSubmitted) {
                            if (currentAnswer != null) {
                                Button(
                                    onClick = {
                                        if (userAnswers.size == questions.size) {
                                            isSubmitted = true
                                        } else if (currentQuestionIndex < questions.size - 1) {
                                            currentQuestionIndex++
                                            showExplanation = false
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(56.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (userAnswers.size == questions.size) Color(0xFF059669) else Color(0xFF2563EB)
                                    ),
                                    shape = RoundedCornerShape(16.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            imageVector = if (userAnswers.size == questions.size) Icons.Default.Send else Icons.Default.ArrowForward,
                                            contentDescription = null,
                                            tint = Color.White,
                                            modifier = Modifier.size(20.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = if (userAnswers.size == questions.size) "Nộp bài" else "Tiếp theo",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        } else {
                            // Complete button (show after submitting at all questions)
                            Button(
                                onClick = {
                                    showResultDialog = true
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF4CAF50)
                                ),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = Color.White,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Hoàn thành",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }

                        // Navigation buttons row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            // Previous button
                            OutlinedButton(
                                onClick = {
                                    if (currentQuestionIndex > 0) {
                                        currentQuestionIndex--
                                        showExplanation = false
                                    }
                                },
                                enabled = currentQuestionIndex > 0,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = if (currentQuestionIndex > 0) Color(0xFF1E40AF) else Color.Gray,
                                    disabledContentColor = Color.Gray
                                ),
                                border = BorderStroke(
                                    width = 2.dp,
                                    color = if (currentQuestionIndex > 0) Color(0xFF3B82F6) else Color.Gray
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowBack,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Trước",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }

                            // Next button (only for navigation)
                            OutlinedButton(
                                onClick = {
                                    if (currentQuestionIndex < questions.size - 1) {
                                        currentQuestionIndex++
                                        showExplanation = false
                                    }
                                },
                                enabled = currentQuestionIndex < questions.size - 1,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(48.dp),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = if (currentQuestionIndex < questions.size - 1) Color(0xFF1E40AF) else Color.Gray,
                                    disabledContentColor = Color.Gray
                                ),
                                border = BorderStroke(
                                    width = 2.dp,
                                    color = if (currentQuestionIndex < questions.size - 1) Color(0xFF3B82F6) else Color.Gray
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Sau",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(
                                        imageVector = Icons.Default.ArrowForward,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // Show result dialog when completed
        if (showResultDialog) {
            FillBlankResultDialog(
                userAnswers = userAnswers,
                questions = questions,
                onDismiss = {
                    showResultDialog = false
                    navController.popBackStack()
                },
                onRestart = {
                    showResultDialog = false
                    userAnswers = mutableMapOf()
                    currentQuestionIndex = 0
                    isSubmitted = false
                    showExplanation = false
                }
            )
        }
    }
}
