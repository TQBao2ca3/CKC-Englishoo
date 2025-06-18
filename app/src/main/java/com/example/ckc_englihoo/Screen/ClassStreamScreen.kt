package com.example.ckc_englihoo.Screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ckc_englihoo.API.AppViewModel
import com.example.ckc_englihoo.DataClass.*
import com.example.ckc_englihoo.Utils.formatTimeAgo
import androidx.compose.ui.text.style.TextAlign
import java.util.Date



@Composable
fun ClassStreamScreen(
    navController: NavController,
    viewModel: AppViewModel,
    courseId: Int = 1,
    classTitle: String = "Tiếng Anh 101"
) {
    // Collect states from ViewModel
    val classPosts by viewModel.classPosts.collectAsState()
    val teachers by viewModel.teachers.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // Load data when component mounts
    LaunchedEffect(courseId) {
        viewModel.loadClassPostsByCourse(courseId)
        viewModel.loadAllTeachers()
    }

    // Process class posts for display to ClassPostDisplayUI
    val allClassPosts = remember(classPosts, teachers) {
        getClassPostsForCourse(classPosts, teachers, courseId)
    }
    
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Tiêu đề khóa học
        Text(
            text = classTitle,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        // Khung input "Announce something to your class"
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            TextField(
                value = "",
                onValueChange = {},
                placeholder = { Text("Thông báo gì đó cho lớp học của bạn...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                enabled = false  // chỉ minh hoạ input
            )
        }
        
        // Show loading indicator for content
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp))
            }
        }

        // Danh sách bài đăng
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(allClassPosts) { post ->
                ClassPostCard(post = post)
            }
        }
    }
}

@Composable
fun ClassPostCard(post: ClassPostDisplayUI) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header với avatar và thông tin giáo viên
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Avatar giáo viên (sử dụng Icon làm placeholder)
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Avatar giáo viên",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = post.teacherName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = post.time,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Nội dung bài đăng
            Text(
                text = post.content,
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = MaterialTheme.typography.bodyMedium.lineHeight
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Khu vực bình luận (gợi ý)
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Comment,
                    contentDescription = "Icon bình luận",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Thêm bình luận...",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// Helper function để xử lý ClassPosts từ ViewModel data
fun getClassPostsForCourse(
    classPosts: List<ClassPost>,
    teachers: List<Teacher>,
    courseId: Int
): List<ClassPostDisplayUI> {
    return classPosts.filter { it.course_id == courseId }.map { classPost ->
        // Handle dynamic author (Teacher or Student)
        val authorName = when (classPost.author_type) {
            "teacher" -> {
                val teacher = classPost.author as? Teacher
                teacher?.fullname ?: "Giáo viên"
            }
            "student" -> {
                val student = classPost.author as? Student
                student?.fullname ?: "Học sinh"
            }
            else -> "Người dùng"
        }

        ClassPostDisplayUI(
            classPostId = classPost.post_id,
            teacherName = authorName,
            teacherAvatar = null, // API không có avatar field
            time = formatTimeAgo(classPost.created_at),
            content = classPost.content,
            courseName = classPost.course?.course_name ?: "Khóa học"
        )
    }.sortedByDescending { it.classPostId } // Sắp xếp theo thời gian mới nhất
}



