package com.example.ckc_englihoo.Screen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class LessonPart(
    val title: String,
    val type: String, // "listening", "reading", "quiz", "speaking"
    val isCompleted: Boolean,
    val isLocked: Boolean = false,
    val duration: String = ""
)

data class Lesson(
    val id: Int,
    val title: String,
    val description: String,
    val parts: List<LessonPart>,
    val isCompleted: Boolean = false,
    val progress: Float = 0f,
    val difficulty: String = "Cơ bản"
)

@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonScreenStudent(courseName: String = "Tiếng Anh 101") {
    // Dữ liệu mẫu với thông tin chi tiết hơn
    val lessons = remember {
        listOf(
            Lesson(
                id = 1,
                title = "Bài 1: Giới thiệu bản thân",
                description = "Học cách giới thiệu tên, tuổi và nghề nghiệp",
                parts = listOf(
                    LessonPart("Từ vựng cơ bản", "reading", true, false, "10 phút"),
                    LessonPart("Nghe hội thoại", "listening", true, false, "15 phút"),
                    LessonPart("Luyện phát âm", "speaking", false, false, "20 phút"),
                    LessonPart("Kiểm tra", "quiz", false, false, "5 phút")
                ),
                isCompleted = false,
                progress = 0.5f,
                difficulty = "Cơ bản"
            ),
            Lesson(
                id = 2,
                title = "Bài 2: Gia đình và bạn bè",
                description = "Mô tả các thành viên trong gia đình",
                parts = listOf(
                    LessonPart("Từ vựng gia đình", "reading", false, false, "12 phút"),
                    LessonPart("Nghe mô tả", "listening", false, false, "18 phút"),
                    LessonPart("Thực hành nói", "speaking", false, true, "25 phút"),
                    LessonPart("Bài tập", "quiz", false, true, "8 phút")
                ),
                isCompleted = false,
                progress = 0f,
                difficulty = "Cơ bản"
            ),
            Lesson(
                id = 3,
                title = "Bài 3: Thì hiện tại đơn",
                description = "Ngữ pháp và cách sử dụng thì hiện tại đơn",
                parts = listOf(
                    LessonPart("Lý thuyết", "reading", false, true, "20 phút"),
                    LessonPart("Ví dụ thực tế", "listening", false, true, "15 phút"),
                    LessonPart("Luyện tập", "quiz", false, true, "30 phút")
                ),
                isCompleted = false,
                progress = 0f,
                difficulty = "Trung bình"
            )
        )
    }

    val completedLessons = lessons.count { it.isCompleted }
    val totalLessons = lessons.size
    val overallProgress = lessons.sumOf { it.progress.toDouble() } / totalLessons

    Scaffold(
        topBar = { LessonTopBar(courseName) },
        containerColor = Color(0xFFF5F5F5)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        ) {
            Spacer(Modifier.height(8.dp))

            // Progress Overview
            ProgressOverviewSection(
                courseName = courseName,
                completedLessons = completedLessons,
                totalLessons = totalLessons,
                overallProgress = overallProgress.toFloat()
            )

            Spacer(Modifier.height(16.dp))

            // Lessons List
            LessonSectionTitle("Danh sách bài học")
            LessonsList(lessons)

            Spacer(Modifier.height(16.dp))
        }
    }
}

// Component Top Bar
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonTopBar(courseName: String) {
    Surface(
        color = Color(0xFF5D31FF),
        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { /* Navigate back */ }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Quay lại",
                    tint = Color.White
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = courseName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Text(
                    text = "Danh sách bài học",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }
    }
}

// Component Progress Overview
@Composable
fun ProgressOverviewSection(
    courseName: String,
    completedLessons: Int,
    totalLessons: Int,
    overallProgress: Float
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Tiến độ khóa học",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "$completedLessons/$totalLessons bài học hoàn thành",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Circular progress indicator
                Box(
                    modifier = Modifier.size(60.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        shape = CircleShape,
                        color = Color(0xFF5D31FF).copy(alpha = 0.1f)
                    ) {}
                    Text(
                        text = "${(overallProgress * 100).toInt()}%",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF5D31FF)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LinearProgressIndicator(
                progress = overallProgress,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = Color(0xFF5D31FF),
                trackColor = Color(0xFF5D31FF).copy(alpha = 0.2f)
            )
        }
    }
}

// Component Lesson Section Title
@Composable
fun LessonSectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
    )
}

// Component Lessons List
@Composable
fun LessonsList(lessons: List<Lesson>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = Modifier.height(600.dp)
    ) {
        items(lessons) { lesson ->
            EnhancedLessonCard(lesson)
        }
    }
}

// Component Enhanced Lesson Card
@Composable
fun EnhancedLessonCard(lesson: Lesson) {
    var expanded by remember { mutableStateOf(false) }

    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .animateContentSize(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = lesson.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = lesson.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Icon(
                    imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = if (expanded) "Thu gọn" else "Mở rộng",
                    tint = Color(0xFF5D31FF)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Progress and difficulty
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Difficulty badge
                Surface(
                    color = when (lesson.difficulty) {
                        "Cơ bản" -> Color(0xFF4CAF50)
                        "Trung bình" -> Color(0xFF2196F3)
                        else -> Color(0xFFFF9800)
                    },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = lesson.difficulty,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Text(
                    text = "${(lesson.progress * 100).toInt()}% hoàn thành",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF5D31FF)
                )
            }

            if (lesson.progress > 0f) {
                Spacer(modifier = Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = lesson.progress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = Color(0xFF5D31FF),
                    trackColor = Color(0xFF5D31FF).copy(alpha = 0.2f)
                )
            }

            // Expanded content
            if (expanded) {
                Spacer(modifier = Modifier.height(16.dp))
                lesson.parts.forEach { part ->
                    LessonPartItem(part)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}
// Component Lesson Part Item
@Composable
fun LessonPartItem(part: LessonPart) {
    val icon = when (part.type) {
        "listening" -> Icons.Default.VolumeUp
        "reading" -> Icons.Default.Book
        "quiz" -> Icons.Default.Quiz
        "speaking" -> Icons.Default.PlayArrow
        else -> Icons.Default.PlayArrow
    }

    val iconColor = when {
        part.isLocked -> Color.Gray
        part.isCompleted -> Color(0xFF4CAF50)
        else -> Color(0xFF5D31FF)
    }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = if (part.isCompleted) Color(0xFF4CAF50).copy(alpha = 0.1f)
               else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon
            Surface(
                modifier = Modifier.size(40.dp),
                shape = CircleShape,
                color = iconColor.copy(alpha = 0.2f)
            ) {
                Icon(
                    imageVector = if (part.isLocked) Icons.Default.Lock else icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(8.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Content
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = part.title,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium,
                    color = if (part.isLocked) Color.Gray else MaterialTheme.colorScheme.onSurface
                )
                if (part.duration.isNotEmpty()) {
                    Text(
                        text = part.duration,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Status and Action
            if (part.isCompleted) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Hoàn thành",
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(24.dp)
                )
            } else if (!part.isLocked) {
                Button(
                    onClick = { /* Navigate to lesson content */ },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF5D31FF)
                    ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(32.dp)
                ) {
                    Text(
                        text = "Bắt đầu",
                        fontSize = 12.sp,
                        color = Color.White
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Đã khóa",
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}
