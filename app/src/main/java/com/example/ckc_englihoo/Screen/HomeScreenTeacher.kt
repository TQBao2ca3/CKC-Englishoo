package com.example.ckc_englihoo.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.ui.window.Dialog
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.ckc_englihoo.R
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import com.example.ckc_englihoo.Data.TeacherClass
import com.example.ckc_englihoo.Data.ClassRepository



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreenTeacher(
    onClassClick: (TeacherClass) -> Unit = {},
    onCreateClassClick: () -> Unit = {},
    onProfileClick: () -> Unit = {}
) {
    val teacherName = "Mr. Nhâm Chí Bửu"
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    // Dữ liệu các lớp học từ ClassRepository với state để refresh khi có thay đổi
    var teacherClasses by remember { mutableStateOf(ClassRepository.getAllClasses()) }
    var refreshTrigger by remember { mutableStateOf(0) }

    // Refresh danh sách classes khi screen được hiển thị lại hoặc có thay đổi
    LaunchedEffect(refreshTrigger) {
        teacherClasses = ClassRepository.getAllClasses()
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                NavigationDrawerTeacher(
                    classes = teacherClasses,
                    onClassClick = { teacherClass ->
                        onClassClick(teacherClass)
                        scope.launch { drawerState.close() }
                    },
                    onSettingsClick = {
                        // TODO: Navigate to settings
                        scope.launch { drawerState.close() }
                    },
                    onHelpClick = {
                        // TODO: Navigate to help
                        scope.launch { drawerState.close() }
                    },
                    onCloseDrawer = {
                        scope.launch { drawerState.close() }
                    }
                )
            }
        }
    ) {
        Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Menu icon
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Menu,
                                contentDescription = "Menu",
                                tint = Color.White
                            )
                        }
                        
                        // Title
                        Text(
                            text = "Lớp Học Của Tôi",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            modifier = Modifier.weight(1f)
                        )
                        
                        // Profile avatar
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .clickable {
                                    println("DEBUG: Profile avatar clicked")
                                    onProfileClick()
                                }
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.teacher),
                                contentDescription = "Profile",
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        Spacer(modifier = Modifier.width(40.dp))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1976D2)
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    println("DEBUG: FAB create class clicked")
                    onCreateClassClick()
                },
                containerColor = Color(0xFF1976D2),
                contentColor = Color.White
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Tạo lớp học mới"
                )
            }
        }
    ) { paddingValues ->
        if (teacherClasses.isEmpty()) {
            // Simple empty state when no classes
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color(0xFFF5F5F5)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Empty state illustration
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .background(
                            color = Color(0xFFF0F0F0),
                            shape = RoundedCornerShape(16.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        // Educational icons arrangement
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Book icon
                            Icon(
                                imageVector = Icons.Default.MenuBook,
                                contentDescription = "Book",
                                tint = Color(0xFFBDBDBD),
                                modifier = Modifier.size(32.dp)
                            )

                            // Pencil/Edit icon
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit",
                                tint = Color(0xFFBDBDBD),
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // School icon
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = "School",
                            tint = Color(0xFFBDBDBD),
                            modifier = Modifier.size(48.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // WiFi/Connection icon
                        Icon(
                            imageVector = Icons.Default.Wifi,
                            contentDescription = "Connection",
                            tint = Color(0xFFBDBDBD),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Chưa có lớp học nào",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF666666)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Vui lòng tạo lớp học đầu tiên của bạn",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color(0xFF888888),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color(0xFFF5F5F5)),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Welcome header
                item {
                    WelcomeHeaderTeacher(teacherName = teacherName)
                }

                // Class list
                items(teacherClasses) { teacherClass ->
                    ClassCard(
                        teacherClass = teacherClass,
                        onClick = { onClassClick(teacherClass) },
                        onDataChanged = { refreshTrigger++ }
                    )
                }

                // Bottom spacing
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
    }
}

@Composable
fun WelcomeHeaderTeacher(teacherName: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1976D2))
            ) {
                Image(
                    painter = painterResource(id = R.drawable.teacher),
                    contentDescription = "Teacher Avatar",
                    modifier = Modifier.fillMaxSize()
                )
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Welcome text
            Column {
                Text(
                    text = "Chào mừng trở lại!",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
                Text(
                    text = teacherName,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333)
                )
            }
        }
    }
}

