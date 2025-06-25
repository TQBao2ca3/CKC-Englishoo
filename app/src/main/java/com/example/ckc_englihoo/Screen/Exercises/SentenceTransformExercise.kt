package com.example.ckc_englihoo.Screen.Exercises

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.ckc_englihoo.Screen.Exercises.SentenceTransformComponents.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SentenceTransformExercise(navController: NavController, exerciseTitle: String = "Đảo ngữ") {
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var userAnswers by remember { mutableStateOf(mutableMapOf<Int, List<String>>()) }
    var isSubmitted by remember { mutableStateOf(false) }
    var showExplanation by remember { mutableStateOf(false) }
    var showResultDialog by remember { mutableStateOf(false) }
    var showInstructions by remember { mutableStateOf(false) }
    var showGameMenu by remember { mutableStateOf(false) }
    var showCompleteButton by remember { mutableStateOf(false) }

    // Track mapping giữa answer slots và letter indices để xử lý chữ cái trùng
    var letterMapping by remember { mutableStateOf(mutableMapOf<Int, List<Int>>()) }

    val questions = remember {
        sampleSentenceTransformQuestions.map { question ->
            question.copy(letters = question.letters.shuffled()) // Đảo lộn xộn các chữ cái
        }
    }
    val currentQuestion = questions[currentQuestionIndex]
    val currentAnswer = userAnswers[currentQuestion.id] ?: emptyList()

    // Check if all questions are answered - yêu cầu đáp án đầy đủ
    val allQuestionsAnswered = questions.all { question ->
        val answer = userAnswers[question.id] ?: emptyList()
        answer.size == question.correctAnswer.length // Phải chọn đủ số từ theo đáp án đúng
    }

    // Check if current question is complete
    val isCurrentQuestionComplete = currentAnswer.size == currentQuestion.correctAnswer.length

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
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = exerciseTitle,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            fontSize = 20.sp
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
                        containerColor = Color(0xFF2196F3)
                    )
                )
            },
            bottomBar = {
                if (!showExplanation) {
                    BottomAppBar(containerColor = Color.White, tonalElevation = 4.dp) {
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = { showInstructions = !showInstructions },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
                            ) {
                                Text("Hướng dẫn", color = Color.White)
                            }
                            IconButton(onClick = { showGameMenu = !showGameMenu }) {
                                Icon(
                                    Icons.Default.MoreVert,
                                    contentDescription = null,
                                    tint = Color(0xFF2196F3)
                                )
                            }
                        }
                    }
                }
            },
            containerColor = Color.Transparent
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                // Top spacing
                Spacer(modifier = Modifier.height(16.dp))

                // Question counter
                Text(
                    text = "Câu ${currentQuestionIndex + 1}/${questions.size}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 20.dp)
                )

                // Image display - to hơn và ở phía trên
                Card(
                    modifier = Modifier
                        .size(220.dp)
                        .padding(bottom = 32.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    border = BorderStroke(2.dp, Color(0xFF2196F3)),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        // Kiểm tra xem imageUrl có phải là emoji hay URL
                        if (currentQuestion.imageUrl.startsWith("http")) {
                            // Hiển thị ảnh từ URL
                            AsyncImage(
                                model = currentQuestion.imageUrl,
                                contentDescription = "Question image",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            // Hiển thị emoji
                            Text(
                                text = currentQuestion.imageUrl,
                                fontSize = 60.sp,
                                textAlign = TextAlign.Center,
                                color = Color(0xFF2196F3)
                            )
                        }
                    }
                }

                // Available letters section
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 24.dp)
                ) {
                    currentQuestion.letters.forEachIndexed { letterIndex, letter ->
                        val letterColors = remember {
                            listOf(
                                Color(0xFF2196F3), // Blue
                                Color(0xFF4CAF50), // Green
                                Color(0xFFFF9800), // Orange
                                Color(0xFFF44336), // Red
                                Color(0xFF9C27B0), // Purple
                                Color(0xFF00BCD4)  // Cyan
                            )
                        }
                        val color = letterColors[letterIndex % letterColors.size]

                        // Kiểm tra xem letter này đã được sử dụng chưa
                        val currentMapping = letterMapping[currentQuestion.id] ?: emptyList()
                        val isUsed = letterIndex in currentMapping

                        CompactLetterCard(
                            letter = letter,
                            color = color,
                            isUsed = isUsed,
                            onClick = {
                                if (!isSubmitted && !isUsed) {
                                    val newAnswer = currentAnswer + letter
                                    val newMapping = currentMapping + letterIndex

                                    userAnswers = userAnswers.toMutableMap().apply {
                                        put(currentQuestion.id, newAnswer)
                                    }
                                    letterMapping = letterMapping.toMutableMap().apply {
                                        put(currentQuestion.id, newMapping)
                                    }
                                }
                            }
                        )
                    }
                }

                // Answer slots section
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(currentQuestion.correctAnswer.length) { slotIndex ->
                        val letter = if (slotIndex < currentAnswer.size) currentAnswer[slotIndex] else null
                        val currentMapping = letterMapping[currentQuestion.id] ?: emptyList()

                        // Tìm màu sắc dựa trên mapping
                        val letterColor = if (letter != null && slotIndex < currentMapping.size) {
                            val letterIndex = currentMapping[slotIndex]
                            val letterColors = listOf(
                                Color(0xFF2196F3), Color(0xFF4CAF50), Color(0xFFFF9800),
                                Color(0xFFF44336), Color(0xFF9C27B0), Color(0xFF00BCD4)
                            )
                            letterColors[letterIndex % letterColors.size]
                        } else null

                        val isCorrect = if (isSubmitted && letter != null) {
                            letter == currentQuestion.correctAnswer[slotIndex].toString()
                        } else null

                        CompactAnswerSlot(
                            letter = letter,
                            letterColor = letterColor,
                            isCorrect = isCorrect,
                            isSubmitted = isSubmitted,
                            onClick = {
                                if (!isSubmitted && letter != null) {
                                    // Remove letter và mapping tương ứng
                                    val newAnswer = currentAnswer.toMutableList()
                                    val newMapping = currentMapping.toMutableList()

                                    newAnswer.removeAt(slotIndex)
                                    newMapping.removeAt(slotIndex)

                                    userAnswers = userAnswers.toMutableMap().apply {
                                        put(currentQuestion.id, newAnswer)
                                    }
                                    letterMapping = letterMapping.toMutableMap().apply {
                                        put(currentQuestion.id, newMapping)
                                    }
                                }
                            }
                        )
                    }
                }

                // Continue/Next/Submit button when question is complete
                if (isCurrentQuestionComplete && !isSubmitted) {
                    Spacer(modifier = Modifier.height(20.dp))

                    val isLastQuestion = currentQuestionIndex == questions.size - 1
                    val canSubmit = isLastQuestion && allQuestionsAnswered // Ở câu cuối và đã trả lời tất cả câu

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                if (isLastQuestion && canSubmit) {
                                    // Nộp bài
                                    isSubmitted = true
                                    showCompleteButton = true
                                } else if (currentQuestionIndex < questions.size - 1) {
                                    // Tiếp theo
                                    currentQuestionIndex++
                                }
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF3B82F6)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                if (isLastQuestion && canSubmit) {
                                    Icon(
                                        imageVector = Icons.Default.Send,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = "Nộp bài",
                                        color = Color.White,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 16.sp
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.ArrowForward,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Text(
                                        text = "Tiếp theo",
                                        color = Color.White,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 16.sp
                                    )
                                }
                            }
                        }
                    }
                }

                // Complete button after submission
                if (showCompleteButton && isSubmitted) {
                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                showCompleteButton = false
                                showResultDialog = true
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(48.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4CAF50)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Text(
                                    text = "Hoàn thành",
                                    color = Color.White,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 16.sp
                                )
                            }
                        }
                    }
                }

                // Explanation section - nằm giữa button Tiếp theo và navigation buttons
                val currentUserAnswer = userAnswers[currentQuestion.id] ?: emptyList()
                val userAnsweredIncorrectly = isSubmitted && currentUserAnswer.joinToString("") != currentQuestion.correctAnswer

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
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(max = 300.dp), // Tăng chiều cao tối đa để hiển thị nhiều nội dung hơn
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFFF0F8FF)
                            ),
                            border = BorderStroke(2.dp, Color(0xFF87CEEB).copy(alpha = 0.6f)),
                            shape = RoundedCornerShape(20.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(20.dp)
                                    .verticalScroll(rememberScrollState()) // Cuộn dọc khi nội dung dài
                            ) {
                                Text(
                                    text = "Giải thích đáp án đúng:",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1E40AF),
                                    modifier = Modifier.padding(bottom = 12.dp)
                                )

                                Text(
                                    text = "Đáp án đúng: ${currentQuestion.correctAnswer}",
                                    fontSize = 15.sp,
                                    color = Color(0xFF1E3A8A),
                                    lineHeight = 22.sp,
                                    fontWeight = FontWeight.Medium,
                                    modifier = Modifier.padding(bottom = 8.dp)
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

                Spacer(modifier = Modifier.height(16.dp))

                // Navigation buttons at bottom - ẩn khi có giải thích
                if (!showExplanation) {
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
                                Text("Trước", fontWeight = FontWeight.Medium)
                            }
                        }

                        // Next button
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
                                contentColor = if (currentQuestionIndex == questions.size - 1) Color.Gray else Color(0xFF2196F3)
                            ),
                            border = BorderStroke(
                                2.dp,
                                if (currentQuestionIndex == questions.size - 1) Color.Gray else Color(0xFF2196F3)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text("Sau", fontWeight = FontWeight.Medium)
                                Icon(
                                    imageVector = Icons.Default.ArrowForward,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Instructions bottom sheet
            if (showInstructions) {
                SentenceTransformInstructionsDialog(
                    onDismiss = { showInstructions = false }
                )
            }

            // Game menu bottom sheet
            if (showGameMenu) {
                SentenceTransformGameOptionsMenu(
                    onRestart = {
                        userAnswers = mutableMapOf()
                        letterMapping = mutableMapOf()
                        currentQuestionIndex = 0
                        isSubmitted = false
                        showResultDialog = false
                        showExplanation = false
                        showGameMenu = false
                        showCompleteButton = false
                    },
                    onContinue = { showGameMenu = false },
                    onDismiss = { showGameMenu = false }
                )
            }
        }

        // Result dialog
        if (showResultDialog) {
            val correctAnswers = questions.count { question ->
                val userAnswer = userAnswers[question.id] ?: emptyList()
                userAnswer.joinToString("") == question.correctAnswer
            }

            SentenceTransformResultDialog(
                correctAnswers = correctAnswers,
                totalQuestions = questions.size,
                onPlayAgain = {
                    userAnswers = mutableMapOf()
                    letterMapping = mutableMapOf()
                    currentQuestionIndex = 0
                    isSubmitted = false
                    showResultDialog = false
                    showExplanation = false
                    showCompleteButton = false
                },
                onExit = { navController.popBackStack() }
            )
        }
    }
}

