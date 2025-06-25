package com.example.ckc_englihoo.Screen.Exercises

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.navigation.NavController
import com.example.ckc_englihoo.Screen.Exercises.CategorizeComponents.*


// Data classes
data class CategorizeItem(
    val id: String,
    val text: String,
    val category: String,
    val color: Color
)

data class CategorySlot(
    val id: String,
    val name: String,
    val color: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorizeExercise(
    navController: NavController,
    exerciseTitle: String = "Phân loại từ"
) {
    // State management
    var selectedWord by remember { mutableStateOf<String?>(null) }
    var userCategories by remember { mutableStateOf(mapOf<String, String>()) } // word -> category
    var isSubmitted by remember { mutableStateOf(false) }
    var checkResults by remember { mutableStateOf(mapOf<String, Boolean>()) }
    var score by remember { mutableIntStateOf(0) }
    var showResult by remember { mutableStateOf(false) }
    var showCheckAnimation by remember { mutableStateOf(false) }
    var showGameMenu by remember { mutableStateOf(false) }
    var showInstructions by remember { mutableStateOf(false) }

    // Sample data - 10 English words categorization
    val items = remember {
        listOf(
            // 🟦 Noun (n)
            CategorizeItem("1", "studying", "Noun (n)", Color(0xFF009688)),
            CategorizeItem("2", "dream", "Noun (n)", Color(0xFF8BC34A)),
            CategorizeItem("3", "joy", "Noun (n)", Color(0xFFE91E63)),

            // 🟥 Verb (v)
            CategorizeItem("4", "design", "Verb (v)", Color(0xFFE91E63)),
            CategorizeItem("5", "remember", "Verb (v)", Color(0xFF00BCD4)),
            CategorizeItem("6", "fight", "Verb (v)", Color(0xFF03A9F4)),

            // 🟩 Adjective (adj)
            CategorizeItem("7", "happy", "Adjective (adj)", Color(0xFF2196F3)),
            CategorizeItem("8", "deep blue", "Adjective (adj)", Color(0xFF4CAF50)),
            CategorizeItem("9", "sad", "Adjective (adj)", Color(0xFFFF9800)),
            CategorizeItem("10", "good", "Adjective (adj)", Color(0xFF9C27B0))
        )

    }

    val categories = remember {
        listOf(
            CategorySlot("noun", "Noun (n)", Color(0xFF2196F3)),
            CategorySlot("verb", "Verb (v)", Color(0xFF4CAF50)),
            CategorySlot("adjective", "Adjective (adj)", Color(0xFFFF9800))
        )
    }

    // Calculate remaining items
    val remainingItems = items.size - userCategories.size

    // Check animation effect
    LaunchedEffect(showCheckAnimation) {
        if (showCheckAnimation) {
            kotlinx.coroutines.delay(1500)
            showResult = true
        }
    }

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
            },
            containerColor = Color.Transparent
        ) { paddingValues ->
            if (showResult) {
                CategorizeResultScreen(
                    items = items,
                    userCategories = userCategories,
                    checkResults = checkResults,
                    score = score,
                    onRestart = {
                        userCategories = mapOf()
                        selectedWord = null
                        isSubmitted = false
                        showResult = false
                        showCheckAnimation = false
                        score = 0
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
                    // Progress indicator
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFF0F8FF)
                        ),
                        border = BorderStroke(2.dp, Color(0xFF3B82F6)),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = if (remainingItems > 0)
                                    "📊 Đáp án còn lại: $remainingItems"
                                else "🎉 Đã hoàn thành tất cả đáp án!",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF1E3A8A),
                                textAlign = TextAlign.Center,
                                lineHeight = 12.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Word cards section
                    CategorizeWordCardsSection(
                        items = items,
                        selectedWord = selectedWord,
                        userCategories = userCategories,
                        isSubmitted = isSubmitted,
                        onWordSelected = { wordId ->
                            if (!isSubmitted) {
                                selectedWord = if (selectedWord == wordId) null else wordId
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "Nhấn vào nhóm bên dưới:",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2196F3)
                    )

                    // Category slots section
                    CategorySlotsSection(
                        categories = categories,
                        items = items,
                        userCategories = userCategories,
                        selectedWord = selectedWord,
                        isSubmitted = isSubmitted,
                        onCategorySelected = { categoryId ->
                            if (!isSubmitted && selectedWord != null) {
                                // Remove existing assignment for this word
                                userCategories = userCategories.toMutableMap().apply {
                                    remove(selectedWord)
                                    put(selectedWord!!, categoryId)
                                }
                                selectedWord = null
                            }
                        }
                    )

                }
            }
            // Game Menu
            if (showGameMenu) {
                CategorizeGameMenu(
                    onRestart = {
                        userCategories = mapOf()
                        selectedWord = null
                        isSubmitted = false
                        showResult = false
                        showCheckAnimation = false
                        score = 0
                        showGameMenu = false
                    },
                    onContinue = { showGameMenu = false },
                    onSubmit = if (!isSubmitted && userCategories.size == items.size && !showCheckAnimation) {
                        {
                            isSubmitted = true
                            checkResults = userCategories.mapValues { (word, category) ->
                                val item = items.find { it.text == word }
                                when (category) {
                                    "noun" -> item?.category == "Noun (n)"
                                    "verb" -> item?.category == "Verb (v)"
                                    "adjective" -> item?.category == "Adjective (adj)"
                                    else -> false
                                }
                            }
                            score = checkResults.count { it.value }
                            showCheckAnimation = true
                        }
                    } else null,
                    showSubmitButton = !isSubmitted && userCategories.size == items.size && !showCheckAnimation
                )
            }

            // Instructions Dialog
            if (showInstructions) {
                CategorizeInstructionsDialog(
                    onDismiss = { showInstructions = false }
                )
            }
        }
    }
}