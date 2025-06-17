package com.example.ckc_englihoo.Screen

import com.example.ckc_englihoo.Data.TeacherClass
import com.example.ckc_englihoo.Data.LegacyClassPost
import com.example.ckc_englihoo.Data.PostType

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePostTeacher(
    teacherClass: TeacherClass,
    onBackClick: () -> Unit = {},
    onPostCreated: (LegacyClassPost) -> Unit = {}
) {
    var selectedPostType by remember { mutableIntStateOf(0) }
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var dueDate by remember { mutableStateOf("") }
    var maxScore by remember { mutableStateOf("") }
    var attachments by remember { mutableStateOf(listOf<String>()) }
    var showDatePicker by remember { mutableStateOf(false) }
    
    val postTypes = listOf("Thông báo", "Bài tập", "Tài liệu")
    val postTypeEnums = listOf(PostType.ANNOUNCEMENT, PostType.ASSIGNMENT, PostType.MATERIAL)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Tạo bài đăng mới",
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
                            if (title.isNotBlank() && content.isNotBlank()) {
                                val post = LegacyClassPost(
                                    id = System.currentTimeMillis().toString(),
                                    title = title,
                                    content = content,
                                    type = postTypeEnums[selectedPostType],
                                    timestamp = "Vừa xong",
                                    dueDate = if (selectedPostType == 1 && dueDate.isNotBlank()) dueDate else null,
                                    attachments = attachments
                                )
                                onPostCreated(post)
                            }
                        }
                    ) {
                        Text(
                            text = "Đăng",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = teacherClass.backgroundColor
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5)),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Class info
            item {
                ClassInfoCard(teacherClass = teacherClass)
            }
            
            // Post type selector
            item {
                PostTypeSelector(
                    postTypes = postTypes,
                    selectedType = selectedPostType,
                    onTypeSelected = { selectedPostType = it }
                )
            }
            
            // Title input
            item {
                TitleInputCard(
                    title = title,
                    onTitleChange = { title = it },
                    postType = postTypes[selectedPostType]
                )
            }
            
            // Content input
            item {
                ContentInputCard(
                    content = content,
                    onContentChange = { content = it }
                )
            }
            
            // Assignment specific fields
            if (selectedPostType == 1) { // Assignment
                item {
                    AssignmentDetailsCard(
                        dueDate = dueDate,
                        onDueDateChange = { dueDate = it },
                        maxScore = maxScore,
                        onMaxScoreChange = { maxScore = it },
                        onDatePickerClick = { showDatePicker = true }
                    )
                }
            }
            
            // Attachments
            item {
                AttachmentsCard(
                    attachments = attachments,
                    onAddAttachment = { 
                        // TODO: Implement file picker
                        attachments = attachments + "sample_file.pdf"
                    },
                    onRemoveAttachment = { index ->
                        attachments = attachments.toMutableList().apply { removeAt(index) }
                    }
                )
            }
            
            // Preview
            item {
                PreviewCard(
                    title = title,
                    content = content,
                    postType = postTypeEnums[selectedPostType],
                    dueDate = if (selectedPostType == 1) dueDate else null,
                    attachments = attachments
                )
            }
        }
    }
    
    // Date picker dialog (simplified)
    if (showDatePicker) {
        DatePickerDialog(
            onDismiss = { showDatePicker = false },
            onDateSelected = { date ->
                dueDate = date
                showDatePicker = false
            }
        )
    }
}

