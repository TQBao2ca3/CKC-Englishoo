package com.example.ckc_englihoo.Screen

import com.example.ckc_englihoo.Data.TeacherClass

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ckc_englihoo.R

data class StudentDetail(
    val id: String,
    val name: String,
    val email: String,
    val studentCode: String,
    val joinDate: String,
    val lastActive: String,
    val avatar: Int = R.drawable.student,
    val isActive: Boolean = true
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentListTeacher(
    teacherClass: TeacherClass,
    onBackClick: () -> Unit = {},
    onStudentClick: (StudentDetail) -> Unit = {},
    onInviteStudentClick: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var showSearchBar by remember { mutableStateOf(false) }
    
    // Sample student data
    val students = remember {
        listOf(
            StudentDetail(
                id = "1",
                name = "Nguyễn Văn An",
                email = "0306221001@caothang.edu.vn",
                studentCode = "0306221001",
                joinDate = "15/09/2024",
                lastActive = "2 giờ trước",
                isActive = true
            ),
            StudentDetail(
                id = "2",
                name = "Trần Thị Bình",
                email = "0306221002@caothang.edu.vn",
                studentCode = "0306221002",
                joinDate = "15/09/2024",
                lastActive = "1 ngày trước",
                isActive = true
            ),
            StudentDetail(
                id = "3",
                name = "Lê Hoàng Cường",
                email = "0306221003@caothang.edu.vn",
                studentCode = "0306221003",
                joinDate = "16/09/2024",
                lastActive = "3 ngày trước",
                isActive = false
            ),
            StudentDetail(
                id = "4",
                name = "Phạm Thị Dung",
                email = "0306221004@caothang.edu.vn",
                studentCode = "0306221004",
                joinDate = "17/09/2024",
                lastActive = "5 giờ trước",
                isActive = true
            ),
            StudentDetail(
                id = "5",
                name = "Võ Minh Đức",
                email = "0306221005@caothang.edu.vn",
                studentCode = "0306221005",
                joinDate = "18/09/2024",
                lastActive = "1 giờ trước",
                isActive = true
            )
        )
    }
    
    // Filter students based on search query
    val filteredStudents = students.filter { student ->
        student.name.contains(searchQuery, ignoreCase = true) ||
        student.email.contains(searchQuery, ignoreCase = true) ||
        student.studentCode.contains(searchQuery, ignoreCase = true)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (showSearchBar) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Tìm kiếm học sinh...") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color.White,
                                unfocusedBorderColor = Color.White.copy(alpha = 0.7f),
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                cursorColor = Color.White
                            )
                        )
                    } else {
                        Column {
                            Text(
                                text = "Danh sách học sinh",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                            Text(
                                text = "${students.size} học sinh",
                                color = Color.White.copy(alpha = 0.8f),
                                fontSize = 12.sp
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (showSearchBar) {
                            showSearchBar = false
                            searchQuery = ""
                        } else {
                            onBackClick()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Quay lại",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    if (!showSearchBar) {
                        IconButton(onClick = { showSearchBar = true }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Tìm kiếm",
                                tint = Color.White
                            )
                        }
                    }
                    
                    IconButton(onClick = { /* TODO: More options */ }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Tùy chọn",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = teacherClass.backgroundColor
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onInviteStudentClick,
                containerColor = teacherClass.backgroundColor,
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Default.PersonAdd,
                    contentDescription = "Mời học sinh"
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
        ) {
            // Class info header
            ClassInfoHeader(teacherClass = teacherClass)
            
            // Student list
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filteredStudents) { student ->
                    StudentCard(
                        student = student,
                        onClick = { onStudentClick(student) }
                    )
                }
                
                if (filteredStudents.isEmpty() && searchQuery.isNotEmpty()) {
                    item {
                        EmptySearchResult(searchQuery = searchQuery)
                    }
                }
                
                // Bottom spacing for FAB
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}

@Composable
fun ClassInfoHeader(teacherClass: TeacherClass) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Class icon
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(teacherClass.backgroundColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = "Class",
                    tint = teacherClass.backgroundColor,
                    modifier = Modifier.size(24.dp)
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Class info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = teacherClass.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )
                Text(
                    text = teacherClass.description,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            
            // Student count badge
            Surface(
                color = teacherClass.backgroundColor.copy(alpha = 0.1f),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = "${teacherClass.studentCount}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = teacherClass.backgroundColor,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
fun StudentCard(
    student: StudentDetail,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Student avatar
            Box {
                Image(
                    painter = painterResource(id = R.drawable.student),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                )
                
                // Online status indicator
                if (student.isActive) {
                    Box(
                        modifier = Modifier
                            .size(14.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF4CAF50))
                            .align(Alignment.BottomEnd)
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Student info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = student.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF333333)
                )
                
                Text(
                    text = student.studentCode,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                
                Text(
                    text = "Hoạt động: ${student.lastActive}",
                    fontSize = 12.sp,
                    color = if (student.isActive) Color(0xFF4CAF50) else Color.Gray
                )
            }
            
            // Action button
            IconButton(onClick = { /* TODO: Student options */ }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "Tùy chọn",
                    tint = Color.Gray
                )
            }
        }
    }
}

@Composable
fun EmptySearchResult(searchQuery: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.SearchOff,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(48.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Không tìm thấy học sinh",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Không có học sinh nào phù hợp với \"$searchQuery\"",
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}
