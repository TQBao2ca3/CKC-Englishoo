package com.example.ckc_englihoo.Screen.Exercises.SentenceTransformComponents

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest

// Letter card component - giống như note paper
@Composable
fun LetterCard(
    letter: String,
    color: Color,
    isUsed: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .size(60.dp)
            .clickable(enabled = !isUsed) { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isUsed) Color.Gray.copy(alpha = 0.3f) else Color.White
        ),
        border = BorderStroke(
            width = 2.dp,
            color = if (isUsed) Color.Gray else color
        ),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isUsed) 2.dp else 6.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            // Spiral binding effect at top
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .background(Color(0xFFE0E0E0))
                    .align(Alignment.TopCenter)
            )

            Text(
                text = letter,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = if (isUsed) Color.Gray else Color.Black,
                textAlign = TextAlign.Center
            )
        }
    }
}

// Answer slot component - giống như drop zone
@Composable
fun AnswerSlot(
    letter: String?,
    letterColor: Color?,
    isCorrect: Boolean?,
    isSubmitted: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .size(60.dp)
            .clickable(enabled = !isSubmitted && letter != null) { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = when {
                isSubmitted && isCorrect == true -> SentenceTransformColors.Correct.copy(alpha = 0.2f)
                isSubmitted && isCorrect == false -> SentenceTransformColors.Incorrect.copy(alpha = 0.2f)
                letter != null && letterColor != null -> letterColor.copy(alpha = 0.3f)
                else -> Color(0xFFF5F5F5)
            }
        ),
        border = BorderStroke(
            width = 2.dp,
            color = when {
                isSubmitted && isCorrect == true -> SentenceTransformColors.Correct
                isSubmitted && isCorrect == false -> SentenceTransformColors.Incorrect
                letter != null && letterColor != null -> letterColor
                else -> Color(0xFFBDBDBD)
            }
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = letter ?: "",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = when {
                    isSubmitted && isCorrect == true -> SentenceTransformColors.Correct
                    isSubmitted && isCorrect == false -> SentenceTransformColors.Incorrect
                    letter != null -> Color.Black
                    else -> Color.Gray
                },
                textAlign = TextAlign.Center
            )
        }
    }
}

// Main game display component
@Composable
fun SentenceTransformGameDisplay(
    question: SentenceTransformQuestion,
    userAnswer: List<String>,
    isSubmitted: Boolean,
    onLetterSelected: (String) -> Unit,
    onAnswerSlotClicked: (Int) -> Unit
) {
    val letterColors = remember {
        question.letters.mapIndexed { index, _ ->
            SentenceTransformColors.LetterColors[index % SentenceTransformColors.LetterColors.size]
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Image display
        Card(
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(16.dp)),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(question.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Question image",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        // Available letters section (like note papers)
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            question.letters.forEachIndexed { index, letter ->
                val color = letterColors[index]
                // Count how many times this letter is used vs available
                val usedCount = userAnswer.count { it == letter }
                val availableCount = question.letters.count { it == letter }
                val isUsed = usedCount >= availableCount

                LetterCard(
                    letter = letter,
                    color = color,
                    isUsed = isUsed,
                    onClick = {
                        if (!isSubmitted && !isUsed) {
                            onLetterSelected(letter)
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Answer slots section (drop zones)
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(question.correctAnswer.length) { index ->
                val letter = if (index < userAnswer.size) userAnswer[index] else null
                val letterColor = if (letter != null) {
                    val letterIndex = question.letters.indexOfFirst { it == letter }
                    if (letterIndex >= 0) letterColors[letterIndex] else null
                } else null

                val isCorrect = if (isSubmitted && letter != null) {
                    letter == question.correctAnswer[index].toString()
                } else null

                AnswerSlot(
                    letter = letter,
                    letterColor = letterColor,
                    isCorrect = isCorrect,
                    isSubmitted = isSubmitted,
                    onClick = {
                        if (!isSubmitted && letter != null) {
                            onAnswerSlotClicked(index)
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
