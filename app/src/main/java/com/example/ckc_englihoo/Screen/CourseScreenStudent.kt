package com.example.ckc_englihoo.Screen

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
fun CourseScreenStudent(
    navController: NavController,
    viewModel: AppViewModel
) {
    // Collect states from ViewModel
    val currentStudent by viewModel.currentStudent.collectAsState()
    val courses by viewModel.courses.collectAsState()
    val courseEnrollments by viewModel.courseEnrollments.collectAsState()
    val teachers by viewModel.teachers.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // Load data when component mounts
    LaunchedEffect(Unit) {
        viewModel.loadAllCourses()
        viewModel.loadAllTeachers()
        currentStudent?.let { student ->
            viewModel.loadStudentEnrollments(student.student_id)
        }
    }

    // Process courses with details to CourseDisplayUI
    val allCourses = remember(courses, teachers, courseEnrollments) {
        getCoursesWithDetails(courses, teachers, courseEnrollments)
    }

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Tất cả") }

    val filterOptions = listOf("Tất cả", "Đang học", "Đã hoàn thành", "Chờ xác nhận")

    // Lọc khóa học theo tìm kiếm và bộ lọc
    val filteredCourses = allCourses.filter { course ->
        val matchesSearch = course.courseName.contains(searchQuery, ignoreCase = true) ||
                course.teacherName.contains(searchQuery, ignoreCase = true)
        val matchesFilter = selectedFilter == "Tất cả" || course.status == selectedFilter
        matchesSearch && matchesFilter
    }

    Scaffold(
        topBar = {
            currentStudent?.let { student ->
                CourseTopSection(student.fullname)
            }
        },
        containerColor = Color(0xFFF5F5F5)
    ) { paddingValues ->

        // Show loading state
        if (isLoading && courses.isEmpty()) {
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
                        viewModel.loadAllCourses()
                    }
                ) {
                    Text("Thử lại")
                }
            }
            return@Scaffold
        }
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        ) {
            Spacer(Modifier.height(8.dp))

            // Thanh tìm kiếm
            SearchSection(searchQuery) { searchQuery = it }

            Spacer(Modifier.height(16.dp))

            // Bộ lọc khóa học
            FilterSection(filterOptions, selectedFilter) { selectedFilter = it }

            Spacer(Modifier.height(16.dp))

            // Thống kê khóa học
            CourseStatsSection(allCourses)

            Spacer(Modifier.height(16.dp))

            // Nút đăng ký khóa học
            RegisterCourseButton {
                navController.navigate("course_registration")
            }

            Spacer(Modifier.height(16.dp))

            // Danh sách khóa học
            CourseSectionTitle("Khóa học của bạn (${filteredCourses.size})")
            CoursesGrid(filteredCourses)

            Spacer(Modifier.height(16.dp))
        }
    }
}

// Component cho phần đầu trang
@Composable
fun CourseTopSection(name: String) {
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
                    Surface(
                        modifier = Modifier.size(40.dp),
                        shape = CircleShape,
                        color = Color.White.copy(alpha = 0.2f)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.avatar),
                            contentDescription = "Avatar",
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Xin chào, $name",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                        Text(
                            text = "Khóa học của tôi",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
                Icon(
                    imageVector = Icons.Filled.Notifications,
                    tint = Color.White,
                    contentDescription = "Thông báo"
                )
            }
        }
    }
}

// Component tìm kiếm
@Composable
fun SearchSection(searchQuery: String, onSearchChange: (String) -> Unit) {
    OutlinedTextField(
        value = searchQuery,
        onValueChange = onSearchChange,
        label = { Text("Tìm kiếm khóa học, giáo viên...") },
        leadingIcon = {
            Icon(Icons.Default.Search, contentDescription = "Tìm kiếm")
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp)
    )
}

// Component bộ lọc
@Composable
fun FilterSection(
    filterOptions: List<String>,
    selectedFilter: String,
    onFilterChange: (String) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(filterOptions) { filter ->
            FilterChip(
                onClick = { onFilterChange(filter) },
                label = { Text(filter) },
                selected = selectedFilter == filter,
                modifier = Modifier.height(36.dp)
            )
        }
    }
}

