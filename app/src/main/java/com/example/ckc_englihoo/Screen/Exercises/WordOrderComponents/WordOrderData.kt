package com.example.ckc_englihoo.Screen.Exercises.WordOrderComponents

data class WordOrderQuestion(
    val id: String,
    val question: String,
    val scrambledWords: List<String>,
    val correctAnswer: String,
    val explanation: String = ""
)

val sampleWordOrderQuestions = listOf(
    WordOrderQuestion(
        id = "1",
        question = "Đây là một con mèo và một con chó",
        scrambledWords = listOf("cat", "This", "a", "is", "and", "dog","a"),
        correctAnswer = "This is a cat and a dog",
        explanation = "Cấu trúc: This is a + [danh từ] and a + [danh từ] " +
                "(Đây là một con mèo và một con chó).Cấu trúc: This is a + [danh từ] and a + [danh từ] (Đây là một con mèo và một con chó)." +
                "Cấu trúc: This is a + [danh từ] and a + [danh từ] (Đây là một con mèo và một con chó)." +
                "Cấu trúc: This is a + [danh từ] and a + [danh từ] \" +\n" +
                "\"(Đây là một con mèo và một con chó).Cấu trúc: This is a + [danh từ] and a + [danh từ] (Đây là một con mèo và một con chó).\" +\n" +
                "\"Cấu trúc: This is a + [danh từ] and a + [danh từ] (Đây là một con mèo và một con chó)." +
                "Cấu trúc: This is a + [danh từ] and a + [danh từ] \" +\n" +
                "\"(Đây là một con mèo và một con chó).Cấu trúc: This is a + [danh từ] and a + [danh từ] (Đây là một con mèo và một con chó).\" +\n" +
                "\"Cấu trúc: This is a + [danh từ] and a + [danh từ] (Đây là một con mèo và một con chó)."
    ),
    WordOrderQuestion(
        id = "2",
        question = "Tôi đi học mỗi ngày",
        scrambledWords = listOf("day", "to", "I", "every", "school", "go"),
        correctAnswer = "I go to school every day",
        explanation = "Cấu trúc: I + go + to + school + every + day (Tôi đi học mỗi ngày)."
    ),
    WordOrderQuestion(
        id = "3",
        question = "Bạn có khỏe không?",
        scrambledWords = listOf("you", "How", "?", "are"),
        correctAnswer = "How are you ?",
        explanation = "Câu hỏi: How + are + you + ? (Bạn có khỏe không?)."
    ),
    WordOrderQuestion(
        id = "4",
        question = "Tôi không thích cà phê",
        scrambledWords = listOf("coffee", "don't", "I", "like"),
        correctAnswer = "I don't like coffee",
        explanation = "Câu phủ định: I + don't + like + coffee (Tôi không thích cà phê)."
    ),
    WordOrderQuestion(
        id = "5",
        question = "Chúng tôi đã chơi bóng đá hôm qua",
        scrambledWords = listOf("yesterday", "football", "We", "played"),
        correctAnswer = "We played football yesterday",
        explanation = "Thì quá khứ: We + played + football + yesterday (Chúng tôi đã chơi bóng đá hôm qua)."
    ),
    WordOrderQuestion(
        id = "6",
        question = "Chúng tôi sẽ thăm bảo tàng vào ngày mai",
        scrambledWords = listOf("museum", "tomorrow", "We", "the", "will", "visit"),
        correctAnswer = "We will visit the museum tomorrow",
        explanation = "Thì tương lai: We + will + visit + the + museum + tomorrow (Chúng tôi sẽ thăm bảo tàng vào ngày mai)."
    ),
    WordOrderQuestion(
        id = "7",
        question = "Cô ấy đang đọc sách",
        scrambledWords = listOf("a", "reading", "She", "book", "is"),
        correctAnswer = "She is reading a book",
        explanation = "Hiện tại tiếp diễn: She + is + reading + a + book (Cô ấy đang đọc sách)."
    ),
    WordOrderQuestion(
        id = "8",
        question = "John cao hơn Peter",
        scrambledWords = listOf("Peter", "taller", "John", "than", "is"),
        correctAnswer = "John is taller than Peter",
        explanation = "So sánh hơn: John + is + taller + than + Peter (John cao hơn Peter)."
    ),
    WordOrderQuestion(
        id = "9",
        question = "Nếu trời mưa tôi sẽ ở nhà",
        scrambledWords = listOf("home", "it", "I", "If", "stay", "rains", "will"),
        correctAnswer = "If it rains I will stay home",
        explanation = "Câu điều kiện: If + it + rains + I + will + stay + home (Nếu trời mưa tôi sẽ ở nhà)."
    ),
    WordOrderQuestion(
        id = "10",
        question = "Tôi đã hoàn thành bài tập về nhà",
        scrambledWords = listOf("my", "finished", "I", "homework", "have"),
        correctAnswer = "I have finished my homework",
        explanation = "Hiện tại hoàn thành: I + have + finished + my + homework (Tôi đã hoàn thành bài tập về nhà)."
    )
)
