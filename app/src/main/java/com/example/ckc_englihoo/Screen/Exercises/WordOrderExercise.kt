package com.example.ckc_englihoo.Screen.Exercises

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import com.example.ckc_englihoo.Screen.Exercises.WordOrderComponents.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WordOrderExercise(navController: NavController) {
    val questions = remember { sampleWordOrderQuestions }
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var selectedWords by remember { mutableStateOf(listOf<String>()) }
    var availableWords by remember { mutableStateOf(listOf<String>()) }
    var userAnswers by remember { mutableStateOf(mutableMapOf<Int, String>()) }
    var questionResults by remember { mutableStateOf(mutableMapOf<Int, Boolean>()) }
    var score by remember { mutableIntStateOf(0) }
    var hasSubmittedAnswers by remember { mutableStateOf(false) }
    var showInstructions by remember { mutableStateOf(false) }
    var showGameMenu by remember { mutableStateOf(false) }
    var showResultDialog by remember { mutableStateOf(false) }
    var showExplanation by remember { mutableStateOf(false) }

    val currentQuestion = questions[currentQuestionIndex]
    val isLastQuestion = currentQuestionIndex == questions.size - 1
    val hasSelectedAnswer by remember { derivedStateOf { selectedWords.isNotEmpty() } }
    val allQuestionsAnswered by remember(userAnswers.size) {
        derivedStateOf { userAnswers.size == questions.size }
    }

    // Color palette for words
    val wordColors = remember {
        listOf(
            Color(0xFF2196F3), Color(0xFF4CAF50), Color(0xFFFF9800), Color(0xFF9C27B0),
            Color(0xFFF44336), Color(0xFF00BCD4), Color(0xFF8BC34A), Color(0xFFE91E63),
            Color(0xFF3F51B5), Color(0xFFFF5722), Color(0xFF607D8B), Color(0xFF795548)
        )
    }

    // Track word colors by position
    var wordColorMap by remember { mutableStateOf(mutableMapOf<String, Color>()) }

    // Store words for each question (only create once)
    var questionWordsMap by remember { mutableStateOf(mutableMapOf<Int, List<String>>()) }

    // Reset trigger to force LaunchedEffect re-run
    var resetTrigger by remember { mutableStateOf(0) }

    // Initialize words when question changes or reset
    LaunchedEffect(currentQuestionIndex, resetTrigger) {
        if (currentQuestionIndex >= 0 && currentQuestionIndex < questions.size) {
            val question = questions[currentQuestionIndex]

            // Create words for this question only if not already created
            if (!questionWordsMap.containsKey(currentQuestionIndex)) {
                val correctWords = question.correctAnswer.split(" ")
                val distractors = listOf("the", "a", "an", "is", "was", "were", "have", "has", "had",
                    "will", "would", "can", "could", "should", "must", "do", "does", "did", "not",
                    "very", "really", "quite", "always", "never", "sometimes", "often", "usually",
                    "today", "yesterday", "tomorrow", "here", "there", "now", "then", "also", "too",
                    "only", "just", "still", "already", "yet", "again", "more", "most", "much", "many")
                    .filter { it !in correctWords }
                    .shuffled()
                    .take(3)

                // Create unique word identifiers with positions and assign colors
                val allWords = (correctWords + distractors).shuffled()
                val wordsWithIds = allWords.mapIndexed { index, word -> "$word#$index" }

                // Store words for this question
                questionWordsMap[currentQuestionIndex] = wordsWithIds

                // Assign colors to words
                wordsWithIds.forEach { wordWithId ->
                    if (!wordColorMap.containsKey(wordWithId)) {
                        wordColorMap[wordWithId] = wordColors.random()
                    }
                }
            }

            // Restore saved answer
            val savedAnswer = userAnswers[currentQuestionIndex]
            if (!savedAnswer.isNullOrEmpty()) {
                val savedWordsList = savedAnswer.split(" ").filter { it.isNotEmpty() }
                selectedWords = listOf()

                // Match saved words with available words with IDs
                savedWordsList.forEach { savedWord ->
                    val matchingWordWithId = questionWordsMap[currentQuestionIndex]?.find {
                        it.startsWith("$savedWord#") && it !in selectedWords
                    }
                    if (matchingWordWithId != null) {
                        selectedWords = selectedWords + matchingWordWithId
                    }
                }
            } else {
                selectedWords = listOf()
            }

            // Set available words (all words minus selected ones)
            availableWords = questionWordsMap[currentQuestionIndex]?.filter { it !in selectedWords } ?: listOf()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Phục hồi trật tự",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.Default.ArrowBack,
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
            BottomAppBar(containerColor = Color.White, tonalElevation = 4.dp) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = { showInstructions = true },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
                    ) {
                        Text("Hướng dẫn", color = Color.White)
                    }
                    IconButton(onClick = { showGameMenu = true }) {
                        Icon(
                            Icons.Default.MoreVert,
                            contentDescription = null,
                            tint = Color(0xFF2196F3)
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFE3F2FD)) // Light blue background
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Question counter
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Câu ${currentQuestionIndex + 1}/${questions.size}",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.Black
                )
            }

            // Vietnamese question
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF8FAFC) // Soft white-blue background
                ),
                border = BorderStroke(2.dp, Color(0xFF3B82F6)), // Professional blue border
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                                colors = listOf(
                                    Color(0xFFF1F5F9), // Light gray-blue top
                                    Color(0xFFE2E8F0)  // Slightly darker gray-blue bottom
                                )
                            )
                        )
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = currentQuestion.question,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF1E293B), // Professional dark gray
                        textAlign = TextAlign.Center,
                        lineHeight = 26.sp,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            // Answer area (selected words)
            WordOrderAnswerArea(
                selectedWords = selectedWords,
                wordColorMap = wordColorMap,
                hasSubmittedAnswers = hasSubmittedAnswers,
                questionResults = questionResults,
                currentQuestionIndex = currentQuestionIndex,
                onWordRemoved = { wordWithId ->
                    // Remove word from selected and add back to available
                    val newSelectedWords = selectedWords.filter { it != wordWithId }
                    selectedWords = newSelectedWords
                    availableWords = availableWords + wordWithId

                    // Update user answer
                    if (newSelectedWords.isNotEmpty()) {
                        val answerText = newSelectedWords.map { it.split("#")[0] }.joinToString(" ")
                        userAnswers = userAnswers.toMutableMap().apply {
                            this[currentQuestionIndex] = answerText
                        }
                    } else {
                        userAnswers = userAnswers.toMutableMap().apply {
                            remove(currentQuestionIndex)
                        }
                    }
                }
            )

            // Available words (word choices)
            WordOrderAvailableWords(
                availableWords = availableWords,
                wordColorMap = wordColorMap,
                hasSubmittedAnswers = hasSubmittedAnswers,
                onWordSelected = { wordWithId ->
                    // Add word to selected and remove from available
                    val newSelectedWords = selectedWords + wordWithId
                    selectedWords = newSelectedWords
                    availableWords = availableWords.filter { it != wordWithId }

                    // Update user answer
                    val answerText = newSelectedWords.map { it.split("#")[0] }.joinToString(" ")
                    userAnswers = userAnswers.toMutableMap().apply {
                        this[currentQuestionIndex] = answerText
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Submit/Continue button logic
            if (!hasSubmittedAnswers) {
                if (hasSelectedAnswer) {
                    if (allQuestionsAnswered) {
                        // Submit button when all 10 questions are answered
                        Button(
                            onClick = {
                                // Submit all answers
                                questions.forEachIndexed { index, question ->
                                    val userAnswer = userAnswers[index] ?: ""
                                    questionResults[index] = userAnswer.trim() == question.correctAnswer.trim()
                                }
                                score = questionResults.count { it.value }
                                hasSubmittedAnswers = true
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF4CAF50)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    Icons.Default.Send,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp),
                                    tint = Color.White
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "Nộp bài",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    } else if (!isLastQuestion) {
                        // Continue button only for questions 1-9
                        Button(
                            onClick = {
                                currentQuestionIndex++
                                showExplanation = false
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF1976D2)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(
                                    Icons.Default.ArrowForward,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp),
                                    tint = Color.White
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "Tiếp theo",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                    // No button at question 10 if not all questions answered
                }
            } else {
                // Complete button after submission like FillBlank
                Button(
                    onClick = {
                        if (isLastQuestion) {
                            showResultDialog = true
                        } else {
                            currentQuestionIndex++
                            showExplanation = false
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50)
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp),
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            if (isLastQuestion) "Hoàn thành" else "Tiếp theo",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Navigation buttons "Trước" và "Sau"
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        if (currentQuestionIndex > 0) {
                            currentQuestionIndex--
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Trước", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                    }
                }

                OutlinedButton(
                    onClick = {
                        if (currentQuestionIndex < questions.size - 1) {
                            currentQuestionIndex++
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Sau", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
                    }
                }
            }





            // Explanation section - chỉ hiện khi sai như FillBlank
            if (hasSubmittedAnswers) {
                val isCurrentAnswerCorrect = questionResults[currentQuestionIndex] ?: false
                if (!isCurrentAnswerCorrect && currentQuestion.explanation.isNotEmpty()) {
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
                            border = BorderStroke(1.dp, Color(0xFF3B82F6).copy(alpha = 0.3f)),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(20.dp)
                            ) {
                                Text(
                                    text = "Giải thích chi tiết:",
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
            }

            Spacer(modifier = Modifier.weight(1f))
        }
    }

    // Dialogs
    if (showInstructions) {
        WordOrderInstructionsDialog { showInstructions = false }
    }

    if (showGameMenu) {
        WordOrderGameMenu(
            onRestart = {
                currentQuestionIndex = 0
                selectedWords = listOf()
                availableWords = listOf()
                userAnswers.clear()
                questionResults.clear()
                questionWordsMap.clear()
                wordColorMap.clear()
                hasSubmittedAnswers = false
                showGameMenu = false
                showExplanation = false
                resetTrigger++ // Force LaunchedEffect to re-run
            },
            onContinue = { showGameMenu = false }
        )
    }

    if (showResultDialog) {
        com.example.ckc_englihoo.Screen.Exercises.MultipleChoiceComponents.ResultDialog(
            score = score,
            totalQuestions = questions.size,
            onRetry = {
                currentQuestionIndex = 0
                selectedWords = listOf()
                availableWords = listOf()
                userAnswers.clear()
                questionResults.clear()
                questionWordsMap.clear()
                wordColorMap.clear()
                hasSubmittedAnswers = false
                showResultDialog = false
                showExplanation = false
                resetTrigger++ // Force LaunchedEffect to re-run
            },
            onExit = {
                navController.popBackStack()
            }
        )
    }


}