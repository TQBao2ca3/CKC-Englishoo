package com.example.vocabularyscreen

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ckc_englihoo.R

// Define types of questions
enum class QuestionType {
    WordOrder,
    MultipleChoice,
    ImageChoice,
    FillBlank
}

//@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VocabularyScreenStudent(
    questionNumber: Int = 1,
    progressPercent: Float = 0.10f,
    category: String = "Fruit and vegetable",
    promptText: String = "Fill in the blank:",
    @DrawableRes imageRes: Int = R.drawable.orange,
    answerLength: Int = 8,
    options: List<String> = listOf("Apple", "Banana", "Orange", "Grapes"),
    missingTemplate: String = "My ________ work in a hospital. My son's a doctor and my daughter's a nurse.",
    correctAnswer: String = "children",
    questionType: QuestionType = QuestionType.WordOrder,
    onBack: () -> Unit = {},
    onNext: (String) -> Unit = {}
) {
    val charList = remember { mutableStateListOf<Char?>().apply { repeat(answerLength) { add(null) } } }
    val currentAnswer = charList.joinToString(separator = "") { it?.toString() ?: "" }

    Scaffold(
        topBar = { TopBar(onBack) },
        bottomBar = {
            if (questionType == QuestionType.WordOrder || questionType == QuestionType.FillBlank) {
                BottomNextBar(
                    enabled = currentAnswer.length == answerLength,
                    onNext = { onNext(currentAnswer) }
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .padding(bottom = 72.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProgressSection(questionNumber, progressPercent, category)
            Spacer(modifier = Modifier.height(24.dp))
            when (questionType) {
                QuestionType.WordOrder -> WordOrder(promptText, imageRes, answerLength, correctAnswer, charList)
                QuestionType.MultipleChoice -> MultipleChoiceQuestion(promptText, options, correctAnswer, onNext)
                QuestionType.ImageChoice -> ImageChoiceQuestion(promptText, imageRes, options, correctAnswer, onNext)
                QuestionType.FillBlank -> FillBlankQuestion(
                    promptText = promptText,
                    sentenceTemplate = missingTemplate,
                    answerLength = answerLength,
                    correctAnswer = correctAnswer,
                    onNext = onNext
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(onBack: () -> Unit) {
    CenterAlignedTopAppBar(
        title = { Text(text = "Vocabulary", fontSize = 20.sp, fontWeight = FontWeight.Bold) },
        navigationIcon = {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close / Back",
                modifier = Modifier.padding(start = 12.dp).clickable { onBack() }
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = MaterialTheme.colorScheme.background)
    )
}

@Composable
private fun BottomNextBar(enabled: Boolean, onNext: () -> Unit) {
    Box(
        Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Button(
            onClick = onNext,
            enabled = enabled,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Next >", fontSize = 16.sp)
        }
    }
}

@Composable
private fun ProgressSection(questionNumber: Int, progressPercent: Float, category: String) {
    Text(text = category, fontSize = 16.sp, color = Color.Gray)
    Spacer(modifier = Modifier.height(16.dp))
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
        Text(text = "Q$questionNumber", fontSize = 14.sp, fontWeight = FontWeight.Medium)
        Spacer(modifier = Modifier.width(8.dp))
        LinearProgressIndicator(
            progress = progressPercent,
            modifier = Modifier.weight(1f).height(6.dp).clip(RoundedCornerShape(3.dp)),
            color = MaterialTheme.colorScheme.primary,
            trackColor = Color(0xFFE0E0E0)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "${(progressPercent * 100).toInt()}%", fontSize = 14.sp, fontWeight = FontWeight.Medium)
    }
}

// Word order type
@Composable
fun WordOrder(
    promptText: String = "Write the word correctly",
    @DrawableRes imageRes: Int = R.drawable.orange,
    answerLength: Int = 8,
    correctAnswer: String = "Orange",
    charList: MutableList<Char?>
) {
    Text(text = promptText, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
    Spacer(modifier = Modifier.height(16.dp))
    Image(
        painter = painterResource(id = imageRes),
        contentDescription = "Image",
        modifier = Modifier
            .size(150.dp)
            .clip(RoundedCornerShape(8.dp))
    )
    Spacer(modifier = Modifier.height(24.dp))
    val rows = (0 until answerLength).chunked(6)
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        rows.forEach { row ->
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                row.forEachIndexed { idx, index ->
                    SingleCharBox(index = index, charList = charList)
                    if (idx < row.lastIndex) Spacer(modifier = Modifier.width(8.dp))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun MultipleChoiceQuestion(
    promptText: String = "Which animal is a mammal?",
    options: List<String> = listOf("Shark", "Dolphin", "Salmon", "Eagle"),
    correctAnswer: String = "Dolphin",
    onNext: (String) -> Unit
) {


    var selected by remember { mutableStateOf<String?>(null) }
    var answered by remember { mutableStateOf(false) }
    var isCorrect by remember { mutableStateOf(false) }

    Text(text = promptText, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
    Spacer(modifier = Modifier.height(16.dp))
    Column {
        options.forEach { option ->
            val borderColor = when {
                answered && option == selected && isCorrect -> Color(0xFF4CAF50)
                answered && option == selected && !isCorrect -> Color(0xFFF44336)
                else -> Color.Transparent
            }
            OutlinedButton(
                onClick = {
                    if (!answered) {
                        selected = option
                        isCorrect = option == correctAnswer
                        answered = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .border(1.dp, borderColor, RoundedCornerShape(8.dp)),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = option, fontSize = 16.sp)
            }
        }
    }
    if (answered) {
        Spacer(modifier = Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = if (isCorrect) Icons.Default.CheckCircle else Icons.Default.Close,
                contentDescription = null,
                tint = if (isCorrect) Color(0xFF4CAF50) else Color(0xFFF44336)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isCorrect) "Correct answer, yeeay..." else "Wrong Answer",
                color = if (isCorrect) Color(0xFF4CAF50) else Color(0xFFF44336),
                fontWeight = FontWeight.Medium
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { onNext(selected ?: "") },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Next >", fontSize = 16.sp)
        }
    }
}

@Composable
fun ImageChoiceQuestion(
    promptText: String = "Select the apple from the list:",
    @DrawableRes imageRes: Int = R.drawable.orange,
    options: List<String> = listOf("Apple", "Banana", "Orange", "Grapes"),
    correctAnswer: String = "Apple",
    onOptionSelected: (String) -> Unit
) {
    var selected by remember { mutableStateOf<String?>(null) }
    var answered by remember { mutableStateOf(false) }
    var isCorrect by remember { mutableStateOf(false) }

    Text(text = promptText, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
    Spacer(modifier = Modifier.height(16.dp))
    Image(
        painter = painterResource(id = imageRes),
        contentDescription = null,
        modifier = Modifier.size(150.dp).clip(RoundedCornerShape(8.dp))
    )
    Spacer(modifier = Modifier.height(16.dp))
    Column {
        options.forEach { option ->
            val borderColor = when {
                answered && option == selected && isCorrect -> Color(0xFF4CAF50)
                answered && option == selected && !isCorrect -> Color(0xFFF44336)
                else -> Color.Transparent
            }
            OutlinedButton(
                onClick = {
                    if (!answered) {
                        selected = option
                        isCorrect = option == correctAnswer
                        answered = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .border(1.dp, borderColor, RoundedCornerShape(8.dp)),
                colors = ButtonDefaults.outlinedButtonColors(containerColor = Color.White),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(text = option, fontSize = 16.sp)
            }
        }
    }
    if (answered) {
        Spacer(modifier = Modifier.height(12.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = if (isCorrect) Icons.Default.CheckCircle else Icons.Default.Close,
                contentDescription = null,
                tint = if (isCorrect) Color(0xFF4CAF50) else Color(0xFFF44336)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (isCorrect) "Correct answer, yeeay..." else "Wrong Answer",
                color = if (isCorrect) Color(0xFF4CAF50) else Color(0xFFF44336),
                fontWeight = FontWeight.Medium
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = { onOptionSelected(selected ?: "") },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(text = "Next >", fontSize = 16.sp)
        }
    }
}

@Composable
fun FillBlankQuestion(
    promptText: String = "Fill in the blank:",
    sentenceTemplate: String = "My ________ work in a hospital. My son's a doctor and my daughter's a nurse.",
    answerLength: Int = 8,
    correctAnswer: String = "children",
    onNext: (String) -> Unit
) {
    var userInput by remember { mutableStateOf("") }

    Text(text = promptText, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
    Spacer(modifier = Modifier.height(16.dp))

    // Display sentence with dynamic blank
    val displaySentence = sentenceTemplate.replace(
        "________",
        if (userInput.isBlank()) "________" else userInput
    )
    Text(
        text = displaySentence,
        fontSize = 18.sp,
        lineHeight = 24.sp
    )

    Spacer(modifier = Modifier.height(24.dp))

    OutlinedTextField(
        value = userInput,
        onValueChange = { new ->
            if (new.length <= answerLength) userInput = new
        },
        placeholder = { Text("Type your answer") },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        textStyle = LocalTextStyle.current.copy(
            fontSize = 18.sp,
            textAlign = TextAlign.Start
        ),
        keyboardOptions = KeyboardOptions.Default,
        shape = RoundedCornerShape(8.dp)
    )
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SingleCharBox(
    index: Int,
    charList: MutableList<Char?>
) {
    var text by remember { mutableStateOf(charList[index]?.toString() ?: "") }
    OutlinedTextField(
        value = text,
        onValueChange = { new ->
            if (new.length <= 1) {
                val ch = new.firstOrNull()
                if (ch == null) {
                    text = ""
                    charList[index] = null
                } else if (ch.isLetter()) {
                    text = ch.uppercase()
                    charList[index] = ch.uppercaseChar()
                }
            }
        },
        modifier = Modifier
            .size(48.dp)
            .border(1.dp, Color.LightGray, RoundedCornerShape(4.dp)),
        singleLine = true,
        textStyle = LocalTextStyle.current.copy(
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium
        ),
        keyboardOptions = KeyboardOptions(
            capitalization = KeyboardCapitalization.Characters,
            autoCorrect = false
        ),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = Color.LightGray,
            containerColor = Color.Transparent
        )
    )
}
