package com.example.ckc_englihoo.Screen

import com.example.ckc_englihoo.Data.TeacherClass

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.ckc_englihoo.R

data class Assignment(
    val id: String,
    val name: String,
    val maxScore: Double,
    val weight: Double, // Trọng số %
    val dueDate: String
)

data class StudentGrade(
    val studentId: String,
    val studentName: String,
    val studentCode: String,
    val grades: MutableMap<String, Double?> = mutableMapOf(), // assignmentId -> score
    val avatar: Int = R.drawable.student
) {
    fun getAverageGrade(assignments: List<Assignment>): Double {
        var totalWeightedScore = 0.0
        var totalWeight = 0.0
        
        assignments.forEach { assignment ->
            grades[assignment.id]?.let { score ->
                val percentage = (score / assignment.maxScore) * 100
                totalWeightedScore += percentage * assignment.weight
                totalWeight += assignment.weight
            }
        }
        
        return if (totalWeight > 0) totalWeightedScore / totalWeight else 0.0
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GradesTeacher(
    teacherClass: TeacherClass,
    onBackClick: () -> Unit = {},
    onAddAssignmentClick: () -> Unit = {}
) {
    var showAddAssignmentDialog by remember { mutableStateOf(false) }
    var selectedStudent by remember { mutableStateOf<StudentGrade?>(null) }
    var selectedAssignment by remember { mutableStateOf<Assignment?>(null) }
    
    // Sample assignments
    val assignments = remember {
        mutableStateListOf(
            Assignment("1", "Bài kiểm tra giữa kỳ", 10.0, 30.0, "15/11/2024"),
            Assignment("2", "Bài tập nhóm", 10.0, 20.0, "20/11/2024"),
            Assignment("3", "Thuyết trình", 10.0, 25.0, "25/11/2024"),
            Assignment("4", "Bài kiểm tra cuối kỳ", 10.0, 25.0, "30/11/2024")
        )
    }
    
    // Sample student grades
    val studentGrades = remember {
        mutableStateListOf(
            StudentGrade("1", "Nguyễn Văn An", "0306221001").apply {
                grades["1"] = 8.5
                grades["2"] = 9.0
                grades["3"] = 7.5
                grades["4"] = null
            },
            StudentGrade("2", "Trần Thị Bình", "0306221002").apply {
                grades["1"] = 7.0
                grades["2"] = 8.5
                grades["3"] = 8.0
                grades["4"] = null
            },
            StudentGrade("3", "Lê Hoàng Cường", "0306221003").apply {
                grades["1"] = 9.0
                grades["2"] = 8.0
                grades["3"] = 9.5
                grades["4"] = null
            },
            StudentGrade("4", "Phạm Thị Dung", "0306221004").apply {
                grades["1"] = 6.5
                grades["2"] = 7.5
                grades["3"] = 7.0
                grades["4"] = null
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Quản lý điểm số",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(
                            text = teacherClass.name,
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 12.sp
                        )
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
                actions = {
                    IconButton(onClick = { showAddAssignmentDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Thêm bài tập",
                            tint = Color.White
                        )
                    }
                    IconButton(onClick = { /* TODO: Export grades */ }) {
                        Icon(
                            imageVector = Icons.Default.FileDownload,
                            contentDescription = "Xuất điểm",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = teacherClass.backgroundColor
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
        ) {
            // Statistics card
            GradeStatisticsCard(
                studentGrades = studentGrades,
                assignments = assignments,
                classColor = teacherClass.backgroundColor
            )
            
            // Grades table
            GradesTable(
                assignments = assignments,
                studentGrades = studentGrades,
                onGradeClick = { student, assignment ->
                    selectedStudent = student
                    selectedAssignment = assignment
                }
            )
        }
    }
    
    // Add assignment dialog
    if (showAddAssignmentDialog) {
        AddAssignmentDialog(
            onDismiss = { showAddAssignmentDialog = false },
            onConfirm = { assignment ->
                assignments.add(assignment)
                showAddAssignmentDialog = false
            }
        )
    }
    
    // Edit grade dialog
    if (selectedStudent != null && selectedAssignment != null) {
        EditGradeDialog(
            student = selectedStudent!!,
            assignment = selectedAssignment!!,
            currentGrade = selectedStudent!!.grades[selectedAssignment!!.id],
            onDismiss = {
                selectedStudent = null
                selectedAssignment = null
            },
            onSave = { newGrade ->
                selectedStudent!!.grades[selectedAssignment!!.id] = newGrade
                selectedStudent = null
                selectedAssignment = null
            }
        )
    }
}

@Composable
fun GradeStatisticsCard(
    studentGrades: List<StudentGrade>,
    assignments: List<Assignment>,
    classColor: Color
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Thống kê lớp học",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Average grade
                val averageGrade = studentGrades.map { it.getAverageGrade(assignments) }.average()
                StatisticItem(
                    title = "Điểm TB",
                    value = String.format("%.1f", averageGrade),
                    color = classColor
                )
                
                // Completed assignments
                val completedCount = assignments.count { assignment ->
                    studentGrades.all { it.grades[assignment.id] != null }
                }
                StatisticItem(
                    title = "Hoàn thành",
                    value = "$completedCount/${assignments.size}",
                    color = Color(0xFF4CAF50)
                )
                
                // Students count
                StatisticItem(
                    title = "Học sinh",
                    value = "${studentGrades.size}",
                    color = Color(0xFF2196F3)
                )
            }
        }
    }
}

@Composable
fun StatisticItem(
    title: String,
    value: String,
    color: Color
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = title,
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}

@Composable
fun GradesTable(
    assignments: List<Assignment>,
    studentGrades: List<StudentGrade>,
    onGradeClick: (StudentGrade, Assignment) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            // Table header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF5F5F5))
                    .padding(8.dp)
                    .horizontalScroll(rememberScrollState()),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Student name column
                Box(
                    modifier = Modifier.width(150.dp)
                ) {
                    Text(
                        text = "Học sinh",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }
                
                // Assignment columns
                assignments.forEach { assignment ->
                    Box(
                        modifier = Modifier.width(80.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = assignment.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "${assignment.weight.toInt()}%",
                                fontSize = 10.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
                
                // Average column
                Box(
                    modifier = Modifier.width(80.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Điểm TB",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
            
            Divider()
            
            // Table content
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(studentGrades) { student ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                            .horizontalScroll(rememberScrollState()),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Student info
                        Box(
                            modifier = Modifier.width(150.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Image(
                                    painter = painterResource(id = R.drawable.student),
                                    contentDescription = "Avatar",
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                )
                                
                                Spacer(modifier = Modifier.width(8.dp))
                                
                                Column {
                                    Text(
                                        text = student.studentName,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = student.studentCode,
                                        fontSize = 10.sp,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }
                        
                        // Grades
                        assignments.forEach { assignment ->
                            Box(
                                modifier = Modifier
                                    .width(80.dp)
                                    .clickable { onGradeClick(student, assignment) },
                                contentAlignment = Alignment.Center
                            ) {
                                val grade = student.grades[assignment.id]
                                if (grade != null) {
                                    Surface(
                                        color = getGradeColor(grade, assignment.maxScore),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.padding(4.dp)
                                    ) {
                                        Text(
                                            text = String.format("%.1f", grade),
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color.White,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                } else {
                                    Surface(
                                        color = Color.Gray.copy(alpha = 0.3f),
                                        shape = RoundedCornerShape(8.dp),
                                        modifier = Modifier.padding(4.dp)
                                    ) {
                                        Text(
                                            text = "-",
                                            fontSize = 12.sp,
                                            color = Color.Gray,
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                        )
                                    }
                                }
                            }
                        }
                        
                        // Average grade
                        Box(
                            modifier = Modifier.width(80.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            val average = student.getAverageGrade(assignments)
                            if (average > 0) {
                                Surface(
                                    color = getGradeColor(average, 10.0),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.padding(4.dp)
                                ) {
                                    Text(
                                        text = String.format("%.1f", average),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            } else {
                                Text(
                                    text = "-",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                    
                    if (student != studentGrades.last()) {
                        Divider(color = Color.Gray.copy(alpha = 0.3f))
                    }
                }
            }
        }
    }
}

@Composable
fun AddAssignmentDialog(
    onDismiss: () -> Unit,
    onConfirm: (Assignment) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var maxScore by remember { mutableStateOf("10") }
    var weight by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf("") }
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "Thêm bài tập mới",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Tên bài tập") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = maxScore,
                    onValueChange = { maxScore = it },
                    label = { Text("Điểm tối đa") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = weight,
                    onValueChange = { weight = it },
                    label = { Text("Trọng số (%)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                OutlinedTextField(
                    value = dueDate,
                    onValueChange = { dueDate = it },
                    label = { Text("Hạn nộp (dd/MM/yyyy)") },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Hủy")
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Button(
                        onClick = {
                            if (name.isNotBlank() && maxScore.isNotBlank() && weight.isNotBlank()) {
                                val assignment = Assignment(
                                    id = System.currentTimeMillis().toString(),
                                    name = name,
                                    maxScore = maxScore.toDoubleOrNull() ?: 10.0,
                                    weight = weight.toDoubleOrNull() ?: 0.0,
                                    dueDate = dueDate.ifBlank { "Chưa xác định" }
                                )
                                onConfirm(assignment)
                            }
                        }
                    ) {
                        Text("Thêm")
                    }
                }
            }
        }
    }
}

@Composable
fun EditGradeDialog(
    student: StudentGrade,
    assignment: Assignment,
    currentGrade: Double?,
    onDismiss: () -> Unit,
    onSave: (Double?) -> Unit
) {
    var gradeText by remember { mutableStateOf(currentGrade?.toString() ?: "") }
    
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = "Chỉnh sửa điểm",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Học sinh: ${student.studentName}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                
                Text(
                    text = "Bài tập: ${assignment.name}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = gradeText,
                    onValueChange = { gradeText = it },
                    label = { Text("Điểm (0 - ${assignment.maxScore})") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Hủy")
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    TextButton(
                        onClick = { onSave(null) }
                    ) {
                        Text("Xóa điểm")
                    }
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Button(
                        onClick = {
                            val grade = gradeText.toDoubleOrNull()
                            if (grade != null && grade >= 0 && grade <= assignment.maxScore) {
                                onSave(grade)
                            }
                        }
                    ) {
                        Text("Lưu")
                    }
                }
            }
        }
    }
}

fun getGradeColor(grade: Double, maxScore: Double): Color {
    val percentage = (grade / maxScore) * 100
    return when {
        percentage >= 80 -> Color(0xFF4CAF50) // Green
        percentage >= 65 -> Color(0xFF2196F3) // Blue
        percentage >= 50 -> Color(0xFFFF9800) // Orange
        else -> Color(0xFFF44336) // Red
    }
}