// Compact letter card component
@Composable
fun CompactLetterCard(
    letter: String,
    color: Color,
    isUsed: Boolean = false,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .size(50.dp)
            .clickable(enabled = !isUsed) { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isUsed) Color.Gray.copy(alpha = 0.3f) else color
        ),
        border = BorderStroke(
            width = 2.dp,
            color = Color.White
        ),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isUsed) 2.dp else 4.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = letter,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = if (isUsed) Color.Gray else Color.White,
                textAlign = TextAlign.Center
            )
        }
    }
}

// Compact answer slot component
@Composable
fun CompactAnswerSlot(
    letter: String?,
    letterColor: Color?,
    isCorrect: Boolean?,
    isSubmitted: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .size(50.dp)
            .clickable(enabled = !isSubmitted && letter != null) { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = when {
                isSubmitted && isCorrect == true -> Color(0xFF4CAF50)
                isSubmitted && isCorrect == false -> Color(0xFFF44336)
                letter != null && letterColor != null -> letterColor
                else -> Color(0xFFE3F2FD) // Light blue background
            }
        ),
        border = BorderStroke(
            width = 2.dp,
            color = Color.White
        ),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = letter ?: "",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = when {
                    isSubmitted && isCorrect == true -> Color.White
                    isSubmitted && isCorrect == false -> Color.White
                    letter != null -> Color.White
                    else -> Color.Transparent
                },
                textAlign = TextAlign.Center
            )
        }
    }
}
