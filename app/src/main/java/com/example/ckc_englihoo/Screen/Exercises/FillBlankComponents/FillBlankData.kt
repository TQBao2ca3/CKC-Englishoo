package com.example.ckc_englihoo.Screen.Exercises.FillBlankComponents

import androidx.compose.ui.graphics.Color

// Data class cho câu hỏi điền từ vào chỗ trống
data class FillBlankQuestion(
    val id: Int,
    val sentence: String, // Câu có chỗ trống, dùng "___" để đánh dấu chỗ trống
    val correctAnswer: String, // Đáp án đúng
    val options: List<String>, // 2 lựa chọn (1 đúng, 1 sai)
    val explanation: String // Giải thích đáp án
)

// Sample data cho 10 câu hỏi
val sampleFillBlankQuestions = listOf(
    FillBlankQuestion(
        id = 1,
        sentence = "Nam goes ___ the beach.",
        correctAnswer = "to",
        options = listOf("to", "into"),
        explanation = "Use 'to' when moving towards a place like the beach."
    ),
    FillBlankQuestion(
        id = 2,
        sentence = "She ___ the house at 8 o'clock.",
        correctAnswer = "leaves",
        options = listOf("leaves", "enters"),
        explanation = "Use 'leaves' when someone goes out of a place."
    ),
    FillBlankQuestion(
        id = 3,
        sentence = "We ___ learning English.",
        correctAnswer = "are",
        options = listOf("are", "were"),
        explanation = "Use 'are' to express an action happening right now (present continuous)."
    ),
    FillBlankQuestion(
        id = 4,
        sentence = "My mother ___ delicious food.",
        correctAnswer = "cooks",
        options = listOf("cook", "cooks"),
        explanation = "Chủ ngữ số ít 'My mother' đi với động từ thêm 's': cooks."
    ),
    FillBlankQuestion(
        id = 5,
        sentence = "We ___ to the cinema last night.",
        correctAnswer = "went",
        options = listOf("go", "went"),
        explanation = "Có 'last night' nên dùng thì quá khứ đơn 'went'."
    ),
    FillBlankQuestion(
        id = 6,
        sentence = "The cat ___ on the table.",
        correctAnswer = "is",
        options = listOf("is", "are"),
        explanation = "Chủ ngữ số ít 'The cat' đi với 'is'."
    ),
    FillBlankQuestion(
        id = 7,
        sentence = "Children ___ in the park every afternoon.",
        correctAnswer = "play",
        options = listOf("play", "plays"),
        explanation = "Chủ ngữ số nhiều 'Children' đi với động từ nguyên mẫu 'play'."
    ),
    FillBlankQuestion(
        id = 8,
        sentence = "He ___ his homework before dinner.",
        correctAnswer = "does",
        options = listOf("do", "does"),
        explanation = "Chủ ngữ số ít 'He' đi với động từ thêm 's': does."
    ),
    FillBlankQuestion(
        id = 9,
        sentence = "The students ___ studying for the exam.",
        correctAnswer = "are",
        options = listOf("is", "are"),
        explanation = "Chủ ngữ số nhiều 'The students' đi với 'are'."
    ),
    FillBlankQuestion(
        id = 10,
        sentence = "I ___ a book when you called me.",
        correctAnswer = "was reading",
        options = listOf("read", "was reading"),
        explanation = "Hành động đang diễn ra trong quá khứ khi có hành động khác xen vào, dùng quá khứ tiếp diễn."
    )
)

// Colors cho UI
object FillBlankColors {
    val Primary = Color(0xFF3B82F6)
    val Secondary = Color(0xFF93C5FD)
    val Background = Color(0xFFF0F8FF)
    val Surface = Color.White
    val Correct = Color(0xFF10B981)
    val Incorrect = Color(0xFFEF4444)
    val Warning = Color(0xFFF59E0B)
    val Text = Color(0xFF1E3A8A)
    val TextSecondary = Color(0xFF64748B)
}
