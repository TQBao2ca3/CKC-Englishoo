package com.example.ckc_englihoo.DataClass

// Exam Result data class - Match với API response
data class ExamResult(
    val exam_result_id: Int,
    val student_id: Int,
    val course_id: Int,
    val exam_date: String = "",
    val lisstening_score: Double = 0.0, // Note: API typo "lisstening"
    val reading_score: Double = 0.0,
    val speaking_score: Double = 0.0,
    val writing_score: Double = 0.0,
    val overall_status: Int = 0, // 1=pass, 0=fail
    val created_at: String = "",
    val updated_at: String = "",
    val student: Student? = null,
    val course: Course? = null
) {
    // Helper property với correct spelling
    val listening_score: Double get() = lisstening_score

    // Calculate average score
    val average_score: Double get() =
        (lisstening_score + reading_score + speaking_score + writing_score) / 4.0
}

// Student Evaluation data class - Match với API response
data class StudentEvaluation(
    val evaluation_id: Int,
    val student_id: Int,
    val progress_id: Int,
    val exam_result_id: Int,
    val evaluation_date: String = "",
    val final_status: Int = 0, // 0=fail, 1=pass
    val remark: String = "",
    val created_at: String = "",
    val updated_at: String = "",
    val student: Student? = null,
    val exam_result: ExamResult? = null
)

// Exam Status Enum
enum class ExamStatus(val value: Int, val displayName: String) {
    FAIL(0, "Không đạt"),
    PASS(1, "Đạt")
}