@Composable
fun ClassCard(
    teacherClass: TeacherClass,
    onClick: () -> Unit,
    onDataChanged: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                println("DEBUG: ClassCard clicked")
                onClick()
            },
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box {
            // Background gradient
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                teacherClass.backgroundColor,
                                teacherClass.backgroundColor.copy(alpha = 0.8f)
                            )
                        ),
                        shape = RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp)
                    )
            )
            
            // Content
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Header section with gradient background
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .padding(16.dp)
                ) {
                    Column {
                        Text(
                            text = teacherClass.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        
                        Spacer(modifier = Modifier.height(4.dp))
                        
                        Text(
                            text = teacherClass.description,
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.9f),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    
                    // More options button
                    Box(
                        modifier = Modifier.align(Alignment.TopEnd)
                    ) {
                        var showDropdown by remember { mutableStateOf(false) }
                        var showEditNameDialog by remember { mutableStateOf(false) }
                        var showEditDescriptionDialog by remember { mutableStateOf(false) }
                        var showDeleteConfirmDialog by remember { mutableStateOf(false) }

                        IconButton(
                            onClick = {
                                showDropdown = true
                                println("DEBUG: More options clicked")
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = "More options",
                                tint = Color.White
                            )
                        }

                        // Dropdown Menu
                        DropdownMenu(
                            expanded = showDropdown,
                            onDismissRequest = { showDropdown = false },
                            modifier = Modifier.background(Color.White)
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        "Đổi tên",
                                        color = Color.Black,
                                        fontSize = 14.sp
                                    )
                                },
                                onClick = {
                                    showDropdown = false
                                    showEditNameDialog = true
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Edit,
                                        contentDescription = "Edit name",
                                        tint = Color.Gray
                                    )
                                }
                            )

                            DropdownMenuItem(
                                text = {
                                    Text(
                                        "Đổi mô tả",
                                        color = Color.Black,
                                        fontSize = 14.sp
                                    )
                                },
                                onClick = {
                                    showDropdown = false
                                    showEditDescriptionDialog = true
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Description,
                                        contentDescription = "Edit description",
                                        tint = Color.Gray
                                    )
                                }
                            )

                            DropdownMenuItem(
                                text = {
                                    Text(
                                        "Xóa lớp",
                                        color = Color.Red,
                                        fontSize = 14.sp
                                    )
                                },
                                onClick = {
                                    showDropdown = false
                                    showDeleteConfirmDialog = true
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = "Delete class",
                                        tint = Color.Red
                                    )
                                }
                            )
                        }

                        // Edit Name Dialog
                        if (showEditNameDialog) {
                            EditClassNameDialog(
                                currentName = teacherClass.name,
                                onDismiss = { showEditNameDialog = false },
                                onConfirm = { newName ->
                                    // Update class name in repository
                                    val success = ClassRepository.updateClassName(teacherClass.id, newName)
                                    if (success) {
                                        // Trigger UI refresh
                                        onDataChanged()
                                        showEditNameDialog = false
                                        println("DEBUG: Class name updated to: $newName")
                                    }
                                }
                            )
                        }

                        // Edit Description Dialog
                        if (showEditDescriptionDialog) {
                            EditClassDescriptionDialog(
                                currentDescription = teacherClass.description,
                                onDismiss = { showEditDescriptionDialog = false },
                                onConfirm = { newDescription ->
                                    // Update class description in repository
                                    val success = ClassRepository.updateClassDescription(teacherClass.id, newDescription)
                                    if (success) {
                                        // Trigger UI refresh
                                        onDataChanged()
                                        showEditDescriptionDialog = false
                                        println("DEBUG: Class description updated to: $newDescription")
                                    }
                                }
                            )
                        }

                        // Delete Confirmation Dialog
                        if (showDeleteConfirmDialog) {
                            DeleteClassConfirmDialog(
                                className = teacherClass.name,
                                onDismiss = { showDeleteConfirmDialog = false },
                                onConfirm = {
                                    // Delete class from repository
                                    val success = ClassRepository.deleteClass(teacherClass.id)
                                    if (success) {
                                        // Trigger UI refresh
                                        onDataChanged()
                                        showDeleteConfirmDialog = false
                                        println("DEBUG: Class deleted: ${teacherClass.name}")
                                    }
                                }
                            )
                        }
                    }
                }
                
                // Bottom section with student count
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Students",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                    
                    Spacer(modifier = Modifier.width(8.dp))
                    
                    Text(
                        text = "${teacherClass.studentCount} học sinh",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}

// Edit Class Name Dialog
@Composable
fun EditClassNameDialog(
    currentName: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var newName by remember { mutableStateOf(currentName) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Đổi tên lớp",
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        },
        text = {
            Column {
                Text(
                    text = "Nhập tên mới cho lớp:",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = newName,
                    onValueChange = { newName = it },
                    label = { Text("Tên lớp") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF2196F3),
                        unfocusedBorderColor = Color(0xFFBBDEFB),
                        focusedLabelColor = Color(0xFF2196F3),
                        unfocusedLabelColor = Color.Gray
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (newName.isNotBlank()) {
                        onConfirm(newName.trim())
                    }
                },
                enabled = newName.isNotBlank() && newName.trim() != currentName
            ) {
                Text(
                    "Lưu",
                    color = Color(0xFF2196F3),
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    "Hủy",
                    color = Color.Gray
                )
            }
        },
        containerColor = Color(0xFFE3F2FD)
    )
}

