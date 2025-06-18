package com.example.ckc_englihoo.DataClass

// Lesson data class - Match với API response
data class Lesson(
    val level: String = "", // Acts as ID - A1, A2, A3, A2/6, B1
    val title: String = "",
    val description: String = "",
    val order_index: Int = 0,
    val created_at: String = "",
    val updated_at: String = "",
    val lesson_parts: List<LessonPart> = emptyList(), // Nested lesson parts
    val courses: List<Course> = emptyList() // Chỉ có trong single lesson API
)

// Lesson Part data class - Match với API response
data class LessonPart(
    val lesson_part_id: Int,
    val level: String = "",
    val part_type: String = "", // Vocabulary, Grammar, Listening, Speaking, Reading, Writing, Pronunciation, Practice Test
    val content: String = "",
    val order_index: Int = 0,
    val created_at: String = "",
    val updated_at: String = "",
    val contents: List<LessonPartContent> = emptyList(), // Nested contents
    val lesson: Lesson? = null // Chỉ có trong single lesson part API
)

// Lesson Part Content data class - Match với API response
data class LessonPartContent(
    val contents_id: Int,
    val lesson_part_id: Int,
    val content_type: String = "", // text, quiz, video, audio, mini_game
    val content_data: String = "",
    val mini_game_type: String? = null, // quiz, matching, null
    val created_at: String = "",
    val updated_at: String = ""
)

// Lesson Part Score data class - Match với API response
data class LessonPartScore(
    val score_id: Int,
    val student_id: Int,
    val lesson_part_id: Int,
    val course_id: Int,
    val attempt_no: Int = 1,
    val score: Double = 0.0,
    val total_questions: Int = 0,
    val correct_answers: Int = 0,
    val submit_time: String = "",
    val created_at: String = "",
    val updated_at: String = ""
)
