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
fun CreateAnnouncementTeacher(
    navController: NavController,
    classId: String
) {
    var announcementText by remember { mutableStateOf("") }

    // Get class info and teacher info
    val teacherClass = remember { ClassRepository.getClassById(classId) }
    val className = teacherClass?.name ?: "Demo2"
    var selectedAudience by remember { mutableStateOf(className) }
    
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
                            if (announcementText.isNotBlank()) {
                                // Create new post
                                val currentTime = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date())
                                val newPost = ClassPost(
                                    id = System.currentTimeMillis().toString(),
                                    authorName = "Mr. Nhâm Chí Bửu", // TODO: Get from MyProfileTeacher
                                    authorAvatar = R.drawable.teacher,
                                    content = announcementText,
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
                            text = "Tạo",
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
            // Audience selection
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Person",
                    tint = Color.Gray,
                    modifier = Modifier.size(20.dp)
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                AudienceChipAnnouncement(
                    text = className,
                    isSelected = selectedAudience == className,
                    onClick = { selectedAudience = className }
                )
                AudienceChipAnnouncement(
                    text = "Tất cả học viên",
                    isSelected = selectedAudience == "Tất cả học viên",
                    onClick = { selectedAudience = "Tất cả học viên" }
                )
            }
            
            // Announcement text field
            OutlinedTextField(
                value = announcementText,
                onValueChange = { announcementText = it },
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
                            text = "Thông báo tin gì đó cho lớp",
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
            
            // Add attachments
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
        }
    }
}

@Composable
fun AudienceChipAnnouncement(
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
        Text(
            text = text,
            color = if (isSelected) Color(0xFF2196F3) else Color.Gray,
            fontSize = 14.sp
        )
    }
}
