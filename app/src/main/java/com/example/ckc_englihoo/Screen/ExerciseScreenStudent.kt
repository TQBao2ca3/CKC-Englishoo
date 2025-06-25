package com.example.ckc_englihoo.Screen

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.example.ckc_englihoo.R
import kotlinx.coroutines.delay

// Data classes for exercises
data class ExerciseQuestion(
    val id: String,
    val type: ExerciseType,
    val question: String,
    val options: List<String> = emptyList(),
    val correctAnswer: String,
    val imageRes: Int? = null,
    val words: List<String> = emptyList(),
    val categories: List<String> = emptyList(),
    val sentence: String = "",
    val scrambledWords: List<String> = emptyList()
)

enum class ExerciseType {
    MULTIPLE_CHOICE,    // Trắc nghiệm ABCD
    MATCH_IMAGE,        // Nối từ với hình ảnh
    CATEGORIZE,         // Phân loại từ
    FILL_BLANK,         // Điền từ vào chỗ trống
    WORD_ORDER,         // Phục hồi trật tự (Duolingo style)
    SENTENCE_TRANSFORM  // Đảo ngữ
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseScreenStudent(
    navController: NavController,
    exerciseTitle: String = "Bài tập tiếng Anh"
) {
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    var showResult by remember { mutableStateOf(false) }
    var selectedAnswer by remember { mutableStateOf("") }
    var isAnswered by remember { mutableStateOf(false) }
    var isCorrect by remember { mutableStateOf(false) }
    
    // Sample questions for all 6 types
    val questions = remember {
        listOf(
            // Multiple Choice
            ExerciseQuestion(
                id = "1",
                type = ExerciseType.MULTIPLE_CHOICE,
                question = "What is the capital of Vietnam?",
                options = listOf("Ho Chi Minh City", "Hanoi", "Da Nang", "Hue"),
                correctAnswer = "Hanoi"
            ),
            // Match Image
            ExerciseQuestion(
                id = "2",
                type = ExerciseType.MATCH_IMAGE,
                question = "Match the word with the image:",
                options = listOf("Apple", "Banana", "Orange", "Grape"),
                correctAnswer = "Apple",
                imageRes = R.drawable.teacher // Replace with actual fruit image
            ),
            // Categorize
            ExerciseQuestion(
                id = "3",
                type = ExerciseType.CATEGORIZE,
                question = "Categorize these words:",
                words = listOf("run", "beautiful", "cat", "quickly", "house", "smart"),
                categories = listOf("Noun", "Verb", "Adjective", "Adverb"),
                correctAnswer = "run:Verb,beautiful:Adjective,cat:Noun,quickly:Adverb,house:Noun,smart:Adjective"
            ),
            // Fill Blank
            ExerciseQuestion(
                id = "4",
                type = ExerciseType.FILL_BLANK,
                question = "Fill in the blank:",
                sentence = "I _____ to school every day.",
                options = listOf("go", "goes", "going", "went"),
                correctAnswer = "go"
            ),
            // Word Order
            ExerciseQuestion(
                id = "5",
                type = ExerciseType.WORD_ORDER,
                question = "Put the words in correct order:",
                scrambledWords = listOf("is", "This", "book", "my"),
                correctAnswer = "This is my book"
            ),
            // Sentence Transform
            ExerciseQuestion(
                id = "6",
                type = ExerciseType.SENTENCE_TRANSFORM,
                question = "Transform to negative form:",
                sentence = "She plays tennis.",
                correctAnswer = "She doesn't play tennis."
            )
        )
    }
    
    val currentQuestion = questions[currentQuestionIndex]
    val progress = (currentQuestionIndex + 1).toFloat() / questions.size
    
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
                    containerColor = Color(0xFF2196F3)
                )
            )
        },
        containerColor = Color(0xFFF5F5F5)
    ) { paddingValues ->
        if (showResult) {
            ExerciseResultScreen(
                score = score,
                totalQuestions = questions.size,
                onRetry = {
                    currentQuestionIndex = 0
                    score = 0
                    showResult = false
                    selectedAnswer = ""
                    isAnswered = false
                },
                onExit = { navController.popBackStack() }
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                // Progress bar
                ExerciseProgressBar(
                    progress = progress,
                    currentQuestion = currentQuestionIndex + 1,
                    totalQuestions = questions.size
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                // Question content
                ExerciseQuestionContent(
                    question = currentQuestion,
                    selectedAnswer = selectedAnswer,
                    isAnswered = isAnswered,
                    isCorrect = isCorrect,
                    onAnswerSelected = { answer ->
                        selectedAnswer = answer
                        isAnswered = true
                        isCorrect = answer == currentQuestion.correctAnswer
                        if (isCorrect) score++
                    }
                )
                
                Spacer(modifier = Modifier.weight(1f))
                
                // Next button
                AnimatedVisibility(
                    visible = isAnswered,
                    enter = slideInVertically() + fadeIn()
                ) {
                    Button(
                        onClick = {
                            if (currentQuestionIndex < questions.size - 1) {
                                currentQuestionIndex++
                                selectedAnswer = ""
                                isAnswered = false
                                isCorrect = false
                            } else {
                                showResult = true
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
                        Text(
                            text = if (currentQuestionIndex < questions.size - 1) "Tiếp theo" else "Hoàn thành",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ExerciseProgressBar(
    progress: Float,
    currentQuestion: Int,
    totalQuestions: Int
) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Câu $currentQuestion/$totalQuestions",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray
            )
            Text(
                text = "${(progress * 100).toInt()}%",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2196F3)
            )
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = Color(0xFF2196F3),
            trackColor = Color(0xFFE3F2FD)
        )
    }
}

@Composable
fun ExerciseQuestionContent(
    question: ExerciseQuestion,
    selectedAnswer: String,
    isAnswered: Boolean,
    isCorrect: Boolean,
    onAnswerSelected: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            // Question text
            Text(
                text = question.question,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Question type specific content
            when (question.type) {
                ExerciseType.MULTIPLE_CHOICE -> {
                    MultipleChoiceContent(
                        options = question.options,
                        selectedAnswer = selectedAnswer,
                        correctAnswer = question.correctAnswer,
                        isAnswered = isAnswered,
                        onAnswerSelected = onAnswerSelected
                    )
                }
                ExerciseType.MATCH_IMAGE -> {
                    MatchImageContent(
                        imageRes = question.imageRes ?: R.drawable.teacher,
                        options = question.options,
                        selectedAnswer = selectedAnswer,
                        correctAnswer = question.correctAnswer,
                        isAnswered = isAnswered,
                        onAnswerSelected = onAnswerSelected
                    )
                }
                ExerciseType.CATEGORIZE -> {
                    CategorizeContent(
                        words = question.words,
                        categories = question.categories,
                        onAnswerSelected = onAnswerSelected
                    )
                }
                ExerciseType.FILL_BLANK -> {
                    FillBlankContent(
                        sentence = question.sentence,
                        options = question.options,
                        selectedAnswer = selectedAnswer,
                        correctAnswer = question.correctAnswer,
                        isAnswered = isAnswered,
                        onAnswerSelected = onAnswerSelected
                    )
                }
                ExerciseType.WORD_ORDER -> {
                    WordOrderContent(
                        scrambledWords = question.scrambledWords,
                        correctAnswer = question.correctAnswer,
                        onAnswerSelected = onAnswerSelected
                    )
                }
                ExerciseType.SENTENCE_TRANSFORM -> {
                    SentenceTransformContent(
                        sentence = question.sentence,
                        correctAnswer = question.correctAnswer,
                        onAnswerSelected = onAnswerSelected
                    )
                }
            }
            
            // Answer feedback
            if (isAnswered) {
                Spacer(modifier = Modifier.height(16.dp))
                AnswerFeedback(isCorrect = isCorrect)
            }
        }
    }
}

// 1. Multiple Choice Component
@Composable
fun MultipleChoiceContent(
    options: List<String>,
    selectedAnswer: String,
    correctAnswer: String,
    isAnswered: Boolean,
    onAnswerSelected: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        options.forEachIndexed { index, option ->
            val isSelected = selectedAnswer == option
            val isCorrectOption = option == correctAnswer
            val backgroundColor = when {
                !isAnswered -> if (isSelected) Color(0xFFE3F2FD) else Color(0xFFF5F5F5)
                isCorrectOption -> Color(0xFFE8F5E8)
                isSelected && !isCorrectOption -> Color(0xFFFFEBEE)
                else -> Color(0xFFF5F5F5)
            }
            val borderColor = when {
                !isAnswered -> if (isSelected) Color(0xFF2196F3) else Color.Transparent
                isCorrectOption -> Color(0xFF4CAF50)
                isSelected && !isCorrectOption -> Color(0xFFF44336)
                else -> Color.Transparent
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = !isAnswered) { onAnswerSelected(option) },
                colors = CardDefaults.cardColors(containerColor = backgroundColor),
                border = BorderStroke(2.dp, borderColor),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${'A' + index}.",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2196F3),
                        modifier = Modifier.width(32.dp)
                    )
                    Text(
                        text = option,
                        fontSize = 16.sp,
                        color = Color.Black,
                        modifier = Modifier.weight(1f)
                    )
                    if (isAnswered) {
                        Icon(
                            imageVector = if (isCorrectOption) Icons.Default.Check else if (isSelected) Icons.Default.Close else Icons.Default.Check,
                            contentDescription = null,
                            tint = if (isCorrectOption) Color(0xFF4CAF50) else if (isSelected) Color(0xFFF44336) else Color.Transparent,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

// 2. Match Image Component
@Composable
fun MatchImageContent(
    imageRes: Int,
    options: List<String>,
    selectedAnswer: String,
    correctAnswer: String,
    isAnswered: Boolean,
    onAnswerSelected: (String) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Image
        Card(
            modifier = Modifier.size(200.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Options
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(options) { option ->
                val isSelected = selectedAnswer == option
                val isCorrectOption = option == correctAnswer
                val backgroundColor = when {
                    !isAnswered -> if (isSelected) Color(0xFFE3F2FD) else Color(0xFFF5F5F5)
                    isCorrectOption -> Color(0xFFE8F5E8)
                    isSelected && !isCorrectOption -> Color(0xFFFFEBEE)
                    else -> Color(0xFFF5F5F5)
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .clickable(enabled = !isAnswered) { onAnswerSelected(option) },
                    colors = CardDefaults.cardColors(containerColor = backgroundColor),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = option,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

// 3. Fill Blank Component
@Composable
fun FillBlankContent(
    sentence: String,
    options: List<String>,
    selectedAnswer: String,
    correctAnswer: String,
    isAnswered: Boolean,
    onAnswerSelected: (String) -> Unit
) {
    Column {
        // Sentence with blank
        val displaySentence = sentence.replace("_____", if (selectedAnswer.isNotEmpty()) "[$selectedAnswer]" else "_____")

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = displaySentence,
                fontSize = 18.sp,
                lineHeight = 24.sp,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(20.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Options
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(options) { option ->
                val isSelected = selectedAnswer == option
                val isCorrectOption = option == correctAnswer
                val backgroundColor = when {
                    !isAnswered -> if (isSelected) Color(0xFFE3F2FD) else Color(0xFFF5F5F5)
                    isCorrectOption -> Color(0xFFE8F5E8)
                    isSelected && !isCorrectOption -> Color(0xFFFFEBEE)
                    else -> Color(0xFFF5F5F5)
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clickable(enabled = !isAnswered) { onAnswerSelected(option) },
                    colors = CardDefaults.cardColors(containerColor = backgroundColor),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = option,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}

// 4. Word Order Component (Duolingo style)
@Composable
fun WordOrderContent(
    scrambledWords: List<String>,
    correctAnswer: String,
    onAnswerSelected: (String) -> Unit
) {
    var selectedWords by remember { mutableStateOf(listOf<String>()) }
    var availableWords by remember { mutableStateOf(scrambledWords) }

    Column {
        // Selected words area
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
            shape = RoundedCornerShape(12.dp)
        ) {
            LazyRow(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(selectedWords) { word ->
                    Card(
                        modifier = Modifier.clickable {
                            selectedWords = selectedWords - word
                            availableWords = availableWords + word
                        },
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF2196F3)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = word,
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Available words
        Text(
            text = "Tap words to build the sentence:",
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 100.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(availableWords) { word ->
                Card(
                    modifier = Modifier.clickable {
                        selectedWords = selectedWords + word
                        availableWords = availableWords - word

                        // Check if sentence is complete
                        if (availableWords.size == 1) {
                            val finalSentence = (selectedWords + word).joinToString(" ")
                            onAnswerSelected(finalSentence)
                        }
                    },
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = word,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }
    }
}

// 5. Categorize Component
@Composable
fun CategorizeContent(
    words: List<String>,
    categories: List<String>,
    onAnswerSelected: (String) -> Unit
) {
    var categorizedWords by remember { mutableStateOf(mapOf<String, List<String>>()) }
    var availableWords by remember { mutableStateOf(words) }

    Column {
        // Categories
        categories.forEach { category ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = category,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2196F3)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Words in this category
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(categorizedWords[category] ?: emptyList()) { word ->
                            Card(
                                modifier = Modifier.clickable {
                                    // Remove word from category
                                    val currentWords = categorizedWords[category] ?: emptyList()
                                    categorizedWords = categorizedWords + (category to currentWords - word)
                                    availableWords = availableWords + word
                                },
                                colors = CardDefaults.cardColors(containerColor = Color.White),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = word,
                                    modifier = Modifier.padding(8.dp),
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Available words
        if (availableWords.isNotEmpty()) {
            Text(
                text = "Tap words to categorize:",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(availableWords) { word ->
                    Card(
                        modifier = Modifier.clickable {
                            // Add to first category for demo
                            val firstCategory = categories.first()
                            val currentWords = categorizedWords[firstCategory] ?: emptyList()
                            categorizedWords = categorizedWords + (firstCategory to currentWords + word)
                            availableWords = availableWords - word

                            // Check if all words are categorized
                            if (availableWords.isEmpty()) {
                                onAnswerSelected("completed")
                            }
                        },
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = word,
                            modifier = Modifier.padding(12.dp),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

// 6. Sentence Transform Component
@Composable
fun SentenceTransformContent(
    sentence: String,
    correctAnswer: String,
    onAnswerSelected: (String) -> Unit
) {
    var userInput by remember { mutableStateOf("") }

    Column {
        // Original sentence
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "Original:",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Gray
                )
                Text(
                    text = sentence,
                    fontSize = 16.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Input field
        OutlinedTextField(
            value = userInput,
            onValueChange = { userInput = it },
            label = { Text("Your answer") },
            placeholder = { Text("Type the transformed sentence...") },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF2196F3),
                focusedLabelColor = Color(0xFF2196F3)
            ),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Submit button
        Button(
            onClick = { onAnswerSelected(userInput) },
            enabled = userInput.isNotBlank(),
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF2196F3)
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Submit",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

// Answer Feedback Component
@Composable
fun AnswerFeedback(isCorrect: Boolean) {
    val backgroundColor = if (isCorrect) Color(0xFFE8F5E8) else Color(0xFFFFEBEE)
    val textColor = if (isCorrect) Color(0xFF4CAF50) else Color(0xFFF44336)
    val icon = if (isCorrect) Icons.Default.Check else Icons.Default.Close
    val message = if (isCorrect) "Correct!" else "Incorrect"

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = message,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )
        }
    }
}

// Exercise Result Screen
@Composable
fun ExerciseResultScreen(
    score: Int,
    totalQuestions: Int,
    onRetry: () -> Unit,
    onExit: () -> Unit
) {
    val percentage = (score.toFloat() / totalQuestions * 100).toInt()
    val resultColor = when {
        percentage >= 80 -> Color(0xFF4CAF50)
        percentage >= 60 -> Color(0xFFFF9800)
        else -> Color(0xFFF44336)
    }
    val resultMessage = when {
        percentage >= 80 -> "Excellent!"
        percentage >= 60 -> "Good job!"
        else -> "Keep practicing!"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Result icon
        Card(
            modifier = Modifier.size(120.dp),
            colors = CardDefaults.cardColors(containerColor = resultColor),
            shape = CircleShape
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "$percentage%",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = resultMessage,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = resultColor
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "You got $score out of $totalQuestions questions correct",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Action buttons
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = onRetry,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFF2196F3)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Try Again",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Button(
                onClick = onExit,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2196F3)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Exit",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}