@Composable
fun ClassInfoCard(teacherClass: TeacherClass) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.School,
                contentDescription = "Class",
                tint = teacherClass.backgroundColor,
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column {
                Text(
                    text = teacherClass.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${teacherClass.studentCount} học sinh",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
fun PostTypeSelector(
    postTypes: List<String>,
    selectedType: Int,
    onTypeSelected: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Loại bài đăng",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                postTypes.forEachIndexed { index, type ->
                    FilterChip(
                        onClick = { onTypeSelected(index) },
                        label = { Text(type) },
                        selected = selectedType == index,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun TitleInputCard(
    title: String,
    onTitleChange: (String) -> Unit,
    postType: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Tiêu đề $postType",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            OutlinedTextField(
                value = title,
                onValueChange = onTitleChange,
                placeholder = { Text("Nhập tiêu đề...") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
            )
        }
    }
}

@Composable
fun ContentInputCard(
    content: String,
    onContentChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            OutlinedTextField(
                value = content,
                onValueChange = onContentChange,
                placeholder = { Text("Nhập nội dung bài đăng...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                maxLines = 5
            )
        }
    }
}

@Composable
fun AssignmentDetailsCard(
    dueDate: String,
    onDueDateChange: (String) -> Unit,
    maxScore: String,
    onMaxScoreChange: (String) -> Unit,
    onDatePickerClick: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Chi tiết bài tập",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            // Due date
            OutlinedTextField(
                value = dueDate,
                onValueChange = onDueDateChange,
                label = { Text("Hạn nộp") },
                placeholder = { Text("dd/MM/yyyy") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = onDatePickerClick) {
                        Icon(
                            imageVector = Icons.Default.DateRange,
                            contentDescription = "Chọn ngày"
                        )
                    }
                }
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Max score
            OutlinedTextField(
                value = maxScore,
                onValueChange = onMaxScoreChange,
                label = { Text("Điểm tối đa") },
                placeholder = { Text("10") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
        }
    }
}

@Composable
fun AttachmentsCard(
    attachments: List<String>,
    onAddAttachment: () -> Unit,
    onRemoveAttachment: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
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
                    text = "Tệp đính kèm",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                
                IconButton(onClick = onAddAttachment) {
                    Icon(
                        imageVector = Icons.Default.AttachFile,
                        contentDescription = "Thêm tệp"
                    )
                }
            }
            
            if (attachments.isNotEmpty()) {
                attachments.forEachIndexed { index, attachment ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.InsertDriveFile,
                                contentDescription = "File",
                                tint = Color(0xFF2196F3),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = attachment,
                                fontSize = 14.sp
                            )
                        }
                        
                        IconButton(
                            onClick = { onRemoveAttachment(index) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Xóa",
                                tint = Color.Gray,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            } else {
                Text(
                    text = "Chưa có tệp đính kèm",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }
    }
}

@Composable
fun PreviewCard(
    title: String,
    content: String,
    postType: PostType,
    dueDate: String?,
    attachments: List<String>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Xem trước",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 12.dp)
            )
            
            if (title.isNotBlank() || content.isNotBlank()) {
                // Post preview
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {
                        // Post header
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            val (icon, iconColor) = when (postType) {
                                PostType.ANNOUNCEMENT -> Icons.Default.Notifications to Color(0xFF4CAF50)
                                PostType.ASSIGNMENT -> Icons.Default.Assignment to Color(0xFFFF9800)
                                PostType.MATERIAL -> Icons.Default.Folder to Color(0xFF2196F3)
                            }
                            
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = iconColor,
                                modifier = Modifier.size(16.dp)
                            )
                            
                            Spacer(modifier = Modifier.width(6.dp))
                            
                            Text(
                                text = title.ifBlank { "Tiêu đề..." },
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (title.isBlank()) Color.Gray else Color.Black
                            )
                        }
                        
                        if (content.isNotBlank()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = content,
                                fontSize = 12.sp,
                                color = Color(0xFF333333)
                            )
                        }
                        
                        // Due date for assignments
                        dueDate?.takeIf { it.isNotBlank() }?.let { date ->
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Schedule,
                                    contentDescription = "Hạn nộp",
                                    tint = Color(0xFFFF9800),
                                    modifier = Modifier.size(12.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Hạn nộp: $date",
                                    fontSize = 10.sp,
                                    color = Color(0xFFFF9800),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                        
                        // Attachments
                        if (attachments.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(6.dp))
                            attachments.forEach { attachment ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AttachFile,
                                        contentDescription = "Tệp đính kèm",
                                        tint = Color(0xFF2196F3),
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = attachment,
                                        fontSize = 10.sp,
                                        color = Color(0xFF2196F3)
                                    )
                                }
                            }
                        }
                    }
                }
            } else {
                Text(
                    text = "Nhập tiêu đề và nội dung để xem trước",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
        }
    }
}

@Composable
fun DatePickerDialog(
    onDismiss: () -> Unit,
    onDateSelected: (String) -> Unit
) {
    // Simplified date picker - in real app, use DatePickerDialog from Material3
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Chọn ngày",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Simplified date selection
                val dates = listOf("25/12/2024", "26/12/2024", "27/12/2024", "28/12/2024")
                
                dates.forEach { date ->
                    TextButton(
                        onClick = { onDateSelected(date) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(date)
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                TextButton(onClick = onDismiss) {
                    Text("Hủy")
                }
            }
        }
    }
}
