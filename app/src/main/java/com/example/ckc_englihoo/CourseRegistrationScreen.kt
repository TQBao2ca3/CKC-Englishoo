package com.example.ckc_englihoo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Data classes cho khóa học
data class CourseSchedule(
    val level: String, // Trình độ lớp
    val schedule: String, // Buổi học
    val courseCode: String, // Khóa
    val notes: String, // Ghi chú
    val type: String, // "Học online" hoặc "Học tại trường"
    val isSelected: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseRegistrationScreen(
    onBackClick: () -> Unit = {}
) {
    val primaryColor = Color(0xFF0066B3)

    // State cho popup xác nhận
    var showConfirmDialog by remember { mutableStateOf(false) }

    // State để kiểm tra xem có khóa học đang mở đăng ký không
    var isRegistrationOpen by remember { mutableStateOf(true) } // Đặt false để hiển thị thông báo

    // Dữ liệu mẫu cho lịch khai giảng - Ban đầu không có gì được chọn
    var courseList by remember {
        mutableStateOf(
            listOf(
                CourseSchedule("LT2/6", "Tối - Thứ 2,4,6", "ON21", "Học online", "Học online"),
                CourseSchedule("LT2/6", "Tối - Thứ 3,5,7", "ON21", "Học online", "Học online"),
                CourseSchedule("A1", "Tối - Thứ 2,4,6", "21_KH090", "Khai giảng 28/10/2023", "Học tại trường"),
                CourseSchedule("A1", "Tối - Thứ 3,5,7", "21_KH090", "Khai giảng 28/10/2023", "Học tại trường"),
                CourseSchedule("A1", "Sáng - Thứ 7-CN", "21_KH090", "Khai giảng 28/10/2023", "Học tại trường"),
                CourseSchedule("A2", "Tối - Thứ 2,4,6", "21_KH090", "Khai giảng 28/10/2023", "Học tại trường"),
                CourseSchedule("A2", "Tối - Thứ 3,5,7", "21_KH090", "Khai giảng 28/10/2023", "Học tại trường"),
                CourseSchedule("A2", "Sáng - Thứ 7-CN", "21_KH090", "Khai giảng 28/10/2023", "Học tại trường"),
                CourseSchedule("A3", "Chiều - Thứ 7-CN", "21_KH090", "Khai giảng 28/10/2023", "Học tại trường"),
                CourseSchedule("LT2/6", "Chiều - Thứ 7-CN", "21_KH090", "Khai giảng 28/10/2023", "Học tại trường"),
                CourseSchedule("LT2/6", "Sáng - Thứ 2,4,6", "21_KH090", "Khai giảng 28/10/2023", "Học tại trường"),
                CourseSchedule("LT2/6", "Sáng - Thứ 3,4,6", "21_KH090", "Khai giảng 28/10/2023", "Học tại trường"),
                CourseSchedule("LT2/6", "Chiều - Thứ 2,4,6", "21_KH090", "Khai giảng 28/10/2023", "Học tại trường") // Loại bỏ true
            )
        )
    }

    // Tìm khóa học được chọn
    val selectedCourse = courseList.find { it.isSelected }

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
                            "Đăng Ký Khoá Học",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
                        
                        // Spacer để cân bằng bên phải (80dp)
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .background(Color.White)
        ) {
            // Header trường học (lấy từ LoginForm)
            SchoolHeader()

            // Kiểm tra xem có khóa học đang mở đăng ký không
            if (isRegistrationOpen) {
                // Lịch khai giảng
                CourseScheduleSection(
                    courseList = courseList,
                    onCourseSelected = { selectedCourse ->
                        courseList = courseList.map { course ->
                            course.copy(isSelected = course == selectedCourse)
                        }
                    }
                )

                // Button đăng ký khóa học
                RegistrationButton(
                    isEnabled = selectedCourse != null,
                    selectedCourse = selectedCourse,
                    onRegisterClick = { showConfirmDialog = true }
                )
            } else {
                // Hiển thị thông báo khi hết hạn đăng ký
                RegistrationClosedNotification()
            }
        }
    }

    // Popup xác nhận đăng ký
    if (showConfirmDialog && selectedCourse != null) {
        RegistrationConfirmDialog(
            course = selectedCourse,
            onConfirm = {
                showConfirmDialog = false
                // TODO: Xử lý đăng ký khóa học
            },
            onDismiss = { showConfirmDialog = false }
        )
    }
}

