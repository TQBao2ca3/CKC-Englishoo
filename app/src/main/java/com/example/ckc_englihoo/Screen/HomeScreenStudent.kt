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
import com.example.ckc_englihoo.R


data class HomeCourse(
    val id: Int,
    val name: String,
    val description: String,
    val teacher: String,
    val progress: Float,
    val status: String
)

data class Notification(
    val id: Int,
    val title: String,
    val description: String
)


@OptIn(ExperimentalMaterial3Api::class)
//@Preview(showBackground = true)
@Composable
fun HomeScreenS() {
    val studentName = "Nguyễn Văn A"
    val coursesInProgress = remember {
        listOf(
            HomeCourse(1, "Tiếng Anh 101", "Cơ bản về tiếng Anh", "Ngô Kim Phụng", 0.6f, "Đang học"),
            HomeCourse(2, "Tiếng Anh 102", "Ngữ pháp nâng cao", "Lê Minh Hà", 0.3f, "Đang học"),
            HomeCourse(3, "Giao tiếp tiếng Anh", "Thực hành giao tiếp", "Phạm Thị Lan", 0.8f, "Đang học")
        )
    }
    val notifications = remember {
        listOf(
            Notification(1, "Chúc mừng!", "Bạn đã hoàn thành 70% khóa học Tiếng Anh 101."),
            Notification(2, "Tin mới", "Bài kiểm tra trình độ mới đã được cập nhật.")
        )
    }
    val progressData = listOf(0.85f, 0.88f, 0.92f, 0.96f, 0.90f)

    Scaffold(
        topBar = { TopSection(studentName, Progress = 0.6f) },
        bottomBar = {  },
        containerColor = Color(0xFFF5F5F5)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        ) {
            Spacer(Modifier.height(8.dp))
            SectionTitle("Khóa đang học")
            CoursesRow(coursesInProgress)
            Spacer(Modifier.height(16.dp))
            SectionTitle("Thông báo")
            NotificationsColumn(notifications)
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
fun CoursesRow(courses: List<HomeCourse>) {
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
                    Text(course.name, style = MaterialTheme.typography.titleSmall)
                    Spacer(Modifier.height(4.dp))
                    Text(course.status, style = MaterialTheme.typography.bodySmall)
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
fun NotificationsColumn(notifications: List<Notification>) {
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
                    Text(note.description, style = MaterialTheme.typography.bodyMedium)
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

