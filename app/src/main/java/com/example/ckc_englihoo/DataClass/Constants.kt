package com.example.ckc_englihoo.DataClass

// User Types - Used in Response.kt and authentication
enum class UserType(val value: String, val displayName: String) {
    STUDENT("student", "Học sinh"),
    TEACHER("teacher", "Giáo viên"),
    ADMIN("admin", "Quản trị viên")
}

// Notification Types - Used in UIModels
enum class NotificationType(val displayName: String) {
    ASSIGNMENT("Bài tập"),
    GRADE("Điểm số"),
    ANNOUNCEMENT("Thông báo"),
    REMINDER("Nhắc nhở"),
    SYSTEM("Hệ thống")
}
