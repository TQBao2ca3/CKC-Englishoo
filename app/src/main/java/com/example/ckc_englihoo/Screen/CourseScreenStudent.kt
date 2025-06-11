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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.ckc_englihoo.R

data class Course(
    val id: Int,
    val name: String,
    val description: String,
    val teacher: String,
    val progress: Float,
    val status: String
)

@OptIn(ExperimentalMaterial3Api::class)
//@Preview(showBackground = true)
@Composable
fun CourseScreenStudent() {
    val studentName = "Nguyễn Văn A"

    // Dữ liệu mẫu với thêm thông tin
    val allCourses = remember {
        listOf(
            Course(1, "Tiếng Anh 101", "Cơ bản về tiếng Anh", "Ngô Kim Phụng", 0.6f, "Đang học"),
            Course(2, "Tiếng Anh 102", "Ngữ pháp nâng cao", "Lê Minh Hà", 1.0f, "Đã hoàn thành"),
            Course(3, "Học Nói Tiếng Anh", "Thực hành kỹ năng nói", "Phạm Thị Lan", 0.2f, "Đang học"),
            Course(4, "Văn phạm Nâng Cao", "Ngữ pháp chuyên sâu", "Trần Văn B", 0.0f, "Chưa học"),
            Course(5, "TOEIC Preparation", "Luyện thi TOEIC", "Nguyễn Thị C", 0.8f, "Đang học"),
            Course(6, "Business English", "Tiếng Anh thương mại", "Lê Văn D", 0.4f, "Đang học")
        )
    }

    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Tất cả") }

    val filterOptions = listOf("Tất cả", "Đang học", "Đã hoàn thành", "Chờ Đăng Ký")

    // Lọc khóa học theo tìm kiếm và bộ lọc
    val filteredCourses = allCourses.filter { course ->
        val matchesSearch = course.name.contains(searchQuery, ignoreCase = true) ||
                course.teacher.contains(searchQuery, ignoreCase = true)
        val matchesFilter = selectedFilter == "Tất cả" || course.status == selectedFilter
        matchesSearch && matchesFilter
    }

    Scaffold(
        topBar = { CourseTopSection(studentName) },
        containerColor = Color(0xFFF5F5F5)
    ) { paddingValues ->
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
fun CourseStatsSection(courses: List<Course>) {
    val completedCount = courses.count { it.status == "Đã hoàn thành" }
    val inProgressCount = courses.count { it.status == "Đang học" }
    val notStartedCount = courses.count { it.status == "Chưa học" }

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
            title = "Chờ đăng ký",
            count = notStartedCount,
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
fun CoursesGrid(courses: List<Course>) {
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
fun EnhancedCourseCard(course: Course) {
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
                        text = course.name,
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
                        "Đã hoàn thành" -> Color(0xFF4CAF50)
                        "Đang học" -> Color(0xFF2196F3)
                        else -> Color(0xFFFF9800)
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
                    text = "Giáo viên: ${course.teacher}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Progress bar và phần trăm
            if (course.progress > 0f) {
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