@Composable
fun SchoolHeader() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Logo trường - sử dụng logo thật từ LoginForm
            Image(
                painter = painterResource(id = R.drawable.logocaothang),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(48.dp)
                    .padding(end = 12.dp)
            )

            // Thông tin trường
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "TRƯỜNG CĐ KỸ THUẬT CAO THẮNG",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0066CC)
                )
                Text(
                    text = "TRUNG TÂM NGOẠI NGỮ",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(0xFFFF0000),
                    letterSpacing = 0.5.sp
                )
            }
        }
    }
}

@Composable
fun CourseScheduleSection(
    courseList: List<CourseSchedule>,
    onCourseSelected: (CourseSchedule) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Title với icon và canh giữa
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icon bên trái
            Image(
                painter = painterResource(id = R.drawable.info),
                contentDescription = "Thông tin các khoa hoc",
                modifier = Modifier
                    .size(28.dp)
            )

            Spacer(modifier = Modifier.width(8.dp))

            // Title
            Text(
                text = "Thông tin các khoá học",
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Blue,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.width(20.dp))
        }
        Text(
            text = "Lịch Khai Giảng:",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        
        // Bảng với scroll ngang toàn bộ
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFBBDEFB)),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            shape = RoundedCornerShape(8.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0))
        ) {
            // Toàn bộ bảng cuộn ngang
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(8.dp)
            ) {
                Column {
                    // Header của bảng
                    CourseTableHeader()

                    // Danh sách khóa học
                    courseList.forEach { course ->
                        CourseTableRow(
                            course = course,
                            onSelected = { onCourseSelected(course) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CourseTableHeader() {
    Row(
        modifier = Modifier
            .background(Color(0xFF0066B3)),
        horizontalArrangement = Arrangement.spacedBy(0.dp) // Loại bỏ spacing để canh đều
    ) {
        // Cột 1: Chọn lớp
        TableHeaderCell(
            text = "Chọn lớp",
            width = 120.dp
        )

        // Cột 2: Buổi học
        TableHeaderCell(
            text = "Buổi học",
            width = 160.dp
        )

        // Cột 3: Khóa
        TableHeaderCell(
            text = "Khóa",
            width = 120.dp
        )

        // Cột 4: Ghi chú
        TableHeaderCell(
            text = "Ghi chú",
            width = 180.dp
        )
    }
}

@Composable
fun TableHeaderCell(
    text: String,
    width: Dp
) {
    Box(
        modifier = Modifier
            .width(width)
            .height(56.dp) // Tăng chiều cao để có không gian đều
            .background(Color(0xFF0066B3))
            .border(0.5.dp, Color.White) // Thêm border để phân biệt các cột
            .padding(8.dp),
        contentAlignment = Alignment.Center // Canh giữa cho header
    ) {
        Text(
            text = text,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            textAlign = TextAlign.Center // Canh giữa cho header
        )
    }
}

@Composable
fun CourseTableRow(
    course: CourseSchedule,
    onSelected: () -> Unit
) {
    val backgroundColor = if (course.isSelected) Color(0xFFFFEBEE) else Color.White // Màu đỏ nhạt
    val borderColor = if (course.isSelected) Color(0xFFE57373) else Color(0xFFE0E0E0) // Màu đỏ nhạt border

    Row(
        modifier = Modifier
            .background(backgroundColor)
            .border(
                width = if (course.isSelected) 2.dp else 1.dp,
                color = borderColor
            ),
        horizontalArrangement = Arrangement.spacedBy(0.dp), // Loại bỏ spacing để canh đều
        verticalAlignment = Alignment.CenterVertically // Canh giữa theo chiều dọc
    ) {
        // Cột 1: Chọn lớp (với RadioButton)
        TableDataCellWithRadio(
            text = course.level,
            width = 120.dp,
            isSelected = course.isSelected,
            onSelected = onSelected
        )

        // Cột 2: Buổi học
        TableDataCell(
            text = course.schedule,
            width = 160.dp,
            isSelected = course.isSelected
        )

        // Cột 3: Khóa
        TableDataCell(
            text = course.courseCode,
            width = 120.dp,
            isSelected = course.isSelected
        )

        // Cột 4: Ghi chú
        TableDataCell(
            text = course.notes,
            width = 180.dp,
            isSelected = course.isSelected
        )
    }
}

@Composable
fun TableDataCell(
    text: String,
    width: Dp,
    isSelected: Boolean = false
) {
    Box(
        modifier = Modifier
            .width(width)
            .height(56.dp) // Chiều cao cố định để canh đều với header
            .background(if (isSelected) Color(0xFFFFEBEE) else Color.White) // Màu đỏ nhạt
            .border(0.5.dp, Color(0xFFE0E0E0)) // Thêm border để phân biệt các cột
            .padding(8.dp),
        contentAlignment = Alignment.CenterStart // Canh trái
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.Black else Color.Black, // Giữ màu đen
            fontSize = 13.sp,
            textAlign = TextAlign.Start, // Canh trái
            fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
        )
    }
}

@Composable
fun TableDataCellWithRadio(
    text: String,
    width: Dp,
    isSelected: Boolean = false,
    onSelected: () -> Unit
) {
    Box(
        modifier = Modifier
            .width(width)
            .height(56.dp) // Chiều cao cố định để canh đều với header
            .background(if (isSelected) Color(0xFFFFEBEE) else Color.White) // Màu đỏ nhạt
            .border(0.5.dp, Color(0xFFE0E0E0)) // Thêm border để phân biệt các cột
            .padding(8.dp),
        contentAlignment = Alignment.CenterStart // Canh trái
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start // Canh trái
        ) {
            RadioButton(
                selected = isSelected,
                onClick = onSelected,
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color(0xFF2196F3), // Màu xanh cho RadioButton
                    unselectedColor = Color.Gray
                ),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = text,
                color = Color.Black, // Màu đen cho text
                fontSize = 13.sp,
                textAlign = TextAlign.Start, // Canh trái
                fontWeight = if (isSelected) FontWeight.Medium else FontWeight.Normal
            )
        }
    }
}

@Composable
fun RegistrationButton(
    isEnabled: Boolean,
    selectedCourse: CourseSchedule?,
    onRegisterClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Thông tin khóa học được chọn - chỉ hiển thị khi có lựa chọn
        if (selectedCourse != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Khóa học đã chọn:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black // Màu đen in đậm
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Lớp: ${selectedCourse.level}",
                        fontSize = 13.sp,
                        color = Color.Black
                    )
                    Text(
                        text = "Buổi học: ${selectedCourse.schedule}",
                        fontSize = 13.sp,
                        color = Color.Black
                    )
                    Text(
                        text = "Khóa: ${selectedCourse.courseCode}",
                        fontSize = 13.sp,
                        color = Color.Black
                    )
                    Text(
                        text = "Ghi chú: ${selectedCourse.notes}",
                        fontSize = 13.sp,
                        color = Color.Black
                    )
                }
            }
        } else {
            // Hiển thị thông báo khi chưa chọn khóa học
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Vui lòng chọn khóa học",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }

        // Button đăng ký - chỉ hoạt động khi đã chọn
        Button(
            onClick = onRegisterClick,
            enabled = isEnabled,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isEnabled) Color(0xFF2196F3) else Color.Gray,
                disabledContainerColor = Color.Gray
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = if (isEnabled) 6.dp else 0.dp,
                pressedElevation = 8.dp
            )
        ) {
            Text(
                text = if (isEnabled) "Đăng Ký Khóa Học" else "Đăng Ký Khóa Học",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
fun RegistrationConfirmDialog(
    course: CourseSchedule,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Xác nhận đăng ký",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2196F3)
            )
        },
        text = {
            Column {
                Text(
                    text = "Bạn có chắc chắn muốn đăng ký khóa học sau không?",
                    fontSize = 14.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp) // Padding đều 2 bên
                    ) {
                        // Title canh giữa
                        Text(
                            text = "Thông tin khóa học:",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black, // Màu đen in đậm
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center // Canh giữa
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("• Lớp: ${course.level}", fontSize = 12.sp)
                        Text("• Buổi học: ${course.schedule}", fontSize = 12.sp)
                        Text("• Khóa: ${course.courseCode}", fontSize = 12.sp)
                        Text("• Ghi chú: ${course.notes}", fontSize = 12.sp)
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2196F3)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Xác nhận",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color(0xFF2196F3)
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF2196F3)),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Hủy",
                    fontWeight = FontWeight.Bold
                )
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(12.dp)
    )
}

@Composable
fun RegistrationClosedNotification() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        // Card chứa thông báo
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Title với icon và canh giữa
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Icon bên trái
                    Image(
                        painter = painterResource(id = R.drawable.info),
                        contentDescription = "Thông bao",
                        modifier = Modifier
                            .size(28.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Title
                    Text(
                        text = "Thông báo",
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Blue,
                        textAlign = TextAlign.Center
                    )
                }
                    Spacer(modifier = Modifier.width(20.dp))

                // Nội dung thông báo
                Text(
                    text = "Đã hết hạn đăng ký học Anh văn",
                    fontSize = 16.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Sinh viên đợi thông báo Khóa tiếp theo.",
                    fontSize = 16.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}
