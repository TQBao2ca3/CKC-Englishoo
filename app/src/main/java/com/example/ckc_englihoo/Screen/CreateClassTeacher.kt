package com.example.ckc_englihoo.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ckc_englihoo.Data.TeacherClass
import kotlin.random.Random

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateClassTeacher(
    onBackClick: () -> Unit = {},
    onClassCreated: (TeacherClass) -> Unit = {}
) {
    var className by remember { mutableStateOf("") }
    var classDescription by remember { mutableStateOf("") }
    var academicYear by remember { mutableStateOf("2024-2025") }
    var subject by remember { mutableStateOf("") }
    var room by remember { mutableStateOf("") }
    var showCreateDialog by remember { mutableStateOf(false) }

    // Validation states
    var classNameError by remember { mutableStateOf("") }
    var isFormValid by remember { mutableStateOf(false) }

    // Update form validation
    LaunchedEffect(className, classDescription) {
        classNameError = when {
            className.isBlank() -> "Tên lớp học không được để trống"
            className.length < 3 -> "Tên lớp học phải có ít nhất 3 ký tự"
            else -> ""
        }
        isFormValid = className.isNotBlank() && classNameError.isEmpty()
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
                            "Tạo Lớp Học Mới",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            letterSpacing = 0.5.sp,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )

                        // Spacer để tạo khoảng cách bên phải (đẩy title qua trái)
                        Spacer(modifier = Modifier.width(80.dp))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Quay lại",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1976D2) // Keep original blue
                )
            )

        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFE3F2FD), // Light blue
                            Color(0xFFF8FDFF)  // Very light blue/white
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFFFFE) // Pure white
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.verticalGradient(
                                    colors = listOf(
                                        Color(0xFF2196F3), // Light blue
                                        Color(0xFF1976D2)  // Original blue
                                    )
                                )
                            )
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.School,
                                contentDescription = "Create Class",
                                tint = Color.White,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Tạo Lớp Học Của Bạn",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Điền thông tin để tạo lớp học của bạn",
                                fontSize = 14.sp,
                                color = Color(0xFFE3F2FD),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    }
                }

                // Form Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFFFFE) // Pure white
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        // Class Name (Required)
                        Column {
                            Text(
                                text = "Tên lớp học *",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = className,
                                onValueChange = { className = it },
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = {
                                    Text(
                                        "Ví dụ: Lập trình Android",
                                        color = Color.Gray
                                    )
                                },
                                isError = classNameError.isNotEmpty(),
                                supportingText = if (classNameError.isNotEmpty()) {
                                    { Text(classNameError, color = Color(0xFFD32F2F)) }
                                } else null,
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Class,
                                        contentDescription = "Class Name",
                                        tint = Color(0xFF2196F3) // Xanh dương sáng
                                    )
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF2196F3),
                                    unfocusedBorderColor = Color(0xFFBBDEFB),
                                    cursorColor = Color(0xFF2196F3),
                                    focusedTextColor = Color(0xFF0D47A1),
                                    unfocusedTextColor = Color(0xFF1976D2)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }

                        // Class Description
                        Column {
                            Text(
                                text = "Mô tả lớp học",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = classDescription,
                                onValueChange = { classDescription = it },
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = {
                                    Text(
                                        "Mô tả ngắn về lớp học",
                                        color = Color.Gray
                                    )
                                },
                                minLines = 3,
                                maxLines = 5,
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Description,
                                        contentDescription = "Description",
                                        tint = Color(0xFF2196F3)
                                    )
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF2196F3),
                                    unfocusedBorderColor = Color(0xFFBBDEFB),
                                    cursorColor = Color(0xFF2196F3),
                                    focusedTextColor = Color(0xFF0D47A1),
                                    unfocusedTextColor = Color(0xFF1976D2)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }

                        // Subject
                        Column {
                            Text(
                                text = "Môn học",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = subject,
                                onValueChange = { subject = it },
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = {
                                    Text(
                                        "Ví dụ: Tin học, Toán học",
                                        color = Color.Gray
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.MenuBook,
                                        contentDescription = "Subject",
                                        tint = Color(0xFF2196F3)
                                    )
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF2196F3),
                                    unfocusedBorderColor = Color(0xFFBBDEFB),
                                    cursorColor = Color(0xFF2196F3),
                                    focusedTextColor = Color(0xFF0D47A1),
                                    unfocusedTextColor = Color(0xFF1976D2)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }

                        // Academic Year
                        Column {
                            Text(
                                text = "Năm học",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = academicYear,
                                onValueChange = { academicYear = it },
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = {
                                    Text(
                                        "2024-2025",
                                        color = Color.Gray
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.CalendarToday,
                                        contentDescription = "Academic Year",
                                        tint = Color(0xFF2196F3)
                                    )
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF2196F3),
                                    unfocusedBorderColor = Color(0xFFBBDEFB),
                                    cursorColor = Color(0xFF2196F3),
                                    focusedTextColor = Color(0xFF0D47A1),
                                    unfocusedTextColor = Color(0xFF1976D2)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }

                        // Room
                        Column {
                            Text(
                                text = "Phòng học",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(
                                value = room,
                                onValueChange = { room = it },
                                modifier = Modifier.fillMaxWidth(),
                                placeholder = {
                                    Text(
                                        "Ví dụ: A201, B105",
                                        color = Color.Gray
                                    )
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Room,
                                        contentDescription = "Room",
                                        tint = Color(0xFF2196F3)
                                    )
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF2196F3),
                                    unfocusedBorderColor = Color(0xFFBBDEFB),
                                    cursorColor = Color(0xFF2196F3),
                                    focusedTextColor = Color(0xFF0D47A1),
                                    unfocusedTextColor = Color(0xFF1976D2)
                                ),
                                shape = RoundedCornerShape(12.dp)
                            )
                        }
                    }
                }

                // Create Button
                Button(
                    onClick = { showCreateDialog = true },
                    enabled = isFormValid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2196F3),
                        disabledContainerColor = Color(0xFFBDBDBD),
                        contentColor = Color.White,
                        disabledContentColor = Color(0xFF9E9E9E)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = if (isFormValid) 8.dp else 2.dp
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Create",
                        modifier = Modifier.size(20.dp),
                        tint = Color.White
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Tạo Lớp Học",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }

    // Create Confirmation Dialog
    if (showCreateDialog) {
        CreateClassConfirmDialog(
            className = className,
            classDescription = classDescription,
            academicYear = academicYear,
            subject = subject,
            room = room,
            onConfirm = {
                // Create new class with random color and ID
                val newClass = TeacherClass(
                    id = generateClassId(className),
                    name = className,
                    description = if (classDescription.isNotBlank()) classDescription else "Không có mô tả",
                    academicYear = academicYear,
                    studentCount = 0,
                    backgroundColor = getRandomClassColor()
                )
                onClassCreated(newClass)
                showCreateDialog = false
            },
            onDismiss = { showCreateDialog = false }
        )
    }
}

