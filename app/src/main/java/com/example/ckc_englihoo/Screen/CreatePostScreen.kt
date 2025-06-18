package com.example.ckc_englihoo.Screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostScreen(
    navController: NavController,
    courseId: Int = 1,
    teacherId: Int = 1,
    classTitle: String = "Tiếng Anh 101",
    onBackClick: () -> Unit = {},
    onPostCreated: (String) -> Unit = {} // Callback khi tạo post thành công
) {
    // State cho input text
    var postContent by remember { mutableStateOf("") }
    var isPosting by remember { mutableStateOf(false) }
    
    // Sample teacher data - In production, use ViewModel
    val currentTeacher = "Ngô Kim Phụng"
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "Tạo thông báo mới",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Quay lại"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            // Thông tin khóa học
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Đăng thông báo cho:",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = classTitle,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Thông tin teacher
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Avatar giáo viên",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = currentTeacher,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "Giáo viên",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Text input cho nội dung post
            OutlinedTextField(
                value = postContent,
                onValueChange = { postContent = it },
                placeholder = { 
                    Text("Nhập nội dung thông báo cho lớp học của bạn...") 
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                minLines = 6,
                maxLines = 10,
                enabled = !isPosting
            )
            

            Spacer(modifier = Modifier.weight(1f))
            
            // Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Cancel button
                Button(
                    onClick = onBackClick,
                    modifier = Modifier.weight(1f),
                    enabled = !isPosting
                ) {
                    Text("Hủy")
                }
                
                // Post button
                Button(
                    onClick = {
                        if (postContent.isNotBlank()) {
                            isPosting = true
                            // TODO: Thêm logic để lưu post vào database
                            // Hiện tại chỉ gọi callback
                            onPostCreated(postContent)
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = postContent.isNotBlank() && !isPosting
                ) {
                    Icon(
                        imageVector = Icons.Default.Send,
                        contentDescription = "Đăng",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(if (isPosting) "Đang đăng..." else "Đăng thông báo")
                }
            }
        }
    }
}