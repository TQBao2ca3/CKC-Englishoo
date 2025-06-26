package com.example.ckc_englihoo.Screen.Exercises.WordOrderComponents

import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.*

@Composable
fun WordOrderAvailableWords(
    availableWords: List<String>,
    wordColorMap: Map<String, Color>,
    hasSubmittedAnswers: Boolean,
    onWordSelected: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Group words into rows based on text length
        val wordRows = mutableListOf<List<String>>()
        var currentRow = mutableListOf<String>()
        var currentRowLength = 0
        
        availableWords.forEach { wordWithId ->
            val displayWord = wordWithId.split("#")[0]
            val wordLength = displayWord.length
            
            // If adding this word would make the row too long, start a new row
            if (currentRowLength + wordLength > 25 && currentRow.isNotEmpty()) {
                wordRows.add(currentRow.toList())
                currentRow.clear()
                currentRowLength = 0
            }
            
            currentRow.add(wordWithId)
            currentRowLength += wordLength + 2 // +2 for spacing
        }
        
        if (currentRow.isNotEmpty()) {
            wordRows.add(currentRow.toList())
        }
        
        wordRows.forEach { rowWords ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowWords.forEach { wordWithId ->
                    WordOrderWordCard(
                        wordWithId = wordWithId,
                        wordColorMap = wordColorMap,
                        hasSubmittedAnswers = hasSubmittedAnswers,
                        questionResults = emptyMap(),
                        currentQuestionIndex = 0,
                        isSelected = false,
                        onClick = { onWordSelected(wordWithId) }
                    )
                }
            }
        }
    }
}
