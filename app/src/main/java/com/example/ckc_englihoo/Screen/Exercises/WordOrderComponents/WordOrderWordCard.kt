package com.example.ckc_englihoo.Screen.Exercises.WordOrderComponents

import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import kotlinx.coroutines.delay

@Composable
fun WordOrderWordCard(
    wordWithId: String,
    wordColorMap: Map<String, Color>,
    hasSubmittedAnswers: Boolean,
    questionResults: Map<Int, Boolean>,
    currentQuestionIndex: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.9f else 1f,
        animationSpec = tween(100),
        label = "scale"
    )
    
    val displayWord = wordWithId.split("#")[0]
    val baseWordColor = wordColorMap[wordWithId] ?: Color(0xFF2196F3)
    
    // Determine word color based on submission status
    val wordColor = if (hasSubmittedAnswers && isSelected) {
        val isCurrentAnswerCorrect = questionResults[currentQuestionIndex] ?: false
        if (isCurrentAnswerCorrect) {
            Color(0xFF4CAF50) // Green for correct
        } else {
            Color(0xFFF44336) // Red for incorrect
        }
    } else if (hasSubmittedAnswers && !isSelected) {
        Color.Gray // Gray out available words after submission
    } else {
        baseWordColor
    }
    
    Card(
        modifier = Modifier
            .scale(scale)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                if (!hasSubmittedAnswers) {
                    isPressed = true
                    onClick()
                }
            },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) wordColor else wordColor.copy(alpha = 0.3f)
        ),
        border = if (!isSelected) BorderStroke(2.dp, wordColor) else null,
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(
            modifier = Modifier
                .wrapContentWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = displayWord,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = if (isSelected) Color.White else wordColor,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
    
    LaunchedEffect(isPressed) {
        if (isPressed) {
            delay(100)
            isPressed = false
        }
    }
}