@Composable
fun CreateClassConfirmDialog(
    className: String,
    classDescription: String,
    academicYear: String,
    subject: String,
    room: String,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = "Create Class",
                    tint = Color(0xFF2196F3),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Xác nhận tạo lớp học",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1976D2)
                )
            }
        },
        text = {
            Column {
                Text(
                    text = "Bạn có chắc chắn muốn tạo lớp học với thông tin sau không?",
                    fontSize = 14.sp,
                    color = Color(0xFF424242)
                )
                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFE3F2FD) // Light blue
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Thông tin lớp học:",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        ClassInfoRow("Tên lớp:", className)
                        if (classDescription.isNotBlank()) {
                            ClassInfoRow("Mô tả:", classDescription)
                        }
                        ClassInfoRow("Năm học:", academicYear)
                        if (subject.isNotBlank()) {
                            ClassInfoRow("Môn học:", subject)
                        }
                        if (room.isNotBlank()) {
                            ClassInfoRow("Phòng học:", room)
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2196F3),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Confirm",
                    modifier = Modifier.size(16.dp),
                    tint = Color.White
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Tạo lớp",
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
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "Hủy",
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2196F3)
                )
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
private fun ClassInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF64B5F6),
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            fontSize = 12.sp,
            color = Color(0xFF1976D2),
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(2f)
        )
    }
}

// Helper functions
private fun generateClassId(className: String): String {
    val prefix = className.take(3).uppercase().replace(" ", "")
    val randomSuffix = Random.nextInt(100, 999)
    return "${prefix}_${randomSuffix}"
}

private fun getRandomClassColor(): Color {
    val colors = listOf(
        Color(0xFF81C784), // Light Green
        Color(0xFF64B5F6), // Light Blue
        Color(0xFFFFB74D), // Orange
        Color(0xFFBA68C8), // Purple
        Color(0xFF4FC3F7), // Cyan
        Color(0xFFA1C181), // Sage Green
        Color(0xFF90CAF9), // Blue
        Color(0xFFFFCC02), // Amber
        Color(0xFFCE93D8), // Light Purple
        Color(0xFF80CBC4)  // Teal
    )
    return colors.random()
}