package com.example.ckc_englihoo.Screen.Exercises

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
import com.example.ckc_englihoo.Screen.Exercises.MultipleChoiceComponents.*



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MultipleChoiceExercise(
    navController: NavController,
    exerciseTitle: String = "Trắc nghiệm ABCD"
) {
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    var showFinalResult by remember { mutableStateOf(false) }
    var showQuestionResult by remember { mutableStateOf(false) }
    var hasSubmittedAnswers by remember { mutableStateOf(false) }
    var showExplanation by remember { mutableStateOf(false) }

    // Store answers for all questions
    val userAnswers = remember { mutableStateMapOf<Int, String>() }
    val questionResults = remember { mutableStateMapOf<Int, Boolean>() }

    // Sample multiple choice questions
    val questions = remember { getSampleMultipleChoiceQuestions() }

    val currentQuestion = questions[currentQuestionIndex]
    val selectedAnswer = userAnswers[currentQuestionIndex] ?: ""
    val hasSelectedAnswer = selectedAnswer.isNotEmpty()

    // Check if all questions have been answered
    val allQuestionsAnswered = questions.indices.all { index ->
        userAnswers[index]?.isNotEmpty() == true
    }

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
                // Question counter (no progress bar)
                QuestionCounter(
                    currentQuestion = currentQuestionIndex + 1,
                    totalQuestions = questions.size,
                    onClose = { navController.popBackStack() }
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Question display
                QuestionDisplay(
                    question = currentQuestion.question,
                    currentQuestionIndex = currentQuestionIndex,
                    totalQuestions = questions.size
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Answer options
                AnswerOptions(
                    options = currentQuestion.options,
                    selectedAnswer = selectedAnswer,
                    correctAnswer = currentQuestion.correctAnswer,
                    showResults = hasSubmittedAnswers,
                    explanation = currentQuestion.explanation,
                    onAnswerSelected = { answer ->
                        if (!hasSubmittedAnswers) {
                            userAnswers[currentQuestionIndex] = answer
                        }
                    },
                    onExplanationToggle = { isShowing ->
                        showExplanation = isShowing
                    }
                )

                Spacer(modifier = Modifier.weight(1f))

                // Navigation buttons - hide when explanation is showing
                if (!showExplanation) {
                    NavigationButtons(
                    currentQuestionIndex = currentQuestionIndex,
                    totalQuestions = questions.size,
                    hasSelectedAnswer = hasSelectedAnswer,
                    allQuestionsAnswered = allQuestionsAnswered,
                    showResults = hasSubmittedAnswers,
                    onPrevious = {
                        if (currentQuestionIndex > 0) {
                            currentQuestionIndex--
                            showExplanation = false
                        }
                    },
                    onNext = {
                        if (currentQuestionIndex < questions.size - 1) {
                            currentQuestionIndex++
                            showExplanation = false
                        }
                    },
                    onSubmit = {
                        // Submit all answers and show results for all questions
                        if (allQuestionsAnswered && !hasSubmittedAnswers) {
                            // Calculate results for all questions
                            questions.forEachIndexed { index, question ->
                                val userAnswer = userAnswers[index] ?: ""
                                val isCorrect = userAnswer == question.correctAnswer
                                questionResults[index] = isCorrect
                            }
                            hasSubmittedAnswers = true
                            showQuestionResult = true
                        }
                    },
                    onFinish = {
                        // Calculate final score and show results
                        score = questionResults.values.count { it }
                        showFinalResult = true
                    }
                )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        // Show final result dialog
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
                },
                onExit = { navController.popBackStack() }
            )
        }
    }
}
