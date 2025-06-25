package com.example.ckc_englihoo.Screen.Exercises

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.example.ckc_englihoo.Screen.Exercises.WordOrderComponents.*
import com.example.ckc_englihoo.Screen.Exercises.MultipleChoiceComponents.ResultDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordOrderExercise(
    navController: NavController,
    exerciseTitle: String = "Phục hồi trật tự"
) {
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    var showFinalResult by remember { mutableStateOf(false) }
    var showQuestionResult by remember { mutableStateOf(false) }
    var hasSubmittedAnswers by remember { mutableStateOf(false) }
    var showExplanation by remember { mutableStateOf(false) }
    var showInstructions by remember { mutableStateOf(false) }
    var showGameMenu by remember { mutableStateOf(false) }

    val userAnswers = remember { mutableStateMapOf<Int, String>() }
    val questionResults = remember { mutableStateMapOf<Int, Boolean>() }

    val questions = remember { sampleWordOrderQuestions }

    if (questions.isEmpty()) return
    if (currentQuestionIndex < 0 || currentQuestionIndex >= questions.size) return

    val currentQuestion = remember(currentQuestionIndex) {
        if (currentQuestionIndex >= 0 && currentQuestionIndex < questions.size) {
            questions[currentQuestionIndex]
        } else {
            questions[0] // Fallback
        }
    }

    var selectedWords by remember { mutableStateOf(listOf<String>()) }
    var availableWords by remember { mutableStateOf(listOf<String>()) }
    var hasSubmittedCurrentQuestion by remember { mutableStateOf(false) }

    // ✅ Kiểm tra đã chọn ít nhất 1 từ
    val hasSelectedAnswer by remember { derivedStateOf {
        selectedWords.isNotEmpty()
    } }

    // ✅ LaunchedEffect đơn giản để tránh crash
    LaunchedEffect(currentQuestionIndex) {
        try {
            if (currentQuestionIndex >= 0 && currentQuestionIndex < questions.size) {
                val question = questions[currentQuestionIndex]

                // Restore đáp án đã lưu (an toàn)
                val savedAnswer = userAnswers[currentQuestionIndex]
                if (!savedAnswer.isNullOrEmpty()) {
                    selectedWords = savedAnswer.split(" ").filter { it.isNotEmpty() }
                } else {
                    selectedWords = listOf()
                }

                // Set available words (an toàn)
                availableWords = question.scrambledWords.toList()

                // Remove selected words from available
                selectedWords.forEach { selectedWord ->
                    availableWords = availableWords.filter { it != selectedWord }
                }
            }
        } catch (e: Exception) {
            // Fallback an toàn
            selectedWords = listOf()
            if (currentQuestionIndex >= 0 && currentQuestionIndex < questions.size) {
                availableWords = questions[currentQuestionIndex].scrambledWords.toList()
            } else {
                availableWords = listOf()
            }
        }
    }

    val selectedAnswer = userAnswers[currentQuestionIndex] ?: ""
    val allQuestionsAnswered = currentQuestionIndex == questions.size - 1 && hasSelectedAnswer

    val isCurrentAnswerCorrect = if (hasSubmittedAnswers) {
        val savedAnswer = userAnswers[currentQuestionIndex] ?: ""
        savedAnswer.trim() == currentQuestion.correctAnswer.trim()
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
                    containerColor = WordOrderColors.Primary
                )
            )
        },
        bottomBar = {
            if (!showExplanation) {
                BottomAppBar(
                    containerColor = Color.White,
                    tonalElevation = 4.dp
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = { showInstructions = !showInstructions },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = WordOrderColors.Primary
                            )
                        ) {
                            Text("Hướng dẫn", color = Color.White)
                        }
                        IconButton(onClick = { showGameMenu = !showGameMenu }) {
                            Icon(
                                Icons.Default.MoreVert,
                                contentDescription = null,
                                tint = WordOrderColors.Primary
                            )
                        }
                    }
                }
            }
        },
        containerColor = WordOrderColors.Background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFEBF4FF),
                            Color(0xFF93C5FD).copy(alpha = 0.3f),
                            Color.White
                        )
                    )
                )
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            WordOrderQuestionCounter(
                currentQuestion = currentQuestionIndex + 1,
                totalQuestions = questions.size
            )

            Spacer(modifier = Modifier.height(24.dp))

            WordOrderGameDisplay(
                question = currentQuestion,
                selectedWords = selectedWords,
                selectedWordsWithColors = selectedWords.mapIndexed { index, word -> word to index },
                availableWords = availableWords,
                onWordSelected = { word, _ ->
                    if (!hasSubmittedAnswers) {
                        val newSelectedWords = selectedWords + word
                        selectedWords = newSelectedWords
                        availableWords = availableWords.filter { it != word }
                        userAnswers[currentQuestionIndex] = newSelectedWords.joinToString(" ")
                    }
                },
                onWordRemoved = { word ->
                    if (!hasSubmittedAnswers) {
                        val newSelectedWords = selectedWords.filter { it != word }
                        selectedWords = newSelectedWords
                        availableWords = availableWords + word
                        if (newSelectedWords.isNotEmpty()) {
                            userAnswers[currentQuestionIndex] = newSelectedWords.joinToString(" ")
                        } else {
                            userAnswers.remove(currentQuestionIndex)
                        }
                    }
                }
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Continue/Next/Submit button when at least 1 word selected
            if (hasSelectedAnswer && !hasSubmittedAnswers) {
                Spacer(modifier = Modifier.height(20.dp))

                val isLastQuestion = currentQuestionIndex == questions.size - 1

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Button(
                        onClick = {
                            try {
                                if (isLastQuestion) {
                                    // Nộp bài - chấm điểm tất cả câu
                                    questions.forEachIndexed { index, question ->
                                        val userAnswer = userAnswers[index] ?: ""
                                        val isCorrect = userAnswer.trim() == question.correctAnswer.trim()
                                        questionResults[index] = isCorrect
                                    }
                                    score = questionResults.count { it.value }
                                    hasSubmittedAnswers = true
                                    // Không hiển thị result ngay, để xem giải thích trước
                                } else {
                                    // Tiếp theo - chỉ khi đã chọn HẾT từ
                                    if (currentQuestionIndex < questions.size - 1) {
                                        currentQuestionIndex++
                                    }
                                }
                            } catch (e: Exception) {
                                // Log error hoặc xử lý lỗi
                                // Không làm gì để tránh crash
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
                            if (isLastQuestion) {
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

            Spacer(modifier = Modifier.height(12.dp))

            // Hiển thị giải thích cho câu sai sau khi nộp bài
            val userAnsweredIncorrectly = hasSubmittedAnswers && isCurrentAnswerCorrect == false
            if (hasSubmittedAnswers && userAnsweredIncorrectly && currentQuestion.explanation.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showExplanation = !showExplanation },
                    colors = CardDefaults.cardColors(
                        containerColor = if (showExplanation) Color(0xFFF0F8FF) else Color(0xFFE0F2FE)
                    ),
                    border = BorderStroke(2.dp, WordOrderColors.Primary.copy(alpha = 0.4f)),
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
                            Text("💡", fontSize = 18.sp, modifier = Modifier.padding(end = 8.dp))
                            Text(
                                text = if (showExplanation) "Ẩn giải thích" else "Xem giải thích",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = WordOrderColors.Primary
                            )
                        }

                        Icon(
                            imageVector = if (showExplanation) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = WordOrderColors.Primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                if (showExplanation) {
                    Spacer(modifier = Modifier.height(12.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF0F8FF)),
                        border = BorderStroke(2.dp, Color(0xFF87CEEB).copy(alpha = 0.6f)),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            Text(
                                text = "Giải thích đáp án đúng:",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = WordOrderColors.Primary,
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

                    Spacer(modifier = Modifier.height(100.dp))
                }
            }

            // Button "Hoàn thành" sau khi nộp bài
            if (hasSubmittedAnswers) {
                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        showFinalResult = true
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
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "Hoàn thành",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Spacer(modifier = Modifier.height(16.dp))
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



        if (showInstructions) {
            WordOrderInstructionsDialog(onDismiss = { showInstructions = false })
        }

        if (showGameMenu) {
            WordOrderGameMenu(
                onRestart = {
                    userAnswers.clear()
                    questionResults.clear()
                    currentQuestionIndex = 0
                    selectedWords = listOf()
                    if (questions.isNotEmpty()) {
                        availableWords = questions[0].scrambledWords.toList()
                    }
                    hasSubmittedAnswers = false
                    showExplanation = false
                    showGameMenu = false
                },
                onContinue = { showGameMenu = false }
            )
        }

        if (showFinalResult) {
            ResultDialog(
                score = score,
                totalQuestions = questions.size,
                onRetry = {
                    currentQuestionIndex = 0
                    score = 0
                    showFinalResult = false
                    showQuestionResult = false
                    hasSubmittedAnswers = false
                    showExplanation = false
                    userAnswers.clear()
                    questionResults.clear()
                    selectedWords = listOf()
                    if (questions.isNotEmpty()) {
                        availableWords = questions[0].scrambledWords.toList()
                    }
                },
                onExit = { navController.popBackStack() }
            )
        }
    }   




