package com.example.ckc_englihoo.Screen.Exercises

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import androidx.navigation.NavController

data class ExerciseMenuItem(
    val id: String,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val color: Color,
    val route: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseMenuScreen(
    navController: NavController
) {
    val exerciseTypes = remember {
        listOf(
            ExerciseMenuItem(
                id = "multiple_choice",
                title = "Trắc nghiệm ABCD",
                description = "Chọn đáp án đúng từ 4 lựa chọn",
                icon = Icons.Default.CheckCircle,
                color = Color(0xFF6C5CE7), // Modern purple
                route = "multiple_choice_exercise"
            ),
            ExerciseMenuItem(
                id = "match_image",
                title = "Nối từ với hình ảnh",
                description = "Chọn từ phù hợp với hình ảnh",
                icon = Icons.Default.Image,
                color = Color(0xFF00D2D3), // Turquoise
                route = "match_image_exercise"
            ),
            ExerciseMenuItem(
                id = "categorize",
                title = "Phân loại từ",
                description = "Sắp xếp từ vào nhóm phù hợp",
                icon = Icons.Default.Apps,
                color = Color(0xFFFF6B6B), // Coral red
                route = "categorize_exercise"
            ),
            ExerciseMenuItem(
                id = "fill_blank",
                title = "Điền từ vào chỗ trống",
                description = "Chọn từ đúng để hoàn thành câu",
                icon = Icons.Default.Edit,
                color = Color(0xFF4ECDC4), // Mint green
                route = "fill_blank_exercise"
            ),
            ExerciseMenuItem(
                id = "word_order",
                title = "Phục hồi trật tự",
                description = "Sắp xếp các từ theo đúng thứ tự",
                icon = Icons.Default.List,
                color = Color(0xFFFFBE0B), // Golden yellow
                route = "word_order_exercise"
            ),
            ExerciseMenuItem(
                id = "sentence_transform",
                title = "Đảo ngữ",
                description = "Sắp xếp chữ cái tạo thành từ",
                icon = Icons.Default.SwapHoriz,
                color = Color(0xFF9B59B6), // Purple
                route = "sentence_transform_exercise"
            )

        )
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Chọn dạng bài tập",
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
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                ),
                modifier = Modifier.background(
                    brush = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF667eea),
                            Color(0xFF764ba2)
                        )
                    )
                )
            )
        },
                containerColor = Color.Transparent
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF667eea),
                            Color(0xFF764ba2),
                            Color(0xFF8360c3),
                            Color(0xFF2ebf91)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
            // Header
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(
                        elevation = 20.dp,
                        shape = RoundedCornerShape(24.dp),
                        ambientColor = Color.Black.copy(alpha = 0.1f),
                        spotColor = Color.Black.copy(alpha = 0.1f)
                    ),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.95f)
                ),
                shape = RoundedCornerShape(24.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.radialGradient(
                                colors = listOf(
                                    Color.White.copy(alpha = 0.9f),
                                    Color(0xFFF8F9FA).copy(alpha = 0.8f)
                                ),
                                radius = 300f
                            )
                        )
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Gradient icon background
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .background(
                                    brush = Brush.linearGradient(
                                        colors = listOf(
                                            Color(0xFF667eea),
                                            Color(0xFF764ba2)
                                        )
                                    ),
                                    shape = CircleShape
                                )
                                .clip(CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.School,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(40.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Luyện tập tiếng Anh",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2D3436),
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Chọn dạng bài tập bạn muốn luyện tập",
                            fontSize = 16.sp,
                            color = Color(0xFF636e72),
                            textAlign = TextAlign.Center,
                            lineHeight = 22.sp
                        )
                    }
                }
            }

                Spacer(modifier = Modifier.height(32.dp))

                // Exercise types grid
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    items(exerciseTypes) { exerciseType ->
                        ExerciseMenuItemCard(
                            exerciseType = exerciseType,
                            onClick = {
                                navController.navigate(exerciseType.route)
                            }
                        )
                    }
                }
            }
        }

    }
}

@Composable
fun ExerciseMenuItemCard(
    exerciseType: ExerciseMenuItem,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }



    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clickable { onClick() }
            .scale(if (isPressed) 0.95f else 1f)
            .shadow(
                elevation = if (isPressed) 8.dp else 16.dp,
                shape = RoundedCornerShape(20.dp),
                ambientColor = exerciseType.color.copy(alpha = 0.3f),
                spotColor = exerciseType.color.copy(alpha = 0.3f)
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.95f)
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.9f),
                            exerciseType.color.copy(alpha = 0.05f)
                        )
                    )
                )
        ) {
            // Decorative circles
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .offset(x = (-20).dp, y = (-20).dp)
                    .background(
                        color = exerciseType.color.copy(alpha = 0.1f),
                        shape = CircleShape
                    )
            )

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .offset(x = 120.dp, y = 140.dp)
                    .background(
                        color = exerciseType.color.copy(alpha = 0.08f),
                        shape = CircleShape
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Enhanced icon with gradient background
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    exerciseType.color,
                                    exerciseType.color.copy(alpha = 0.8f)
                                )
                            ),
                            shape = CircleShape
                        )
                        .clip(CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = exerciseType.icon,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Title with enhanced typography
                Text(
                    text = exerciseType.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2D3436),
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    lineHeight = 18.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Description with better styling
                Text(
                    text = exerciseType.description,
                    fontSize = 12.sp,
                    color = Color(0xFF636e72),
                    textAlign = TextAlign.Center,
                    maxLines = 2,
                    lineHeight = 16.sp
                )
            }
        }
    }
}


