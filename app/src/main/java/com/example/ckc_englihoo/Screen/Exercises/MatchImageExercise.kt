package com.example.ckc_englihoo.Screen.Exercises

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import com.example.ckc_englihoo.R
import com.example.ckc_englihoo.Screen.Exercises.MatchImageComponents.*
import kotlinx.coroutines.delay

// Data class cho mục từ và hình ảnh
data class MatchImageItem(
    val id: String,
    val word: String,
    val imageUrl: String,
    val color: Color
)

// Trạng thái kéo thả
data class DragState(
    val isDragging: Boolean = false,
    val draggedItem: MatchImageItem? = null,
    val currentPosition: Offset = Offset.Zero,
    val startPosition: Offset = Offset.Zero
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MatchImageExercise(navController: NavController, exerciseTitle: String = "Nối từ với hình ảnh") {
    var userMatches by remember { mutableStateOf(mutableMapOf<String, String>()) }
    var isSubmitted by remember { mutableStateOf(false) }
    var showResult by remember { mutableStateOf(false) }
    var score by remember { mutableIntStateOf(0) }
    var showCheckAnimation by remember { mutableStateOf(false) }
    var checkResults by remember { mutableStateOf<Map<String, Boolean>>(emptyMap()) }
    var showInstructions by remember { mutableStateOf(false) }
    var showGameMenu by remember { mutableStateOf(false) }
    var selectedWord by remember { mutableStateOf<String?>(null) }

    val items = remember {
        listOf(
            MatchImageItem("1", "Panda", "https://images.unsplash.com/photo-1564349683136-77e08dba1ef7?w=400&h=400&fit=crop", Color(0xFF2196F3)),
            MatchImageItem("2", "Dog", "https://images.freeimages.com/images/large-previews/05a/funny-dog-1409073.jpg", Color(0xFFF44336)),
            MatchImageItem("3", "Elephant", "https://tse4.mm.bing.net/th?id=OIP.zWDKu-OUPxkyHizMgZf3ogHaFj&pid=Api&P=0&h=220", Color(0xFFFF9800)),
            MatchImageItem("4", "Bee", "https://tse4.mm.bing.net/th?id=OIP.y9tQ48XWegUIdK6JoPwnggHaE8&pid=Api&P=0&h=220", Color(0xFF4CAF50)),
            MatchImageItem("5", "Tiger", "https://tse1.mm.bing.net/th?id=OIP.nO9WuXULH3xwkct92ic8fwHaE7&pid=Api&P=0&h=220", Color(0xFF9C27B0)),
            MatchImageItem("6", "Shark", "https://tse3.mm.bing.net/th?id=OIP.4aFEGkxDABT8E_lBqmaqlgHaE8&pid=Api&P=0&h=220", Color(0xFF2196F3)),
            MatchImageItem("7", "Dolphin", "https://tse2.mm.bing.net/th?id=OIP.RDrO68WeYpRfNgsA0hpe3gHaEo&pid=Api&P=0&h=220", Color(0xFF00BCD4)),
            MatchImageItem("8", "Snake", "https://img.freepik.com/premium-vector/cute-snake-cartoon-style-vector-illustration-white-background_1023984-4161.jpg", Color(0xFFFF5722)),
            MatchImageItem("9", "Alligator", "https://m.media-amazon.com/images/I/61yQ9tvECqL._AC_SL1500_.jpg", Color(0xFF4CAF50)),
            MatchImageItem("10", "Lion", "https://tse3.mm.bing.net/th?id=OIP.H18-v3J60a6eS4IVsnPvTwHaFp&pid=Api&P=0&h=220", Color(0xFF2196F3))
        )
    }



    LaunchedEffect(showCheckAnimation) {
        if (showCheckAnimation) {
            delay(3000L)
            showResult = true
            showCheckAnimation = false
        }
    }

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
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFF00D2D3))
            )
        },
        containerColor = Color.White,
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
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFEBF4FF),
                            Color(0xFFDBEAFE),
                            Color(0xFFBFDBFE),
                            Color(0xFF93C5FD)
                        )
                    )
                )
        ) {
            if (showResult) {
                MatchResultScreen(
                    score = score,
                    totalItems = items.size,
                    items = items,
                    userMatches = userMatches,
                    onRetry = {
                        userMatches.clear()
                        selectedWord = null
                        isSubmitted = false
                        showResult = false
                        score = 0
                    },
                    onExit = { navController.popBackStack() }
                )
            } else {
                Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)) {
                    Spacer(modifier = Modifier.height(12.dp))

                    // Progress indicator
                    val remainingAnswers = items.size - userMatches.size
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
                                text = if (remainingAnswers > 0)
                                    "📊 Đáp án còn lại: $remainingAnswers"
                                else "🎉 Đã hoàn thành tất cả đáp án!",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF1E3A8A),
                                textAlign = TextAlign.Center,
                                lineHeight = 12.sp
                            )
                        }
                    }

                    WordCardsSection(
                        items = items,
                        selectedWord = selectedWord,
                        isSubmitted = isSubmitted,
                        userMatches = userMatches,
                        onWordSelected = { wordId ->
                            if (!isSubmitted) {
                                selectedWord = if (selectedWord == wordId) null else wordId
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        "Nhấn vào ô bên dưới hình ảnh:",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2196F3)
                    )

                    DropZoneSection(
                        items = items,
                        userMatches = userMatches,
                        selectedWord = selectedWord,
                        isSubmitted = isSubmitted,
                        onImageSelected = { imageId ->
                            if (!isSubmitted) {
                                if (selectedWord != null) {
                                    userMatches = userMatches.toMutableMap().apply {
                                        // Gỡ đáp án trùng trước khi thêm
                                        val existing = filterValues { it == selectedWord }.keys
                                        existing.forEach { remove(it) }

                                        put(imageId, selectedWord!!)
                                    }
                                    selectedWord = null
                                } else {
                                    // Nếu chưa chọn từ nhưng nhấn vào ô đã có đáp án, cho phép gỡ đáp án
                                    val currentMatch = userMatches[imageId]
                                    if (currentMatch != null) {
                                        userMatches = userMatches.toMutableMap().apply {
                                            remove(imageId)
                                        }
                                    }
                                }
                            }
                        }
                    )

                    // Submit button is now in GameMenu
                }
            }

            if (showGameMenu) {
                GameMenu(
                    onRestart = {
                        userMatches.clear()
                        selectedWord = null
                        isSubmitted = false
                        showResult = false
                        showCheckAnimation = false
                        score = 0
                        showGameMenu = false
                    },
                    onContinue = { showGameMenu = false },
                    onSubmit = if (!isSubmitted && userMatches.size == items.size && !showCheckAnimation) {
                        {
                            isSubmitted = true
                            checkResults = userMatches.mapValues { (dropZoneId, wordId) -> dropZoneId == wordId }
                            score = checkResults.count { it.value }
                            showCheckAnimation = true
                        }
                    } else null,
                    showSubmitButton = !isSubmitted && userMatches.size == items.size && !showCheckAnimation
                )
            }

            if (showInstructions) {
                InstructionsDialog(onDismiss = { showInstructions = false })
            }
        }
    }
}