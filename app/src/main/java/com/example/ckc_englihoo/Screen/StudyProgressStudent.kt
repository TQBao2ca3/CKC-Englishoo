package com.example.ckc_englihoo.Screen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class StudyProgress(
    val courseCode: String, // Mã khóa: ON29_KH098, ON29, etc.
    val startDate: String, // Ngày khai giảng
    val className: String,
    val room: String,
    val schedule: String,
    val teacher: String,
    val note: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudyProgressStudent(
    onBackClick: () -> Unit = {}
) {
    val primaryColor = Color(0xFF0066B3)

    // Quá trình học hiện tại - chỉ có 1 lớp đang học hoặc để trống
    val currentStudyProgress = remember {
        mutableStateOf(
            StudyProgress(
                courseCode = "ON29_KH098",
                startDate = "02/06/2025",
                className = "A1-01",
                room = "A201",
                schedule = "T2,T4,T6: 18:30-20:30",
                teacher = "Mr. Nhâm Chí Bửu",
                note = "Đang học"
            )
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Spacer để cân bằng với nút back (48dp)
                        Spacer(modifier = Modifier.width(48.dp))

                        // Title căn giữa
                        Text(
                            "Quá Trình Học Tập",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            letterSpacing = 0.5.sp,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )

                        // Spacer để cân bằng bên phải (48dp - tương đương với icon)
                        Spacer(modifier = Modifier.width(80.dp))
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Quay lại",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primaryColor
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = androidx.compose.ui.graphics.Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFF8FAFC),
                            Color(0xFFFFFFFF)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                Spacer(modifier = Modifier.height(10.dp))

                // Bảng quá trình học với thiết kế chuyên nghiệp
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 20.dp), // Thêm padding bottom để đảm bảo không bị che
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 8.dp,
                        hoveredElevation = 12.dp
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    StudyProgressTable(studyProgress = currentStudyProgress.value)
                }
            }
        }
    }
}

@Composable
fun StudyProgressTable(studyProgress: StudyProgress?) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Header với gradient background
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = androidx.compose.ui.graphics.Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFF0066B3),
                            Color(0xFF4A90E2)
                        )
                    ),
                    shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                )
                .padding(20.dp)
        ) {
            Text(
                text = "Thông Tin Lớp Học",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        }

        // Content với padding đẹp
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            // Các hàng của bảng với thiết kế chuyên nghiệp
            TableRowItem(
                label = "STT",
                value = "1"
            )

            TableRowItem(
                label = "Khóa học",
                value = studyProgress?.courseCode ?: ""
            )

            TableRowItem(
                label = "Ngày khai giảng",
                value = studyProgress?.startDate ?: ""
            )

            TableRowItem(
                label = "Lớp",
                value = studyProgress?.className ?: ""
            )

            TableRowItem(
                label = "Phòng học",
                value = studyProgress?.room ?: ""
            )

            TableRowItem(
                label = "Lịch học",
                value = studyProgress?.schedule ?: ""
            )

            TableRowItem(
                label = "Giảng viên",
                value = studyProgress?.teacher ?: ""
            )

            TableRowItem(
                label = "Trạng thái",
                value = studyProgress?.note ?: "",
                isStatus = true
            )

            // Thêm spacer cuối để đảm bảo có thể cuộn hết
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun TableRowItem(
    label: String,
    value: String,
    isStatus: Boolean = false
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = Color(0xFFE5E7EB)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = androidx.compose.ui.graphics.Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFFFAFBFC),
                            Color.White
                        )
                    )
                )
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Label với màu đẹp
            Text(
                text = label,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF374151),
                modifier = Modifier.weight(1f)
            )

            // Value với styling đặc biệt
            if (value.isNotEmpty()) {
                if (isStatus) {
                    // Status chip cho trạng thái
                    val (backgroundColor, textColor) = when {
                        value.contains("Đang học") -> Pair(Color(0xFF10B981), Color.White)
                        value.contains("Chưa đăng ký") -> Pair(Color(0xFFF59E0B), Color.White)
                        value.contains("Sắp mở") -> Pair(Color(0xFF3B82F6), Color.White)
                        else -> Pair(Color(0xFF6B7280), Color.White)
                    }

                    Surface(
                        color = backgroundColor,
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.padding(start = 8.dp)
                    ) {
                        Text(
                            text = value,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = textColor,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                } else {
                    Text(
                        text = value,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF111827),
                        textAlign = TextAlign.End,
                        modifier = Modifier.weight(1f)
                    )
                }
            } else {
                // Empty state với style đẹp
                Text(
                    text = "Chưa có thông tin",
                    fontSize = 14.sp,
                    color = Color(0xFF9CA3AF),
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.End
                )
            }
        }
    }
}
