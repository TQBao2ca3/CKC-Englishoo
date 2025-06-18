package com.example.ckc_englihoo.DataClass

// Assignment data class - Match với API response (same as LessonPart với questions)
data class Assignment(
    val lesson_part_id: Int, // Acts as assignment_id
    val part_type: String = "", // Assignment title (Vocabulary, Grammar, etc.)
    val level: String = "",
    val questions: List<Question> = emptyList()
)

// Question data class - Match với API response
data class Question(
    val questions_id: Int,
    val contents_id: Int? = null,
    val question_type: String = "", // single_choice, multiple_choice, matching, classification, fill_blank, arrangement, image_word
    val question_text: String = "",
    val media_url: String? = null,
    val order_index: Int = 0,
    val created_at: String = "",
    val updated_at: String = "",
    val answers: List<Answer> = emptyList()
)

// Answer data class - Match với API response
data class Answer(
    val answers_id: Int,
    val questions_id: Int,
    val match_key: String = "", // A, B, C, D hoặc category name
    val answer_text: String = "",
    val is_correct: Int = 0, // 1=correct, 0=incorrect
    val feedback: String = "",
    val media_url: String? = null,
    val order_index: Int = 0,
    val created_at: String = "",
    val updated_at: String = ""
)

// Student Answer data class - Match với API response
data class StudentAnswer(
    val student_answers_id: Int,
    val student_id: Int,
    val questions_id: Int,
    val course_id: Int,
    val answer_text: String = "",
    val answered_at: String = "",
    val created_at: String = "",
    val updated_at: String = "",
    val question: Question? = null,
    val course: Course? = null,
    val student: Student? = null
)

// Question Type Enum - Updated với 7 types
enum class QuestionType(val value: String, val displayName: String) {
    SINGLE_CHOICE("single_choice", "Trắc nghiệm 4 đáp án"),
    MULTIPLE_CHOICE("multiple_choice", "Trắc nghiệm nhiều đáp án"),
    MATCHING("matching", "Nối từ với hình ảnh/nghĩa"),
    CLASSIFICATION("classification", "Phân loại từ"),
    FILL_BLANK("fill_blank", "Điền vào chỗ trống"),
    ARRANGEMENT("arrangement", "Sắp xếp thành câu"),
    IMAGE_WORD("image_word", "Nhìn ảnh sắp xếp từ")
}

// Content Type Enum
enum class ContentType(val value: String, val displayName: String) {
    TEXT("text", "Văn bản"),
    QUIZ("quiz", "Câu hỏi"),
    VIDEO("video", "Video"),
    AUDIO("audio", "Âm thanh"),
    MINI_GAME("mini_game", "Trò chơi")
}

// Mini Game Type Enum
enum class MiniGameType(val value: String, val displayName: String) {
    QUIZ("quiz", "Câu hỏi"),
    MATCHING("matching", "Nối từ")
}
