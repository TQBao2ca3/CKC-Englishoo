package com.example.ckc_englihoo.Screen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ckc_englihoo.API.AppViewModel
import com.example.ckc_englihoo.DataClass.*
import com.example.ckc_englihoo.R
import androidx.compose.ui.text.style.TextAlign
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenStudent(
    navController: NavController,
    viewModel: AppViewModel
) {
    // Collect states from ViewModel
    val currentStudent by viewModel.currentStudent.collectAsState()
    val courses by viewModel.studentCourses.collectAsState()
    val notifications by viewModel.notifications.collectAsState()
    val studentProgress by viewModel.studentProgress.collectAsState()
    val examResults by viewModel.examResults.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // Load data when student is available
    LaunchedEffect(currentStudent) {
        currentStudent?.let { student ->
            viewModel.loadStudentCourses(student.student_id)
            viewModel.loadStudentNotifications(student.student_id)
            viewModel.loadStudentProgressData(student.student_id)
            viewModel.loadStudentExamResults(student.student_id)
        }
    }

    // Process data for UI
    val coursesInProgress = remember(courses, studentProgress) {
        getCoursesWithProgress(courses, studentProgress)
    }
    val notificationsForDisplay = remember(notifications) {
        getNotificationsForDisplay(notifications)
    }
    val overallProgress = remember(studentProgress) {
        getOverallProgress(studentProgress)
    }
    val progressData = remember(examResults) {
        getProgressData(examResults)
    }

    Scaffold(
        topBar = {
            currentStudent?.let { student ->
                TopSection(student.fullname, Progress = overallProgress)
            }
        },
        bottomBar = {  },
        containerColor = Color(0xFFF5F5F5)
    ) { paddingValues ->

        // Show loading state
        if (isLoading && currentStudent == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            return@Scaffold
        }

        // Show error state
        errorMessage?.let { error ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Có lỗi xảy ra",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = error,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        viewModel.clearError()
                        currentStudent?.let { student ->
                            viewModel.refreshCurrentStudentData()
                        }
                    }
                ) {
                    Text("Thử lại")
                }
            }
            return@Scaffold
        }

        // Show empty state if no student
        if (currentStudent == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Vui lòng đăng nhập để xem thông tin",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            return@Scaffold
        }

        // Main content
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        ) {
            Spacer(Modifier.height(8.dp))

            // Show loading indicator for content
            if (isLoading && courses.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                }
            }

            SectionTitle("Khóa đang học")
            CoursesRow(coursesInProgress)
            Spacer(Modifier.height(16.dp))
            SectionTitle("Thông báo")
            NotificationsColumn(notificationsForDisplay)
            Spacer(Modifier.height(16.dp))
            SectionTitle("Tiến độ tổng quan")
            ProgressChart(progressData)
            Spacer(Modifier.height(16.dp))
        }
    }
}

@Composable
fun TopSection(name: String, Progress: Float) {
    Surface(
        color = Color(0xFF5D31FF),
        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(R.drawable.avatar),
                        contentDescription = "Avatar",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = name,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )
                }
                Row {
                    Icon(
                        imageVector = Icons.Filled.Notifications,
                        tint = Color.White,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                    Icon(
                        imageVector = Icons.Filled.Chat,
                        tint = Color.White,
                        contentDescription = null
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(text = "Tiến Độ", color = Color.White, fontWeight = FontWeight.Medium)
            LinearProgressIndicator(
                progress = Progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = Color(0xFF00FFBA),
                trackColor = Color.White.copy(alpha = 0.3f)
            )
        }
    }
}

@Composable
fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
    )
}

@Composable
fun CoursesRow(courses: List<CourseDisplayUI>) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(courses) { course ->
            ElevatedCard(
                modifier = Modifier.width(200.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(course.courseName, style = MaterialTheme.typography.titleSmall)
                    Spacer(Modifier.height(4.dp))
                    Text(course.status, style = MaterialTheme.typography.bodySmall)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "GV: ${course.teacherName}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(Modifier.height(8.dp))
                    LinearProgressIndicator(
                        progress = course.progress,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp))
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = "%${(course.progress * 100).toInt()}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@Composable
fun NotificationsColumn(notifications: List<NotificationDisplayUI>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .heightIn(max = 200.dp)
    ) {
        items(notifications) { note ->
            Card(
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.elevatedCardColors(containerColor = Color(0xFFE3F2FD)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(note.title, style = MaterialTheme.typography.titleSmall)
                    Spacer(Modifier.height(2.dp))
                    Text(note.message, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

@Composable
fun ProgressChart(data: List<Float>) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .height(220.dp)
            .fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "My Progress", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            Canvas(modifier = Modifier.fillMaxSize()) {
                val step = size.width / (data.size - 1)
                val points = data.mapIndexed { i, v -> Offset(x = step * i, y = size.height * (1 - v)) }
                points.zipWithNext { a, b ->
                    drawLine(
                        color = Color(0xFF00FFBA),
                        start = a,
                        end = b,
                        strokeWidth = 4.dp.toPx(),
                        cap = StrokeCap.Round
                    )
                }
            }
        }
    }
}

// Helper functions để xử lý dữ liệu từ ViewModel
fun getCoursesWithProgress(
    courses: List<Course>,
    progressList: List<StudentProgress>
): List<CourseDisplayUI> {
    return courses.map { course ->
        val progress = progressList.find { it.courseId == course.course_id }
        val progressValue = if (progress?.completionStatus == true) 1.0f else 0.5f // Simplified progress calculation

        CourseDisplayUI(
            courseId = course.course_id,
            courseName = course.course_name,
            description = course.description,
            teacherName = "Giáo viên", // Will be loaded separately via teacher API
            progress = progressValue,
            status = when (course.status) {
                "Đang mở lớp" -> "Đang học"
                "Đã hoàn thành" -> "Đã hoàn thành"
                else -> "Chờ đăng ký"
            }
        )
    }
}

fun getNotificationsForDisplay(notifications: List<Notification>): List<NotificationDisplayUI> {
    return notifications.take(5).map { notification ->
        NotificationDisplayUI(
            notificationId = notification.notification_id,
            title = notification.title,
            message = notification.message
        )
    }
}

fun getOverallProgress(progressList: List<StudentProgress>): Float {
    if (progressList.isEmpty()) return 0f

    val completedCount = progressList.count { it.completionStatus }
    return completedCount.toFloat() / progressList.size
}

fun getProgressData(examResults: List<ExamResult>): List<Float> {
    return if (examResults.isNotEmpty()) {
        examResults.map { result ->
            val avgScore = listOfNotNull(
                result.listening_score, // Use helper property
                result.speaking_score,
                result.reading_score,
                result.writing_score
            ).average()
            (avgScore / 10).toFloat() // API scores are 0-10, not 0-100
        }
    } else {
        // Fallback data when no exam results
        listOf(0.85f, 0.88f, 0.92f, 0.96f, 0.90f)
    }
}