// Edit Class Description Dialog
@Composable
fun EditClassDescriptionDialog(
    currentDescription: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    var newDescription by remember { mutableStateOf(currentDescription) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Đổi mô tả lớp",
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        },
        text = {
            Column {
                Text(
                    text = "Nhập mô tả mới cho lớp:",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = newDescription,
                    onValueChange = { newDescription = it },
                    label = { Text("Mô tả lớp") },
                    maxLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF2196F3),
                        unfocusedBorderColor = Color(0xFFBBDEFB),
                        focusedLabelColor = Color(0xFF2196F3),
                        unfocusedLabelColor = Color.Gray
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (newDescription.isNotBlank()) {
                        onConfirm(newDescription.trim())
                    }
                },
                enabled = newDescription.isNotBlank() && newDescription.trim() != currentDescription
            ) {
                Text(
                    "Lưu",
                    color = Color(0xFF2196F3),
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    "Hủy",
                    color = Color.Gray
                )
            }
        },
        containerColor = Color(0xFFE3F2FD)
    )
}

// Delete Class Confirmation Dialog
@Composable
fun DeleteClassConfirmDialog(
    className: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Icon cảnh báo
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .padding(bottom = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier.fillMaxSize(),
                        shape = RoundedCornerShape(40.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFFFEBEE) // Màu đỏ nhạt
                        )
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Cảnh báo",
                                tint = Color(0xFFFFB300), // Màu vàng
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }
                }

                // Tiêu đề
                Text(
                    text = "Xác nhận xóa lớp",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Nội dung thông báo
                Text(
                    text = "Bạn có chắc chắn muốn xóa lớp:",
                    fontSize = 16.sp,
                    color = Color(0xFF666666),
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                // Tên lớp
                Text(
                    text = "\"$className\"",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFF44336),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Thông tin bổ sung
                Text(
                    text = "Hành động này không thể hoàn tác!",
                    fontSize = 14.sp,
                    color = Color(0xFF999999),
                    textAlign = TextAlign.Center,
                    fontStyle = FontStyle.Italic,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                // Các nút hành động
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Nút Hủy
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = ButtonDefaults.outlinedButtonBorder.copy(
                            width = 1.5.dp
                        ),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = Color(0xFF666666)
                        )
                    ) {
                        Text(
                            text = "Hủy",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Nút Xóa
                    Button(
                        onClick = onConfirm,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFF44336) // Màu đỏ
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 2.dp,
                            pressedElevation = 4.dp
                        )
                    ) {
                        Text(
                            text = "Xóa",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyClassesState(
    onCreateClassClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Empty state illustration
        Box(
            modifier = Modifier
                .size(200.dp)
                .background(
                    color = Color(0xFFF0F0F0),
                    shape = RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // School/Education icons
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Book icon
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = "Book",
                        tint = Color(0xFFBDBDBD),
                        modifier = Modifier.size(32.dp)
                    )

                    // Pencil/Edit icon
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Edit",
                        tint = Color(0xFFBDBDBD),
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Classroom icon
                Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = "School",
                    tint = Color(0xFFBDBDBD),
                    modifier = Modifier.size(48.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // WiFi/Connection icon
                Icon(
                    imageVector = Icons.Default.Wifi,
                    contentDescription = "Connection",
                    tint = Color(0xFFBDBDBD),
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Main message
        Text(
            text = "Đây là bạn giao tiếp với lớp học của mình",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color(0xFF333333),
            textAlign = TextAlign.Center,
            lineHeight = 24.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Description
        Text(
            text = "Dùng bảng tin để thông báo, đăng bài tập và trả lời câu hỏi",
            fontSize = 14.sp,
            color = Color(0xFF666666),
            textAlign = TextAlign.Center,
            lineHeight = 20.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Bottom navigation hint
        Row(
            horizontalArrangement = Arrangement.spacedBy(32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Dashboard,
                    contentDescription = "Bảng tin",
                    tint = Color(0xFF1976D2),
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Bảng tin",
                    fontSize = 12.sp,
                    color = Color(0xFF1976D2),
                    fontWeight = FontWeight.Medium
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Assignment,
                    contentDescription = "Bài tập trên lớp",
                    tint = Color(0xFF666666),
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Bài tập trên lớp",
                    fontSize = 12.sp,
                    color = Color(0xFF666666)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.People,
                    contentDescription = "Mọi người",
                    tint = Color(0xFF666666),
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Mọi người",
                    fontSize = 12.sp,
                    color = Color(0xFF666666)
                )
            }
        }
    }
}
