package com.example.ckc_englihoo.Screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ckc_englihoo.API.AppViewModel
import com.example.ckc_englihoo.DataClass.*

@Composable
fun CourseListScreen(
    navController: NavController,
    viewModel: AppViewModel
) {
    // Collect states from ViewModel
    val courseEnrollments by viewModel.courseEnrollments.collectAsState()
    val courses by viewModel.courses.collectAsState()
    val courseStudentCounts by viewModel.courseStudentCounts.collectAsState()
    val currentStudent by viewModel.currentStudent.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // Load student enrollments and courses when screen loads
    LaunchedEffect(currentStudent) {
        currentStudent?.let { student ->
            viewModel.loadStudentEnrollments(student.student_id)
            viewModel.loadAllCourses()
        }
    }

    // Filter enrollments with status >= 2 (Đang học và Đã hoàn thành)
    val validEnrollments = courseEnrollments.filter { it.status >= 2 }

    // Get courses from valid enrollments
    val studentCourses = validEnrollments.mapNotNull { enrollment ->
        courses.find { it.course_id == enrollment.assigned_course_id }
    }

    // Load student counts for all courses
    LaunchedEffect(studentCourses) {
        if (studentCourses.isNotEmpty()) {
            val courseIds = studentCourses.map { it.course_id }
            viewModel.loadMultipleCourseStudentCounts(courseIds)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Text(
            text = "Khóa học của tôi",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Student info
        currentStudent?.let { student ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Học sinh: ${student.fullname}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Tổng số khóa học: ${studentCourses.size}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }

        // Loading state
        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        // Error state
        errorMessage?.let { error ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = error,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer
                )
            }
        }

        // Course list
        if (studentCourses.isNotEmpty()) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(studentCourses) { course ->
                    CourseCard(
                        course = course,
                        studentCount = courseStudentCounts[course.course_id]?.total_students ?: 0,
                        onClick = {
                            // Navigate to ClassStreamScreen for this course
                            navController.navigate("class_stream/${course.course_id}/${course.course_name}")
                        }
                    )
                }
            }
        } else if (!isLoading) {
            // Empty state
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Book,
                        contentDescription = "No courses",
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Chưa có khóa học nào",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        }
    }
}

@Composable
fun CourseCard(
    course: Course,
    studentCount: Int = 0,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Course header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = course.course_name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    Text(
                        text = course.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                
                // Course level badge
                Surface(
                    color = when (course.level) {
                        "A1" -> Color(0xFF4CAF50)
                        "A2" -> Color(0xFF2196F3)
                        "B1" -> Color(0xFFFF9800)
                        "B2" -> Color(0xFFE91E63)
                        "C1" -> Color(0xFF9C27B0)
                        "C2" -> Color(0xFFF44336)
                        else -> MaterialTheme.colorScheme.primary
                    },
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = course.level,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = Color.White,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Course info row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                // Students count
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Group,
                        contentDescription = "Students",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "$studentCount học sinh",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Action hint
            Text(
                text = "Nhấn để xem lớp học →",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}