// Component thống kê khóa học
@Composable
fun CourseStatsSection(courses: List<CourseDisplayUI>) {
    val completedCount = courses.count { it.status == "Đã hoàn thành" }
    val inProgressCount = courses.count { it.status == "Đang học" }
    val pendingCount = courses.count { it.status == "Chờ xác nhận" }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(
            title = "Hoàn thành",
            count = completedCount,
            color = Color(0xFF4CAF50),
            icon = Icons.Default.School,
            modifier = Modifier.weight(1f)
        )
        StatCard(
            title = "Đang học",
            count = inProgressCount,
            color = Color(0xFF2196F3),
            icon = Icons.Default.Book,
            modifier = Modifier.weight(1f)
        )
        StatCard(
            title = "Chờ xác nhận",
            count = pendingCount,
            color = Color(0xFFFF9800),
            icon = Icons.Default.FilterList,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun StatCard(
    title: String,
    count: Int,
    color: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier.height(100.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = count.toString(),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = title,
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// Component tiêu đề phần
@Composable
fun CourseSectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
    )
}

// Component danh sách khóa học dạng grid
@Composable
fun CoursesGrid(courses: List<CourseDisplayUI>) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp),
        modifier = Modifier.heightIn(max = 600.dp)
    ) {
        items(courses) { course ->
            EnhancedCourseCard(course)
        }
    }
}

// Component thẻ khóa học được cải tiến
@Composable
fun EnhancedCourseCard(course: CourseDisplayUI) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* Xử lý click vào khóa học */ },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header với tên khóa học và trạng thái
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = course.courseName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = course.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Status badge
                Surface(
                    color = when (course.status) {
                        "Đã hoàn thành" -> Color(0xFF4CAF50) // Green
                        "Đang học" -> Color(0xFF2196F3) // Blue
                        "Chờ xác nhận" -> Color(0xFFFF9800) // Orange
                        else -> Color(0xFF9E9E9E) // Gray
                    },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = course.status,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Thông tin giáo viên
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Giáo viên: ${course.teacherName}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Progress bar và phần trăm (chỉ hiển thị khi không phải "Chờ xác nhận")
            if (course.progress > 0f && course.status != "Chờ xác nhận") {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Tiến độ",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "${(course.progress * 100).toInt()}%",
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF5D31FF)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = course.progress,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = Color(0xFF5D31FF),
                    trackColor = Color(0xFF5D31FF).copy(alpha = 0.2f)
                )
            }
        }
    }
}

// Helper function để xử lý courses từ ViewModel data
// Chỉ hiển thị các courses có trong enrollments của student
fun getCoursesWithDetails(
    courses: List<Course>,
    teachers: List<Teacher>,
    enrollments: List<CourseEnrollment>
): List<CourseDisplayUI> {
    // Chỉ lấy các courses mà student đã enroll
    return enrollments.mapNotNull { enrollment ->
        val course = courses.find { it.course_id == enrollment.assigned_course_id }
        if (course != null) {
            // Tìm teacher cho course này
            val teacher = teachers.find { teacher ->
                // Tìm teacher được assign cho course này
                teacher.courses.any { courseWithPivot ->
                    courseWithPivot.course_id == course.course_id
                }
            }

            val progress = when (enrollment.status) {
                3 -> 1.0f // Hoàn thành
                2 -> 0.6f // Đang học
                1 -> 0.0f // Đang chờ xác nhận - không có progress
                else -> 0.0f // Không xác định
            }

            val status = when (enrollment.status) {
                3 -> "Đã hoàn thành"
                2 -> "Đang học"
                1 -> "Chờ xác nhận"
                else -> "Không xác định"
            }

            CourseDisplayUI(
                courseId = course.course_id,
                courseName = course.course_name,
                description = course.description,
                teacherName = teacher?.fullname ?: "Chưa có giáo viên",
                progress = progress,
                status = status
            )
        } else {
            null // Course không tồn tại
        }
    }
}

// Component nút đăng ký khóa học
@Composable
fun RegisterCourseButton(onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF4CAF50)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            ),
            shape = RoundedCornerShape(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Đăng ký",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Đăng ký khóa học mới",
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}