package com.example.ckc_englihoo.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ckc_englihoo.Data.ClassPost
import com.example.ckc_englihoo.Data.AttachmentItem
import com.example.ckc_englihoo.Data.AttachmentType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditPostTeacher(
    post: ClassPost,
    onBackClick: () -> Unit = {},
    onSaveClick: (String, List<AttachmentItem>) -> Unit = { _, _ -> }
) {
    // Extract title and content from post content
    val contentParts = post.content.split("\n\n", limit = 2)
    val initialTitle = if (contentParts.size > 1) contentParts[0] else ""
    val initialContent = if (contentParts.size > 1) contentParts[1] else post.content
    
    var title by remember { mutableStateOf(initialTitle) }
    var content by remember { mutableStateOf(initialContent) }
    var attachments by remember { mutableStateOf(post.attachments) }
    var showAttachmentSheet by remember { mutableStateOf(false) }
    
    val isFormValid = title.isNotBlank() && content.isNotBlank()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Chỉnh sửa bài đăng",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
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
                    TextButton(
                        onClick = {
                            if (isFormValid) {
                                onSaveClick(title + "\n\n" + content, attachments)
                            }
                        },
                        enabled = isFormValid
                    ) {
                        Text(
                            text = "Lưu",
                            color = if (isFormValid) Color.White else Color.Gray,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF2196F3)
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFF1A1A1A)),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Title input
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A2A)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Tiêu đề",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        
                        OutlinedTextField(
                            value = title,
                            onValueChange = { title = it },
                            placeholder = {
                                Text(
                                    text = "Nhập tiêu đề bài đăng...",
                                    color = Color.Gray
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = Color(0xFF2196F3),
                                unfocusedBorderColor = Color.Gray,
                                cursorColor = Color(0xFF2196F3)
                            ),
                            singleLine = true
                        )
                    }
                }
            }
            
            // Content input
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A2A)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Nội dung",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        
                        OutlinedTextField(
                            value = content,
                            onValueChange = { content = it },
                            placeholder = {
                                Text(
                                    text = "Nhập nội dung bài đăng...",
                                    color = Color.Gray
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedBorderColor = Color(0xFF2196F3),
                                unfocusedBorderColor = Color.Gray,
                                cursorColor = Color(0xFF2196F3)
                            ),
                            maxLines = 10
                        )
                    }
                }
            }
            
            // Attachments section
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A2A)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Tệp đính kèm (${attachments.size})",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                            
                            IconButton(onClick = { showAttachmentSheet = true }) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Thêm tệp",
                                    tint = Color(0xFF2196F3)
                                )
                            }
                        }
                        
                        if (attachments.isNotEmpty()) {
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.padding(top = 8.dp)
                            ) {
                                items(attachments) { attachment ->
                                    EditAttachmentChip(
                                        attachment = attachment,
                                        onRemove = {
                                            attachments = attachments.filter { it.id != attachment.id }
                                        }
                                    )
                                }
                            }
                        } else {
                            Text(
                                text = "Chưa có tệp đính kèm",
                                fontSize = 14.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
            }
        }
    }
    
    // Attachment bottom sheet
    if (showAttachmentSheet) {
        AttachmentBottomSheet(
            onDismiss = { showAttachmentSheet = false },
            onAttachmentSelected = { newAttachment ->
                attachments = attachments + newAttachment
                showAttachmentSheet = false
            }
        )
    }
}

@Composable
fun EditAttachmentChip(
    attachment: AttachmentItem,
    onRemove: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xFF3A3A3A)),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = when (attachment.type) {
                    AttachmentType.DRIVE -> Icons.Default.CloudQueue
                    AttachmentType.LINK -> Icons.Default.Link
                    AttachmentType.FILE -> Icons.Default.InsertDriveFile
                    AttachmentType.IMAGE -> Icons.Default.Image
                    AttachmentType.VIDEO -> Icons.Default.VideoFile
                    AttachmentType.PDF -> Icons.Default.PictureAsPdf
                },
                contentDescription = attachment.type.name,
                tint = Color(0xFF2196F3),
                modifier = Modifier.size(16.dp)
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            Text(
                text = attachment.name,
                fontSize = 12.sp,
                color = Color.White,
                maxLines = 1
            )
            
            Spacer(modifier = Modifier.width(8.dp))
            
            IconButton(
                onClick = onRemove,
                modifier = Modifier.size(20.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove",
                    tint = Color.Gray,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttachmentBottomSheet(
    onDismiss: () -> Unit,
    onAttachmentSelected: (AttachmentItem) -> Unit
) {
    val bottomSheetState = rememberModalBottomSheetState()
    
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = bottomSheetState,
        containerColor = Color(0xFF2A2A2A)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Thêm tệp đính kèm",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            val attachmentOptions = listOf(
                "Google Drive" to AttachmentType.DRIVE,
                "Liên kết" to AttachmentType.LINK,
                "Tệp tin" to AttachmentType.FILE,
                "Hình ảnh" to AttachmentType.IMAGE,
                "Video" to AttachmentType.VIDEO,
                "PDF" to AttachmentType.PDF
            )
            
            attachmentOptions.forEach { (name, type) ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val newAttachment = AttachmentItem(
                                id = System.currentTimeMillis().toString(),
                                name = "Sample $name",
                                type = type,
                                size = "1.2 MB"
                            )
                            onAttachmentSelected(newAttachment)
                        }
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = when (type) {
                            AttachmentType.DRIVE -> Icons.Default.CloudQueue
                            AttachmentType.LINK -> Icons.Default.Link
                            AttachmentType.FILE -> Icons.Default.InsertDriveFile
                            AttachmentType.IMAGE -> Icons.Default.Image
                            AttachmentType.VIDEO -> Icons.Default.VideoFile
                            AttachmentType.PDF -> Icons.Default.PictureAsPdf
                        },
                        contentDescription = name,
                        tint = Color(0xFF2196F3),
                        modifier = Modifier.size(24.dp)
                    )
                    
                    Spacer(modifier = Modifier.width(16.dp))
                    
                    Text(
                        text = name,
                        fontSize = 16.sp,
                        color = Color.White
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
