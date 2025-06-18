package com.example.ckc_englihoo.DataClass

// API Response Wrapper - Match với API response
data class ProgressResponse<T>(
    val success: Boolean,
    val data: T
)

// Lesson Part Progress data class - Match với API response
data class LessonPartProgress(
    val student_id: String,
    val lesson_part_id: String,
    val lesson_part_title: String = "",
    val total_questions: Int = 0,
    val answered_questions: Int = 0,
    val correct_answers: String = "0", // Note: String type từ API
    val progress_percentage: Int = 0,
    val is_completed: Boolean = false,
    val required_correct_answers: Int = 0
)

// Lesson Progress data class - Match với API response
data class LessonProgress(
    val student_id: String,
    val lesson_level: String = "",
    val lesson_title: String = "",
    val total_parts: Int = 8, // Always 8 parts
    val completed_parts: Int = 0,
    val progress_percentage: Int = 0,
    val is_completed: Boolean = false
)

// Overall Progress data class - Match với API response
data class OverallProgress(
    val student_id: String,
    val student_name: String = "",
    val total_lessons: Int = 0,
    val completed_lessons: Int = 0,
    val overall_progress_percentage: Int = 0,
    val lessons_progress: List<LessonProgress> = emptyList()
)

// Student Progress data class - Simplified version
data class StudentProgress(
    val progressId: Int,
    val studentId: Int,
    val courseId: Int,
    val completionStatus: Boolean = false,
    val lastUpdated: String = ""
)
