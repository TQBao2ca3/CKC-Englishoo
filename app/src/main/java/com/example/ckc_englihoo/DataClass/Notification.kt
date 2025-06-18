package com.example.ckc_englihoo.DataClass

// Notification data class - Match với API response
data class Notification(
    val notification_id: Int,
    val admin: Int, // Admin ID who created
    val target: Int, // 0=all students, 1=specific student
    val title: String = "",
    val message: String = "",
    val notification_date: String = "",
    val status: Int = 1, // 1=active, 0=inactive
    val created_at: String = "",
    val updated_at: String = ""
)

// Notification Target Enum
enum class NotificationTarget(val value: Int, val displayName: String) {
    ALL_STUDENTS(0, "Tất cả học sinh"),
    SPECIFIC_STUDENT(1, "Học sinh cụ thể")
}

// Notification Status Enum
enum class NotificationStatus(val value: Int, val displayName: String) {
    INACTIVE(0, "Ẩn"),
    ACTIVE(1, "Hiển thị")
}
