package com.example.ckc_englihoo.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.navigation.NavController
import com.example.ckc_englihoo.API.AppViewModel
import com.example.ckc_englihoo.DataClass.*
import com.example.ckc_englihoo.R

// Data model cho course registration options
data class CourseRegistrationOption(
    val level: String,
    val schedule: String,
    val format: String,
    val availableCourses: List<Course>,
    val isSelected: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseRegistrationScreen(
    navController: NavController,
    viewModel: AppViewModel
) {
    // Collect states from ViewModel
    val courses by viewModel.courses.collectAsState()
    val courseEnrollments by viewModel.courseEnrollments.collectAsState()
    val currentStudent by viewModel.currentStudent.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var registrationOptions by remember { mutableStateOf<List<CourseRegistrationOption>>(emptyList()) }

    // Load courses when screen loads
    LaunchedEffect(Unit) {
        viewModel.loadAllCourses()
        currentStudent?.let { student ->
            viewModel.loadStudentEnrollments(student.student_id)
        }
    }

    // Process available registration options
    LaunchedEffect(courses, courseEnrollments) {
        registrationOptions = processRegistrationOptions(courses, courseEnrollments)
    }

    val selectedOption = registrationOptions.find { it.isSelected }

    Scaffold(
        topBar = { 
            AppBar { navController.popBackStack() }
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .background(Color.White)
        ) {
            SchoolHeader()
            
            if (isLoading) {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else if (registrationOptions.isNotEmpty()) {
                CourseRegistrationTable(registrationOptions) { chosen ->
                    registrationOptions = registrationOptions.map { 
                        it.copy(isSelected = it == chosen) 
                    }
                }
                RegisterSection(selectedOption) { 
                    showDialog = true 
                }
            } else {
                RegistrationClosed()
            }
        }
        
        if (showDialog && selectedOption != null) {
            ConfirmDialog(
                option = selectedOption,
                onConfirm = { 
                    // Handle registration
                    currentStudent?.let { student ->
                        registerStudentToCourses(viewModel, student, selectedOption)
                    }
                    showDialog = false
                    navController.popBackStack()
                }
            ) {
                showDialog = false
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AppBar(onBack: () -> Unit) {
    TopAppBar(
        title = { 
            Text(
                "Đăng Ký Khóa Học", 
                Modifier.fillMaxWidth(), 
                textAlign = TextAlign.Center, 
                color = Color.White, 
                fontWeight = FontWeight.Bold, 
                fontSize = 20.sp
            ) 
        },
        navigationIcon = {
            IconButton(
                onClick = onBack, 
                Modifier.size(48.dp).clip(CircleShape)
            ) {
                Icon(
                    Icons.AutoMirrored.Filled.ArrowBack, 
                    "Quay lại", 
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF0066B3)
        )
    )
}

@Composable
private fun SchoolHeader() {
    Card(
        Modifier.fillMaxWidth().padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1))
    ) {
        Row(
            Modifier.padding(16.dp), 
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(R.drawable.logocaothang),
                "Logo", 
                Modifier.size(48.dp).padding(end = 12.dp)
            )
            Column {  
                Text(
                    "TRƯỜNG CĐ KỸ THUẬT CAO THẮNG", 
                    fontWeight = FontWeight.Bold, 
                    fontSize = 14.sp, 
                    color = Color(0xFF0066CC)
                )
                Text(
                    "TRUNG TÂM NGOẠI NGỮ", 
                    fontWeight = FontWeight.ExtraBold, 
                    fontSize = 20.sp, 
                    color = Color.Red
                )
            }
        }
    }
}

@Composable
private fun CourseRegistrationTable(
    options: List<CourseRegistrationOption>, 
    onSelect: (CourseRegistrationOption) -> Unit
) {
    Column(Modifier.fillMaxWidth().padding(16.dp)) {
        InfoTitle("Thông tin các khóa học")
        Text(
            "Lịch Khai Giảng:", 
            fontWeight = FontWeight.Bold, 
            fontSize = 18.sp, 
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Card(
            Modifier.fillMaxWidth(), 
            shape = RoundedCornerShape(8.dp), 
            elevation = CardDefaults.cardElevation(1.dp), 
            colors = CardDefaults.cardColors(containerColor = Color(0xFFBBDEFB))
        ) {
            Row(Modifier.horizontalScroll(rememberScrollState()).padding(8.dp)) {
                Column {
                    TableRow() // Header row
                    options.forEach { option ->
                        TableRow(option, onSelect)
                    }
                }
            }
        }
    }
}

@Composable
private fun InfoTitle(text: String) {
    Row(
        Modifier.fillMaxWidth().padding(bottom = 16.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.info),
            contentDescription = "info",
            modifier = Modifier.size(28.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text = text,
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Blue
        )
    }
}

@Composable
private fun TableRow(
    option: CourseRegistrationOption? = null, 
    onSelect: (CourseRegistrationOption) -> Unit = {}
) {
    val isHeader = option == null
    val bg = if (isHeader) Color(0xFF0066B3) 
             else if (option?.isSelected == true) Color(0xFFFFEBEE) 
             else Color.White
    val border = if (option?.isSelected == true) 2.dp else 1.dp
    val borderColor = if (option?.isSelected == true) Color(0xFFE57373) else Color(0xFFE0E0E0)
    
    Row(Modifier.background(bg).border(border, borderColor)) {
        listOf(
            option?.level ?: "Trình độ",
            option?.schedule ?: "Lịch học",
            option?.format ?: "Hình thức",
            if (option != null) "${option.availableCourses.size} khóa" else "Số khóa"
        ).forEachIndexed { i, text ->
            val w = listOf(120.dp, 200.dp, 140.dp, 100.dp)[i]
            Box(
                Modifier
                    .width(w)
                    .height(56.dp)
                    .background(bg)
                    .border(0.5.dp, Color.White.takeIf { isHeader } ?: Color(0xFFE0E0E0))
                    .padding(8.dp)
            ) {
                if (i == 0 && !isHeader && option != null) {
                    Row(
                        Modifier.fillMaxSize(), 
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            option.isSelected, 
                            onClick = { onSelect(option) }, 
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text(
                            text, 
                            fontWeight = if (option.isSelected) FontWeight.Medium else FontWeight.Normal,
                            fontSize = 13.sp
                        )
                    }
                } else {
                    Text(
                        text, 
                        fontSize = 13.sp, 
                        fontWeight = if (option?.isSelected == true) FontWeight.Medium else FontWeight.Normal,
                        color = if (isHeader) Color.White else Color.Black
                    )
                }
            }
        }
    }
}

@Composable
private fun RegisterSection(
    selected: CourseRegistrationOption?,
    onClick: () -> Unit
) {
    Column(Modifier.fillMaxWidth().padding(16.dp)) {
        Card(
            Modifier.fillMaxWidth().padding(bottom = 16.dp),
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(2.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (selected != null) Color(0xFFE3F2FD) else Color(0xFFF5F5F5)
            )
        ) {
            Box(Modifier.padding(16.dp)) {
                if (selected != null) {
                    Column {
                        Text("Khóa học đã chọn:", fontWeight = FontWeight.Bold)
                        listOf(
                            "Trình độ: ${selected.level}",
                            "Lịch học: ${selected.schedule}",
                            "Hình thức: ${selected.format}",
                            "Số khóa có sẵn: ${selected.availableCourses.size}"
                        ).forEach {
                            Text(it, fontSize = 13.sp)
                        }
                    }
                } else {
                    Text(
                        "Vui lòng chọn khóa học",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
        Button(
            onClick = onClick,
            enabled = selected != null,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (selected != null) Color(0xFF2196F3) else Color.Gray
            )
        ) {
            Text(
                "Đăng Ký Khóa Học",
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }
    }
}

@Composable
private fun ConfirmDialog(
    option: CourseRegistrationOption,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                "Xác nhận đăng ký",
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2196F3)
            )
        },
        text = {
            Column {
                Text(
                    "Bạn có chắc chắn muốn đăng ký khóa học sau không?",
                    fontSize = 14.sp
                )
                Spacer(Modifier.height(12.dp))
                Card(
                    shape = RoundedCornerShape(8.dp),
                    elevation = CardDefaults.cardElevation(2.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE3F2FD))
                ) {
                    Column(Modifier.fillMaxWidth().padding(16.dp)) {
                        Text(
                            "Thông tin khóa học:",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        listOf(
                            "• Trình độ: ${option.level}",
                            "• Lịch học: ${option.schedule}",
                            "• Hình thức: ${option.format}",
                            "• Số khóa: ${option.availableCourses.size}"
                        ).forEach {
                            Text(it, fontSize = 12.sp)
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Xác nhận")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy")
            }
        }
    )
}

@Composable
private fun RegistrationClosed() {
    Column(
        Modifier.fillMaxSize().padding(18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            Modifier.fillMaxWidth().padding(16.dp),
            shape = RoundedCornerShape(12.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
        ) {
            Column(
                Modifier.fillMaxWidth().padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Simple title without InfoTitle to avoid @Composable issues
                Text(
                    text = "Thông báo",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Blue
                )
                Spacer(Modifier.height(20.dp))
                Text(
                    text = "Hiện tại không có khóa học nào mở đăng ký",
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Vui lòng đợi thông báo khóa học tiếp theo.",
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp
                )
            }
        }
    }
}

// Helper functions
private fun processRegistrationOptions(
    courses: List<Course>,
    enrollments: List<CourseEnrollment>
): List<CourseRegistrationOption> {
    // Filter courses that are open for registration
    val openCourses = courses.filter { it.status == "Đang mở lớp" }

    // Get enrolled levels (student shouldn't register for same level)
    val enrolledLevels = enrollments
        .filter { it.status >= 2 } // Only active/completed enrollments
        .mapNotNull { enrollment ->
            courses.find { it.course_id == enrollment.assigned_course_id }?.level
        }
        .toSet()

    // Group courses by level, schedule, format
    val groupedOptions = openCourses
        .filter { !enrolledLevels.contains(it.level) } // Exclude enrolled levels
        .groupBy { course ->
            val description = course.description
            val level = course.level
            val schedule = extractSchedule(description)
            val format = extractFormat(description)
            Triple(level, schedule, format)
        }
        .map { (key, coursesInGroup) ->
            CourseRegistrationOption(
                level = key.first,
                schedule = key.second,
                format = key.third,
                availableCourses = coursesInGroup
            )
        }

    return groupedOptions
}

private fun extractSchedule(description: String): String {
    // Extract schedule from description
    // Example: "Lịch học: Thứ 2-4-6 buổi sáng (7h30-9h30)"
    val scheduleRegex = "Lịch học:\\s*([^.]+)".toRegex()
    return scheduleRegex.find(description)?.groupValues?.get(1)?.trim() ?: "Chưa xác định"
}

private fun extractFormat(description: String): String {
    // Extract format from description
    // Example: "Hình thức: Học trực tiếp tại lớp"
    val formatRegex = "Hình thức:\\s*([^.]+)".toRegex()
    return formatRegex.find(description)?.groupValues?.get(1)?.trim() ?: "Chưa xác định"
}

private fun registerStudentToCourses(
    viewModel: AppViewModel,
    student: Student,
    option: CourseRegistrationOption
) {
    // Register student to one of the available courses (distribute evenly)
    val targetCourse = option.availableCourses.firstOrNull()
    if (targetCourse != null) {
        val enrollment = CourseEnrollment(
            enrollment_id = 0, // Will be set by backend
            student_id = student.student_id,
            assigned_course_id = targetCourse.course_id,
            registration_date = "",
            status = 1, // Pending confirmation
            created_at = "",
            updated_at = ""
        )
        viewModel.enrollStudentInCourse(enrollment)
    }
}
