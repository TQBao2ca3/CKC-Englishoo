package com.example.ckc_englihoo.DataClass

// Class Post data class - Match với API response
data class ClassPost(
    val post_id: Int,
    val course_id: Int,
    val author_id: Int,
    val author_type: String = "", // "teacher" or "student"
    val title: String = "",
    val content: String = "",
    val status: Int = 1, // 1=active, 0=inactive
    val created_at: String = "",
    val updated_at: String = "",
    val author: Any? = null, // Dynamic - Teacher or Student object
    val course: Course? = null,
    val comments: List<ClassPostComment> = emptyList()
)

// Class Post Comment data class - Match với API response
data class ClassPostComment(
    val comment_id: Int,
    val post_id: Int,
    val author_id: Int,
    val author_type: String = "", // "teacher" or "student"
    val content: String = "",
    val status: Int = 1,
    val created_at: String = "",
    val updated_at: String = "",
    val author: Any? = null // Dynamic - Teacher or Student object
)

// Author Type Enum
enum class AuthorType(val value: String, val displayName: String) {
    TEACHER("teacher", "Giáo viên"),
    STUDENT("student", "Học sinh")
}

// Post Status Enum
enum class PostStatus(val value: Int, val displayName: String) {
    INACTIVE(0, "Ẩn"),
    ACTIVE(1, "Hiển thị")
}
