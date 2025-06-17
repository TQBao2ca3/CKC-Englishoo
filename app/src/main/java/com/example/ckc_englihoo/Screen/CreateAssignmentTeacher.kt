package com.example.ckc_englihoo.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ckc_englihoo.Data.ClassRepository
import com.example.ckc_englihoo.Data.ClassPost
import com.example.ckc_englihoo.R
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAssignmentTeacher(
    navController: NavController,
    assignmentType: String = "assignment",
    classId: String = ""
) {
    val title = when (assignmentType) {
        "assignment" -> "Tiêu đề bài tập (bắt buộc)"
        "quiz" -> "Tiêu đề bài kiểm tra (bắt buộc)"
        "question" -> "Tiêu đề câu hỏi (bắt buộc)"
        "material" -> "Tiêu đề tài liệu (bắt buộc)"
        else -> "Tiêu đề bài tập (bắt buộc)"
    }
    
    var assignmentTitle by remember { mutableStateOf("") }
    var selectedAudience by remember { mutableStateOf("Demo") }
    var points by remember { mutableStateOf("100 điểm") }
    var dueDate by remember { mutableStateOf("Đặt ngày đến hạn") }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = Color.Black
                        )
                    }
                },
                actions = {
                    Button(
                        onClick = {
                            if (assignmentTitle.isNotBlank()) {
                                // Create assignment post
                                val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
                                val assignmentContent = when (assignmentType) {
                                    "assignment" -> "📝 Bài tập: $assignmentTitle"
                                    "quiz" -> "📊 Bài kiểm tra: $assignmentTitle"
                                    "question" -> "❓ Câu hỏi: $assignmentTitle"
                                    "material" -> "📚 Tài liệu: $assignmentTitle"
                                    else -> "📝 Bài tập: $assignmentTitle"
                                }

                                val newPost = ClassPost(
                                    id = System.currentTimeMillis().toString(),
                                    authorName = "Mr. Nhâm Chí Bửu", // TODO: Get from MyProfileTeacher
                                    authorAvatar = R.drawable.teacher,
                                    content = assignmentContent,
                                    timestamp = currentTime,
                                    comments = emptyList(),
                                    attachments = emptyList()
                                )

                                // Save to repository
                                ClassRepository.addPostToClass(classId, newPost)

                                // Navigate back
                                navController.popBackStack()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2196F3),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier.padding(end = 8.dp)
                    ) {
                        Text(
                            text = "Giao",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 16.dp)
            ) {
                AudienceChip(
                    text = "Demo",
                    isSelected = selectedAudience == "Demo",
                    onClick = { selectedAudience = "Demo" }
                )
                AudienceChip(
                    text = "Tất cả học viên",
                    isSelected = selectedAudience == "Tất cả học viên",
                    onClick = { selectedAudience = "Tất cả học viên" }
                )
            }
            
            OutlinedTextField(
                value = assignmentTitle,
                onValueChange = { assignmentTitle = it },
                placeholder = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Menu,
                            contentDescription = "Menu",
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Mô tả",
                            color = Color.Gray
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFF2196F3),
                    unfocusedBorderColor = Color.Gray,
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black
                ),
                shape = RoundedCornerShape(8.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable { }
                    .padding(vertical = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AttachFile,
                    contentDescription = "Attach",
                    tint = Color(0xFF2196F3),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Thêm tệp đính kèm",
                    color = Color(0xFF2196F3),
                    fontSize = 16.sp
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Assessment,
                    contentDescription = "Points",
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                
                Box(
                    modifier = Modifier
                        .background(
                            color = Color(0xFFF5F5F5),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = points,
                            color = Color.Black,
                            fontSize = 14.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Remove",
                            tint = Color.Gray,
                            modifier = Modifier
                                .size(16.dp)
                                .clickable { }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable { }
                    .padding(vertical = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Date",
                    tint = Color(0xFF2196F3),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = dueDate,
                    color = Color(0xFF2196F3),
                    fontSize = 16.sp
                )
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable { }
                    .padding(vertical = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Topic,
                    contentDescription = "Topic",
                    tint = Color(0xFF2196F3),
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Thêm chủ đề",
                    color = Color(0xFF2196F3),
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
fun AudienceChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clickable { onClick() }
            .background(
                color = if (isSelected) Color(0xFFE3F2FD) else Color.Transparent,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.dp,
                color = if (isSelected) Color(0xFF2196F3) else Color.Gray,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Person",
                tint = if (isSelected) Color(0xFF2196F3) else Color.Gray,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                color = if (isSelected) Color(0xFF2196F3) else Color.Gray,
                fontSize = 14.sp
            )
        }
    }
}
