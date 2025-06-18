package com.example.ckc_englihoo.DataClass

import androidx.compose.runtime.Composable

// Course Card UI Model - Để hiển thị course trong UI
data class CourseCardUI(
    val courseId: Int,
    val title: String,
    val subtitle: String,
    val icon: @Composable () -> Unit,
    val score: Int = 0, // Điểm thay vì coins
    val progress: Float = 0f,
    val onClick: () -> Unit
)

// Lesson Card UI Model - Để hiển thị lesson trong UI
data class LessonCardUI(
    val lessonId: Int,
    val title: String,
    val description: String,
    val progress: Float = 0f,
    val isCompleted: Boolean = false,
    val isLocked: Boolean = false,
    val score: Int = 0, // Điểm thay vì coins
    val duration: String = "",
    val onClick: () -> Unit
)

// Student Profile UI Model - Kết hợp từ Student, ExamResult, StudentProgress
data class StudentProfileUI(
    val studentId: Int,
    val name: String,
    val email: String,
    val avatarUrl: String? = null,
    val totalScore: Int = 0, // Từ ExamResult
    val completedCourses: Int = 0, // Từ StudentProgress
    val totalTimeSpent: Int = 0, // Thời gian học (phút)
    val averageScore: Double = 0.0 // Điểm trung bình
)

// Teacher Profile UI Model
data class TeacherProfileUI(
    val teacherId: Int,
    val name: String,
    val email: String,
    val avatarUrl: String? = null,
    val totalStudents: Int = 0,
    val totalCourses: Int = 0,
    val averageStudentScore: Double = 0.0
)

// Notification UI Model
data class NotificationUI(
    val notificationId: Int,
    val title: String,
    val message: String,
    val timestamp: String,
    val isRead: Boolean = false,
    val type: NotificationType = NotificationType.SYSTEM
)

// Dashboard UI Model - Trang chủ tổng hợp (không có coins)
data class DashboardUI(
    val studentId: Int,
    val welcomeMessage: String,
    val totalScore: Int, // Tổng điểm thay vì coins
    val todayScore: Int, // Điểm hôm nay thay vì coins
    val recentCourses: List<CourseCardUI>,
    val weeklyProgress: Float, // Tính từ StudentProgress
    val notifications: List<NotificationUI>
)

// Leaderboard UI Model (sử dụng điểm thay vì coins)
data class LeaderboardUI(
    val rank: Int,
    val studentId: Int,
    val studentName: String,
    val avatarUrl: String? = null,
    val totalScore: Int, // Điểm thay vì coins
    val averageScore: Double = 0.0,
    val isCurrentUser: Boolean = false
)

// Assignment Card UI Model
data class AssignmentCardUI(
    val assignmentId: Int,
    val title: String,
    val description: String,
    val dueDate: String,
    val isCompleted: Boolean = false,
    val score: Int? = null,
    val maxScore: Int,
    val onClick: () -> Unit
)

// Progress Summary UI Model
data class ProgressSummaryUI(
    val studentId: Int,
    val courseId: Int,
    val courseName: String,
    val totalLessons: Int,
    val completedLessons: Int,
    val totalScore: Int,
    val averageScore: Double,
    val progressPercentage: Float
)

// UI Model cho ClassPost hiển thị trong Stream
data class ClassPostDisplayUI(
    val classPostId: Int,
    val teacherName: String,
    val teacherAvatar: String?,
    val time: String,
    val content: String,
    val courseName: String
)

// UI Model cho Course hiển thị trong Course Screen
data class CourseDisplayUI(
    val courseId: Int,
    val courseName: String,
    val description: String,
    val teacherName: String,
    val progress: Float,
    val status: String
)

// UI Model cho Notification hiển thị trong Home Screen
data class NotificationDisplayUI(
    val notificationId: Int,
    val title: String,
    val message: String
)

// UI Models cho Lesson Screen
data class LessonDisplayUI(
    val lessonId: String,
    val title: String,
    val description: String,
    val parts: List<LessonPartDisplayUI>,
    val isCompleted: Boolean = false,
    val progress: Float = 0f,
    val difficulty: String = "Cơ bản"
)

data class LessonPartDisplayUI(
    val lessonPartId: Int,
    val title: String,
    val type: String, // "listening", "reading", "quiz", "speaking"
    val isCompleted: Boolean,
    val isLocked: Boolean = false,
    val duration: String = "",
    val score: Double = 0.0
)