package com.example.ckc_englihoo.Screen

import com.example.ckc_englihoo.Data.TeacherClass
import com.example.ckc_englihoo.Data.ClassPost
import com.example.ckc_englihoo.Data.PostComment
import com.example.ckc_englihoo.Data.Assignment
import com.example.ckc_englihoo.Data.AssignmentType
import com.example.ckc_englihoo.Data.Student
import com.example.ckc_englihoo.Data.InvitationStatus
import com.example.ckc_englihoo.Data.AttachmentItem
import com.example.ckc_englihoo.Data.AttachmentType

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.ui.window.Dialog
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import kotlinx.coroutines.launch
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.rememberCoroutineScope
import com.example.ckc_englihoo.Data.ClassRepository
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ckc_englihoo.R





@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassDetailTeacher(
    navController: NavController,
    teacherClass: TeacherClass,
    onBackClick: () -> Unit = {},
    onCreatePostClick: () -> Unit = {},
    onStudentListClick: () -> Unit = {},
    onGradesClick: () -> Unit = {},
    onClassClick: (TeacherClass) -> Unit = {},
    onPostDetailClick: (String) -> Unit = {},
    onEditPostClick: (String) -> Unit = {}
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Stream", "Classwork", "People")
    var announceText by remember { mutableStateOf("") }

    // Create assignment bottom sheet state
    var showCreateBottomSheet by remember { mutableStateOf(false) }

    // Class details dialog state
    var showClassDetailsDialog by remember { mutableStateOf(false) }

    // Drawer state
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    // Assignment state for ClassworkTab
    var assignments by remember {
        mutableStateOf(
            listOf(
                Assignment(
                    id = "1",
                    title = "Báo cáo hàng ngày",
                    description = "Nộp báo cáo tiến độ dự án",
                    dueDate = "Posted 9 Jun",
                    type = AssignmentType.ASSIGNMENT,
                    points = "100 points"
                ),
                Assignment(
                    id = "2",
                    title = "Bài tập tuần 1",
                    description = "Hoàn thành các câu hỏi trong chương 1",
                    dueDate = "Due tomorrow",
                    type = AssignmentType.ASSIGNMENT,
                    points = "50 points"
                ),
                Assignment(
                    id = "3",
                    title = "Tài liệu học tập",
                    description = "Slide bài giảng và tài liệu tham khảo",
                    dueDate = "Posted 5 Jun",
                    type = AssignmentType.MATERIAL,
                    points = null
                )
            )
        )
    }
    
    // Get posts from ClassRepository - using mutableStateOf for dynamic updates
    var classPosts by remember(teacherClass.id) {
        mutableStateOf(ClassRepository.getClassPosts(teacherClass.id))
    }

    // Refresh posts when returning from create post
    LaunchedEffect(teacherClass.id) {
        classPosts = ClassRepository.getClassPosts(teacherClass.id)
    }

    // Refresh posts when returning from create announcement
    LaunchedEffect(navController.currentBackStackEntry) {
        classPosts = ClassRepository.getClassPosts(teacherClass.id)
    }

    // Get all classes for drawer
    val teacherClasses = remember { ClassRepository.getAllClasses() }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet {
                NavigationDrawerTeacher(
                    classes = teacherClasses,
                    onClassClick = { selectedClass ->
                        onClassClick(selectedClass)
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
                        Text(
                            text = teacherClass.name,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    },
                    navigationIcon = {
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
                    },
                actions = {
                    IconButton(onClick = { showClassDetailsDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Class Details",
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = teacherClass.backgroundColor
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF1A1A1A),
                contentColor = Color.White
            ) {
                tabs.forEachIndexed { index, title ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        icon = {
                            val icon = when (index) {
                                0 -> Icons.Default.Stream
                                1 -> Icons.Default.Assignment
                                2 -> Icons.Default.People
                                else -> Icons.Default.Home
                            }
                            Icon(
                                imageVector = icon,
                                contentDescription = title,
                                tint = if (selectedTab == index) Color.White else Color.Gray
                            )
                        },
                        label = {
                            Text(
                                text = title,
                                color = if (selectedTab == index) Color.White else Color.Gray,
                                fontSize = 12.sp
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Color.White,
                            unselectedIconColor = Color.Gray,
                            selectedTextColor = Color.White,
                            unselectedTextColor = Color.Gray,
                            indicatorColor = Color.Transparent
                        )
                    )
                }
            }
        },
        floatingActionButton = {
            // Show FAB only for Classwork tab
            if (selectedTab == 1) {
                FloatingActionButton(
                    onClick = { showCreateBottomSheet = true },
                    containerColor = Color(0xFFFF6B35),
                    contentColor = Color.White,
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Tạo bài tập",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        },
        containerColor = Color(0xFF1A1A1A)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFF1A1A1A))
        ) {
            // Tab content
            when (selectedTab) {
                0 -> StreamTab(
                    teacherClass = teacherClass,
                    posts = classPosts,
                    announceText = announceText,
                    onAnnounceTextChange = { announceText = it },
                    onPostAnnouncement = { content, attachmentList ->
                        if (content.isNotBlank() || attachmentList.isNotEmpty()) {
                            // Create new post and add to list
                            val newPost = ClassPost(
                                id = System.currentTimeMillis().toString(),
                                authorName = "Mr. Nhâm Chí Bửu",
                                authorAvatar = R.drawable.teacher,
                                content = content,
                                timestamp = "Vừa xong",
                                comments = emptyList(),
                                attachments = attachmentList
                            )
                            // Add to posts list (in real app, this would be saved to database)
                            classPosts = listOf(newPost) + classPosts
                            announceText = ""
                        }
                    },
                    onPostClick = { post ->
                        onPostDetailClick(post.id)
                    },
                    onEditPost = { post ->
                        onEditPostClick(post.id)
                    },
                    onDeletePost = { post ->
                        // Delete post from ClassRepository
                        ClassRepository.deletePostFromClass(teacherClass.id, post.id)
                        // Refresh posts
                        classPosts = ClassRepository.getClassPosts(teacherClass.id)
                    },
                    onCreateAnnouncementClick = {
                        navController.navigate("create_announcement/${teacherClass.id}")
                    }
                )
                1 -> ClassworkTab(
                    posts = classPosts,
                    onPostClick = { post ->
                        onPostDetailClick(post.id)
                    },
                    onEditPost = { post ->
                        onEditPostClick(post.id)
                    },
                    onDeletePost = { post ->
                        // Delete post from ClassRepository
                        ClassRepository.deletePostFromClass(teacherClass.id, post.id)
                        // Refresh posts
                        classPosts = ClassRepository.getClassPosts(teacherClass.id)
                    }
                )
                2 -> PeopleTab(
                    navController = navController,
                    classId = teacherClass.id,
                    studentCount = teacherClass.studentCount,
                    onViewAllClick = onStudentListClick
                )
            }
        }
    }

    // Create Assignment Bottom Sheet
    if (showCreateBottomSheet) {
        CreateAssignmentBottomSheet(
            onDismiss = { showCreateBottomSheet = false },
            onCreateAssignment = { assignmentType ->
                showCreateBottomSheet = false
                navController.navigate("create_assignment/$assignmentType/${teacherClass.id}")
            }
        )
    }

    // Class Details Dialog
    if (showClassDetailsDialog) {
        ClassDetailsDialog(
            teacherClass = teacherClass,
            onDismiss = { showClassDetailsDialog = false }
        )
    }
    } // Close ModalNavigationDrawer
}

@Composable
fun StreamTab(
    teacherClass: TeacherClass,
    posts: List<ClassPost>,
    announceText: String,
    onAnnounceTextChange: (String) -> Unit,
    onPostAnnouncement: (String, List<AttachmentItem>) -> Unit,
    onPostClick: (ClassPost) -> Unit = {},
    onEditPost: (ClassPost) -> Unit = {},
    onDeletePost: (ClassPost) -> Unit = {},
    onCreateAnnouncementClick: () -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF5F5F5)),
        contentPadding = PaddingValues(0.dp),
        verticalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        // Class header card
        item {
            StreamClassHeaderCard(teacherClass = teacherClass)
        }

        // Action buttons
        item {
            StreamActionButtons(
                onCreateAnnouncementClick = onCreateAnnouncementClick
            )
        }

        // Empty state or posts
        if (posts.isEmpty()) {
            item {
                StreamEmptyState()
            }
        } else {
            items(posts) { post ->
                PostCard(
                    post = post,
                    onPostClick = { onPostClick(post) },
                    onEditPost = { onEditPost(post) },
                    onDeletePost = { onDeletePost(post) }
                )
            }
        }
    }
}

@Composable
fun StreamClassHeaderCard(teacherClass: TeacherClass) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF607D8B)),
        shape = RoundedCornerShape(12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(160.dp)
        ) {
            // Background with devices illustration
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(0xFF607D8B),
                                Color(0xFF455A64)
                            )
                        )
                    )
            )

            // Devices illustration (simplified)
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp)
            ) {
                // Phone illustration
                Box(
                    modifier = Modifier
                        .size(60.dp, 100.dp)
                        .background(
                            color = Color(0xFF03DAC6),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .offset(x = (-20).dp, y = (-10).dp)
                )

                // Laptop illustration
                Box(
                    modifier = Modifier
                        .size(80.dp, 50.dp)
                        .background(
                            color = Color(0xFF2196F3),
                            shape = RoundedCornerShape(4.dp)
                        )
                        .offset(x = 20.dp, y = 20.dp)
                )

                // Tablet illustration
                Box(
                    modifier = Modifier
                        .size(40.dp, 60.dp)
                        .background(
                            color = Color(0xFF00BCD4),
                            shape = RoundedCornerShape(6.dp)
                        )
                        .offset(x = (-40).dp, y = 30.dp)
                )
            }

            // Content
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(
                    text = teacherClass.name,
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = teacherClass.description,
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
fun StreamActionButtons(
    onCreateAnnouncementClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Thông báo mới button
        Button(
            onClick = onCreateAnnouncementClick,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFE3F2FD),
                contentColor = Color(0xFF1976D2)
            ),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.Edit,
                contentDescription = "Edit",
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Thông báo mới",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }

        // Đăng lại button
        OutlinedButton(
            onClick = { /* TODO: Repost functionality */ },
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = Color(0xFF1976D2)
            ),
            border = BorderStroke(1.dp, Color(0xFF1976D2)),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = Icons.Default.Refresh,
                contentDescription = "Refresh",
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Đăng lại",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun StreamEmptyState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Illustration
        Box(
            modifier = Modifier
                .size(200.dp)
                .padding(bottom = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            // Main circle background
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .background(
                        color = Color(0xFFE0E0E0),
                        shape = CircleShape
                    )
            )

            // Letter "D" illustration
            Text(
                text = "D",
                fontSize = 80.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF9E9E9E),
                modifier = Modifier.offset(x = (-20).dp)
            )

            // Pencil illustration
            Box(
                modifier = Modifier
                    .size(60.dp, 4.dp)
                    .background(
                        color = Color(0xFF757575),
                        shape = RoundedCornerShape(2.dp)
                    )
                    .offset(x = 30.dp, y = (-10).dp)
                    .rotate(45f)
            )

            // Settings gear illustration
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = Color(0xFF424242),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .offset(x = 50.dp, y = 40.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(
                            color = Color.White,
                            shape = CircleShape
                        )
                        .align(Alignment.Center)
                )
            }

            // Small decorative elements
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(
                        color = Color(0xFF9E9E9E),
                        shape = CircleShape
                    )
                    .offset(x = (-60).dp, y = (-40).dp)
            )

            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(
                        color = Color(0xFF757575),
                        shape = CircleShape
                    )
                    .offset(x = 70.dp, y = (-30).dp)
            )
        }

        // Main message
        Text(
            text = "Đây là bạn giao tiếp với lớp học của mình",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Description
        Text(
            text = "Dùng bảng tin để thông báo, đăng bài tập và trả lời câu hỏi",
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )
    }
}

@Composable
fun AnnounceSection(
    announceText: String,
    onAnnounceTextChange: (String) -> Unit,
    onPostAnnouncement: (String, List<AttachmentItem>) -> Unit
) {
    var showAttachmentMenu by remember { mutableStateOf(false) }
    var attachments by remember { mutableStateOf(listOf<AttachmentItem>()) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A2A)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Main input row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Teacher avatar
                Image(
                    painter = painterResource(id = R.drawable.teacher),
                    contentDescription = "Teacher Avatar",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.width(12.dp))

                // Announce input
                OutlinedTextField(
                    value = announceText,
                    onValueChange = onAnnounceTextChange,
                    placeholder = {
                        Text(
                            text = "Thông báo tin gì đó cho lớp",
                            color = Color.Gray,
                            style = TextStyle(fontSize = 14.sp)
                        )
                    },
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = Color.Gray,
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = Color.White
                    ),
                    shape = RoundedCornerShape(20.dp)
                )

                // Post button
                if (announceText.isNotEmpty() || attachments.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = {
                        onPostAnnouncement(announceText, attachments)
                        // Clear attachments after posting
                        attachments = emptyList()
                    }) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Post",
                            tint = Color.White
                        )
                    }
                }
            }

            // Attachment row
            if (announceText.isNotEmpty() || attachments.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(52.dp)) // Align with text field

                    // Attachment button
                    IconButton(
                        onClick = { showAttachmentMenu = true },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AttachFile,
                            contentDescription = "Thêm tệp đính kèm",
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Link button
                    IconButton(
                        onClick = { /* TODO: Add link */ },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Link,
                            contentDescription = "Chèn liên kết",
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            // Show attachments if any
            if (attachments.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(start = 52.dp)
                ) {
                    items(attachments) { attachment ->
                        AttachmentChip(
                            attachment = attachment,
                            onRemove = {
                                attachments = attachments.filter { it.id != attachment.id }
                            }
                        )
                    }
                }
            }
        }
    }

    // Attachment menu bottom sheet
    if (showAttachmentMenu) {
        AttachmentMenuBottomSheet(
            onDismiss = { showAttachmentMenu = false },
            onAttachmentSelected = { attachment ->
                attachments = attachments + attachment
                showAttachmentMenu = false
            }
        )
    }
}

@Composable
fun ClassHeaderInfo(teacherClass: TeacherClass) {
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
                    text = "Mã lớp: ${teacherClass.id}",
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    text = "${teacherClass.studentCount} học sinh",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF333333)
                )
            }
            
            // Quick actions
            Row {
                IconButton(onClick = { /* TODO: Invite students */ }) {
                    Icon(
                        imageVector = Icons.Default.PersonAdd,
                        contentDescription = "Mời học sinh",
                        tint = teacherClass.backgroundColor
                    )
                }
                IconButton(onClick = { /* TODO: Share class */ }) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Chia sẻ",
                        tint = teacherClass.backgroundColor
                    )
                }
            }
        }
    }
}

@Composable
fun PostsTab(posts: List<ClassPost>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(posts) { post ->
            PostCard(post = post)
        }
    }
}

@Composable
fun PostCard(
    post: ClassPost,
    onPostClick: () -> Unit = {},
    onEditPost: () -> Unit = {},
    onDeletePost: () -> Unit = {}
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp)
            .clickable { onPostClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A2A)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Post header with author info
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Author avatar
                Image(
                    painter = painterResource(id = post.authorAvatar),
                    contentDescription = "Author Avatar",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = post.authorName,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White
                    )
                    Text(
                        text = post.timestamp,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                // More options
                Box {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More",
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                        modifier = Modifier.background(Color.White)
                    ) {
                        DropdownMenuItem(
                            text = { Text("Xem chi tiết", color = Color.Black) },
                            onClick = {
                                showMenu = false
                                onPostClick()
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Visibility,
                                    contentDescription = "View",
                                    tint = Color.Gray
                                )
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Chỉnh sửa", color = Color.Black) },
                            onClick = {
                                showMenu = false
                                onEditPost()
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Edit,
                                    contentDescription = "Edit",
                                    tint = Color.Gray
                                )
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Xóa", color = Color.Red) },
                            onClick = {
                                showMenu = false
                                onDeletePost()
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = Color.Red
                                )
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Post content
            Text(
                text = post.content,
                fontSize = 14.sp,
                color = Color.White,
                lineHeight = 20.sp
            )

            // Show attachments if any
            if (post.attachments.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(post.attachments) { attachment ->
                        AttachmentChip(
                            attachment = attachment,
                            onRemove = { /* Read-only in post view */ }
                        )
                    }
                }
            }

            // Comments section
            if (post.comments.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                post.comments.forEach { comment ->
                    CommentItem(comment = comment)
                }
            }

            // Add comment section
            Spacer(modifier = Modifier.height(8.dp))
            AddCommentSection()
        }
    }
}

@Composable
fun CommentItem(comment: PostComment) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Image(
            painter = painterResource(id = comment.authorAvatar),
            contentDescription = "Comment Author Avatar",
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = comment.authorName,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
            Text(
                text = comment.content,
                fontSize = 12.sp,
                color = Color.Gray,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
fun AddCommentSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Add class comment",
            fontSize = 12.sp,
            color = Color.Gray,
            modifier = Modifier
                .weight(1f)
                .clickable { /* TODO: Open comment input */ }
                .padding(vertical = 8.dp)
        )
    }
}

@Composable
fun ClassworkTab(
    posts: List<ClassPost>,
    onPostClick: (ClassPost) -> Unit = {},
    onEditPost: (ClassPost) -> Unit = {},
    onDeletePost: (ClassPost) -> Unit = {}
) {

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A)),
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(posts) { post ->
            ClassworkPostCard(
                post = post,
                onClick = { onPostClick(post) },
                onEdit = { onEditPost(post) },
                onDelete = { onDeletePost(post) }
            )
        }

        if (posts.isEmpty()) {
            item {
                EmptyClassworkStateWithCat()
            }
        }
    }
}

@Composable
fun ClassworkPostCard(
    post: ClassPost,
    onClick: () -> Unit = {},
    onEdit: () -> Unit = {},
    onDelete: () -> Unit = {}
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A2A)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header with author and menu
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Author avatar
                    Image(
                        painter = painterResource(id = post.authorAvatar),
                        contentDescription = "Author Avatar",
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = post.authorName,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = post.timestamp,
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }

                // More options menu
                Box {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More",
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                        modifier = Modifier.background(Color.White)
                    ) {
                        DropdownMenuItem(
                            text = { Text("Xem chi tiết", color = Color.Black) },
                            onClick = {
                                showMenu = false
                                onClick()
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Visibility,
                                    contentDescription = "View Detail",
                                    tint = Color.Gray
                                )
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Chỉnh sửa", color = Color.Black) },
                            onClick = {
                                showMenu = false
                                onEdit()
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Edit,
                                    contentDescription = "Edit",
                                    tint = Color.Gray
                                )
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Xóa", color = Color.Red) },
                            onClick = {
                                showMenu = false
                                onDelete()
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = Color.Red
                                )
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Post content (truncated for Classwork view)
            Text(
                text = post.content.take(100) + if (post.content.length > 100) "..." else "",
                fontSize = 14.sp,
                color = Color.White,
                lineHeight = 20.sp
            )

            // Show attachments if any
            if (post.attachments.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(post.attachments.take(3)) { attachment ->
                        AttachmentChip(
                            attachment = attachment,
                            onRemove = { /* Read-only in classwork view */ }
                        )
                    }
                    if (post.attachments.size > 3) {
                        item {
                            Text(
                                text = "+${post.attachments.size - 3} more",
                                fontSize = 12.sp,
                                color = Color.Gray,
                                modifier = Modifier.padding(4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyStateCard(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A2A)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color.Gray,
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = description,
                fontSize = 14.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun AssignmentCard(
    assignment: Assignment,
    onClick: () -> Unit = {},
    onEdit: () -> Unit = {},
    onDelete: () -> Unit = {}
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A2A)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Assignment type icon
            val (icon, iconColor) = when (assignment.type) {
                AssignmentType.ASSIGNMENT -> Icons.Default.Assignment to Color(0xFF4285F4)
                AssignmentType.MATERIAL -> Icons.Default.Folder to Color(0xFF34A853)
                AssignmentType.QUIZ -> Icons.Default.Quiz to Color(0xFFEA4335)
            }

            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(iconColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = assignment.type.name,
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Assignment info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = assignment.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
                Text(
                    text = assignment.description,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = assignment.dueDate,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                    assignment.points?.let { points ->
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "• $points",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                }
            }

            // More options
            Box {
                IconButton(onClick = { showMenu = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false },
                    modifier = Modifier.background(Color.White)
                ) {
                    DropdownMenuItem(
                        text = { Text("Xem chi tiết", color = Color.Black) },
                        onClick = {
                            showMenu = false
                            onClick()
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Visibility,
                                contentDescription = "View Detail",
                                tint = Color.Gray
                            )
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Chỉnh sửa", color = Color.Black) },
                        onClick = {
                            showMenu = false
                            onEdit()
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Edit",
                                tint = Color.Gray
                            )
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Xóa", color = Color.Red) },
                        onClick = {
                            showMenu = false
                            onDelete()
                        },
                        leadingIcon = {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = Color.Red
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun PeopleTab(
    navController: NavController,
    classId: String,
    studentCount: Int,
    onViewAllClick: () -> Unit
) {
    // Teachers state management
    var teachers by remember {
        mutableStateOf(
            listOf(
                Student(id = "1", name = "Hoàng Sơn Phạm Phú", email = "teacher@example.com", avatar = R.drawable.teacher)
            )
        )
    }

    // Dialog states
    var showAddTeacherDialog by remember { mutableStateOf(false) }
    var showRemoveTeacherDialog by remember { mutableStateOf(false) }
    var teacherToRemove by remember { mutableStateOf<Student?>(null) }

    // Student dialog states
    var showAddStudentDialog by remember { mutableStateOf(false) }
    var showRemoveStudentDialog by remember { mutableStateOf(false) }
    var studentToRemove by remember { mutableStateOf<Student?>(null) }

    // Students state management
    var students by remember {
        mutableStateOf(
            listOf(
                Student(id = "2", name = "Bảo Trần Quốc", email = "bao.tran@student.com", avatar = R.drawable.student, invitationStatus = InvitationStatus.ACCEPTED),
                Student(id = "3", name = "Khoa Nguyễn Tấn", email = "khoa.nguyen@student.com", avatar = R.drawable.student, invitationStatus = InvitationStatus.ACCEPTED),
                Student(id = "4", name = "Nhựt Dương Minh", email = "nhut.duong@student.com", avatar = R.drawable.student, invitationStatus = InvitationStatus.PENDING),
                Student(id = "5", name = "Phúc Dương Trọng", email = "phuc.duong@student.com", avatar = R.drawable.student, invitationStatus = InvitationStatus.ACCEPTED),
                Student(id = "6", name = "Quyền Lê Văn", email = "quyen.le@student.com", avatar = R.drawable.student, invitationStatus = InvitationStatus.PENDING),
                Student(id = "7", name = "Thông- Nguyễn Hoàng Minh", email = "thong.nguyen@student.com", avatar = R.drawable.student, invitationStatus = InvitationStatus.ACCEPTED),
                Student(id = "8", name = "Trang Nguyễn Thị Huyền", email = "trang.nguyen@student.com", avatar = R.drawable.student, invitationStatus = InvitationStatus.ACCEPTED)
            )
        )
    }

    // Selection and sorting state
    var selectedStudents by remember { mutableStateOf(setOf<String>()) }
    var sortOption by remember { mutableStateOf(SortOption.BY_NAME) }
    var showSortMenu by remember { mutableStateOf(false) }
    var showBulkDeleteDialog by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1A1A1A)),
        contentPadding = PaddingValues(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Teachers section
        item {
            SectionHeader(
                title = "Teachers",
                count = teachers.size,
                onAddClick = { showAddTeacherDialog = true },
                onMoreClick = { /* TODO: More teacher options */ }
            )
        }

        items(teachers) { teacher ->
            PersonCard(
                person = teacher,
                isTeacher = true,
                onRemoveClick = {
                    teacherToRemove = teacher
                    showRemoveTeacherDialog = true
                }
            )
        }

        // Students section
        item {
            Spacer(modifier = Modifier.height(16.dp))
            StudentsSection(
                students = students,
                selectedStudents = selectedStudents,
                sortOption = sortOption,
                showSortMenu = showSortMenu,
                onAddClick = { showAddStudentDialog = true },
                onSortOptionChange = { sortOption = it },
                onShowSortMenuChange = { showSortMenu = it },
                onStudentSelectionChange = { studentId, isSelected ->
                    selectedStudents = if (isSelected) {
                        selectedStudents + studentId
                    } else {
                        selectedStudents - studentId
                    }
                },
                onSelectAll = {
                    selectedStudents = if (selectedStudents.size == students.size) {
                        emptySet()
                    } else {
                        students.map { it.id }.toSet()
                    }
                },
                onBulkAction = { action ->
                    when (action) {
                        "email" -> {
                            println("DEBUG: Send email to selected students: $selectedStudents")
                        }
                        "remove" -> {
                            showBulkDeleteDialog = true
                        }
                        "hide" -> {
                            students = students.map { student ->
                                if (student.id in selectedStudents) {
                                    student.copy(invitationStatus = InvitationStatus.HIDDEN)
                                } else {
                                    student
                                }
                            }
                            selectedStudents = emptySet()
                        }
                    }
                }
            )
        }

        // Sort students based on selected option
        val sortedStudents = when (sortOption) {
            SortOption.BY_NAME -> students.sortedBy { it.name.split(" ").last() } // Sort by first name
            SortOption.BY_SURNAME -> students.sortedBy { it.name.split(" ").first() } // Sort by surname
        }.filter { it.invitationStatus != InvitationStatus.HIDDEN }

        items(sortedStudents) { student ->
            StudentCard(
                student = student,
                isSelected = student.id in selectedStudents,
                onSelectionChange = { isSelected ->
                    selectedStudents = if (isSelected) {
                        selectedStudents + student.id
                    } else {
                        selectedStudents - student.id
                    }
                },
                onPersonClick = {
                    // Navigate to student grading page
                    navController.navigate("student_grading/${student.id}/${student.name}")
                },
                onMenuAction = { action ->
                    when (action) {
                        "email" -> {
                            println("DEBUG: Send email to ${student.name}")
                        }
                        "remove" -> {
                            studentToRemove = student
                            showRemoveStudentDialog = true
                        }
                        "hide" -> {
                            students = students.map {
                                if (it.id == student.id) {
                                    it.copy(invitationStatus = InvitationStatus.HIDDEN)
                                } else {
                                    it
                                }
                            }
                        }
                    }
                }
            )
        }
    }

    // Add Teacher Dialog
    if (showAddTeacherDialog) {
        AddTeacherDialog(
            onDismiss = { showAddTeacherDialog = false },
            onInviteTeacher = { email ->
                // Add new teacher to list
                val newTeacher = Student(
                    id = System.currentTimeMillis().toString(),
                    name = "Invited Teacher",
                    email = email,
                    avatar = R.drawable.teacher
                )
                teachers = teachers + newTeacher
                showAddTeacherDialog = false
                println("DEBUG: Invited teacher with email: $email")
            }
        )
    }

    // Remove Teacher Dialog
    if (showRemoveTeacherDialog && teacherToRemove != null) {
        RemoveTeacherDialog(
            teacher = teacherToRemove!!,
            onDismiss = {
                showRemoveTeacherDialog = false
                teacherToRemove = null
            },
            onConfirmRemove = {
                teachers = teachers.filter { it.id != teacherToRemove!!.id }
                showRemoveTeacherDialog = false
                teacherToRemove = null
                println("DEBUG: Removed teacher: ${teacherToRemove!!.name}")
            }
        )
    }

    // Add Student Dialog
    if (showAddStudentDialog) {
        AddStudentDialog(
            classId = classId,
            onDismiss = { showAddStudentDialog = false },
            onStudentJoined = { studentName ->
                // Add new student to list with PENDING status
                val newStudent = Student(
                    id = System.currentTimeMillis().toString(),
                    name = studentName,
                    email = if (studentName.contains("@")) studentName else "student@example.com",
                    avatar = R.drawable.student,
                    invitationStatus = InvitationStatus.PENDING
                )
                students = students + newStudent
                showAddStudentDialog = false
                println("DEBUG: Student joined via link: $studentName")
            }
        )
    }

    // Remove Student Dialog
    if (showRemoveStudentDialog && studentToRemove != null) {
        RemoveStudentDialog(
            student = studentToRemove!!,
            onDismiss = {
                showRemoveStudentDialog = false
                studentToRemove = null
            },
            onConfirmRemove = {
                students = students.filter { it.id != studentToRemove!!.id }
                showRemoveStudentDialog = false
                studentToRemove = null
                println("DEBUG: Removed student: ${studentToRemove!!.name}")
            }
        )
    }

    // Bulk delete confirmation dialog
    if (showBulkDeleteDialog) {
        BulkDeleteConfirmationDialog(
            selectedCount = selectedStudents.size,
            onConfirm = {
                students = students.filter { it.id !in selectedStudents }
                selectedStudents = emptySet()
                showBulkDeleteDialog = false
            },
            onDismiss = {
                showBulkDeleteDialog = false
            }
        )
    }
}

@Composable
fun StudentsSection(
    students: List<Student>,
    selectedStudents: Set<String>,
    sortOption: SortOption,
    showSortMenu: Boolean,
    onAddClick: () -> Unit,
    onSortOptionChange: (SortOption) -> Unit,
    onShowSortMenuChange: (Boolean) -> Unit,
    onStudentSelectionChange: (String, Boolean) -> Unit,
    onSelectAll: () -> Unit,
    onBulkAction: (String) -> Unit
) {
    var showActionMenu by remember { mutableStateOf(false) }

    Column {
        // Header with title and add button
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Select all checkbox
                Checkbox(
                    checked = selectedStudents.size == students.size && students.isNotEmpty(),
                    onCheckedChange = { onSelectAll() },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color(0xFF2196F3),
                        uncheckedColor = Color.Gray
                    )
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Students",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "(${students.filter { it.invitationStatus != InvitationStatus.HIDDEN }.size})",
                    fontSize = 16.sp,
                    color = Color.Gray
                )
            }

            // Add student button
            IconButton(onClick = onAddClick) {
                Icon(
                    imageVector = Icons.Default.PersonAdd,
                    contentDescription = "Add student",
                    tint = Color(0xFF2196F3),
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Action and Sort controls row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Action button (enabled only when students are selected)
            Box {
                Button(
                    onClick = { showActionMenu = true },
                    enabled = selectedStudents.isNotEmpty(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (selectedStudents.isNotEmpty()) Color(0xFF2196F3) else Color(0xFF424242),
                        contentColor = Color.White,
                        disabledContainerColor = Color(0xFF424242),
                        disabledContentColor = Color.Gray
                    ),
                    modifier = Modifier.height(36.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Thao tác",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Actions",
                        modifier = Modifier.size(18.dp)
                    )
                }

                DropdownMenu(
                    expanded = showActionMenu,
                    onDismissRequest = { showActionMenu = false },
                    modifier = Modifier.background(Color.White)
                ) {
                    DropdownMenuItem(
                        text = { Text("Gửi email cho học viên", color = Color.Black) },
                        onClick = {
                            showActionMenu = false
                            onBulkAction("email")
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Email, contentDescription = null, tint = Color(0xFF2196F3))
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Xóa", color = Color.Red) },
                        onClick = {
                            showActionMenu = false
                            onBulkAction("remove")
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Ẩn", color = Color(0xFFFF9800)) },
                        onClick = {
                            showActionMenu = false
                            onBulkAction("hide")
                        },
                        leadingIcon = {
                            Icon(Icons.Default.VisibilityOff, contentDescription = null, tint = Color(0xFFFF9800))
                        }
                    )
                }
            }

            // Sort button
            Box {
                Button(
                    onClick = { onShowSortMenuChange(true) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF4CAF50),
                        contentColor = Color.White
                    ),
                    modifier = Modifier.height(36.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Sort,
                        contentDescription = "Sort",
                        tint = Color.White,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = when (sortOption) {
                            SortOption.BY_SURNAME -> "Theo họ"
                            SortOption.BY_NAME -> "Theo tên"
                        },
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.ArrowDropDown,
                        contentDescription = "Sort options",
                        modifier = Modifier.size(18.dp)
                    )
                }

                DropdownMenu(
                    expanded = showSortMenu,
                    onDismissRequest = { onShowSortMenuChange(false) },
                    modifier = Modifier.background(Color.White)
                ) {
                    DropdownMenuItem(
                        text = { Text("Sắp xếp theo họ", color = Color.Black) },
                        onClick = {
                            onSortOptionChange(SortOption.BY_SURNAME)
                            onShowSortMenuChange(false)
                        },
                        leadingIcon = {
                            if (sortOption == SortOption.BY_SURNAME) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF2196F3))
                            }
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Sắp xếp theo tên", color = Color.Black) },
                        onClick = {
                            onSortOptionChange(SortOption.BY_NAME)
                            onShowSortMenuChange(false)
                        },
                        leadingIcon = {
                            if (sortOption == SortOption.BY_NAME) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = Color(0xFF2196F3))
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SectionHeader(
    title: String,
    count: Int,
    onAddClick: () -> Unit = {},
    onMoreClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "($count)",
                fontSize = 16.sp,
                color = Color.Gray
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onAddClick) {
                Icon(
                    imageVector = Icons.Default.PersonAdd,
                    contentDescription = "Add person",
                    tint = Color(0xFF2196F3),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun StudentCard(
    student: Student,
    isSelected: Boolean,
    onSelectionChange: (Boolean) -> Unit,
    onPersonClick: () -> Unit,
    onMenuAction: (String) -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onPersonClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFF3A3A3A) else Color(0xFF2A2A2A)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Checkbox
            Checkbox(
                checked = isSelected,
                onCheckedChange = onSelectionChange,
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFF2196F3),
                    uncheckedColor = Color.Gray
                )
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Student avatar
            Image(
                painter = painterResource(id = student.avatar),
                contentDescription = "${student.name} Avatar",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Student info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = student.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
                Text(
                    text = student.email,
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                // Invitation status
                when (student.invitationStatus) {
                    InvitationStatus.PENDING -> {
                        Text(
                            text = "đã được mời",
                            fontSize = 12.sp,
                            color = Color(0xFFFF9800),
                            fontWeight = FontWeight.Medium
                        )
                    }
                    InvitationStatus.ACCEPTED -> {
                        // No status text for accepted students
                    }
                    InvitationStatus.HIDDEN -> {
                        Text(
                            text = "ẩn",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // More options menu
            Box {
                IconButton(onClick = { showMenu = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false },
                    modifier = Modifier.background(Color.White)
                ) {
                    DropdownMenuItem(
                        text = { Text("Gửi email cho học viên", color = Color.Black) },
                        onClick = {
                            showMenu = false
                            onMenuAction("email")
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Email, contentDescription = null, tint = Color(0xFF2196F3))
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Xóa", color = Color.Red) },
                        onClick = {
                            showMenu = false
                            onMenuAction("remove")
                        },
                        leadingIcon = {
                            Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red)
                        }
                    )
                    DropdownMenuItem(
                        text = { Text("Ẩn", color = Color(0xFFFF9800)) },
                        onClick = {
                            showMenu = false
                            onMenuAction("hide")
                        },
                        leadingIcon = {
                            Icon(Icons.Default.VisibilityOff, contentDescription = null, tint = Color(0xFFFF9800))
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun PersonCard(
    person: Student,
    isTeacher: Boolean,
    onRemoveClick: () -> Unit = {},
    onPersonClick: () -> Unit = {}
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                if (!isTeacher) {
                    onPersonClick() // Only students can be graded
                }
            },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2A2A2A)),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Person avatar
            Image(
                painter = painterResource(id = person.avatar),
                contentDescription = "${person.name} Avatar",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Person info
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = person.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
                Text(
                    text = person.email,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                if (isTeacher) {
                    Text(
                        text = "Teacher",
                        fontSize = 12.sp,
                        color = Color(0xFF4285F4),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // More options menu
            Box {
                IconButton(onClick = { showMenu = true }) {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = "More",
                        tint = Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }

                DropdownMenu(
                    expanded = showMenu,
                    onDismissRequest = { showMenu = false },
                    modifier = Modifier.background(Color.White)
                ) {
                    if (isTeacher) {
                        DropdownMenuItem(
                            text = { Text("Xóa khỏi lớp", color = Color.Red) },
                            onClick = {
                                showMenu = false
                                onRemoveClick()
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.PersonRemove,
                                    contentDescription = "Remove Teacher",
                                    tint = Color.Red
                                )
                            }
                        )
                    } else {
                        DropdownMenuItem(
                            text = { Text("Xóa khỏi lớp", color = Color.Red) },
                            onClick = {
                                showMenu = false
                                onRemoveClick()
                            },
                            leadingIcon = {
                                Icon(
                                    Icons.Default.PersonRemove,
                                    contentDescription = "Remove Student",
                                    tint = Color.Red
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AddTeacherDialog(
    onDismiss: () -> Unit,
    onInviteTeacher: (String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var isValidEmail by remember { mutableStateOf(false) }

    // Simple email validation
    LaunchedEffect(email) {
        isValidEmail = email.contains("@") && email.contains(".") && email.length > 5
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Mời giáo viên",
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        },
        text = {
            Column {
                Text(
                    text = "Nhập địa chỉ email của giáo viên để gửi lời mời tham gia lớp học.",
                    color = Color.Gray,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email giáo viên") },
                    placeholder = { Text("teacher@example.com") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF2196F3),
                        unfocusedBorderColor = Color.Gray,
                        cursorColor = Color(0xFF2196F3)
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Email",
                            tint = Color.Gray
                        )
                    }
                )

                if (email.isNotEmpty() && !isValidEmail) {
                    Text(
                        text = "Vui lòng nhập địa chỉ email hợp lệ",
                        color = Color.Red,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (isValidEmail) {
                        onInviteTeacher(email)
                    }
                },
                enabled = isValidEmail
            ) {
                Text(
                    text = "Gửi lời mời",
                    color = if (isValidEmail) Color(0xFF2196F3) else Color.Gray
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy", color = Color.Gray)
            }
        },
        containerColor = Color.White
    )
}

@Composable
fun AddStudentDialog(
    classId: String,
    onDismiss: () -> Unit,
    onStudentJoined: (String) -> Unit
) {
    var inviteLink by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var isValidEmail by remember { mutableStateOf(false) }

    // Generate random invite link
    LaunchedEffect(Unit) {
        val randomCode = (1..8).map { ('A'..'Z').random() }.joinToString("")
        inviteLink = "https://classroom.google.com/c/NjEwOTU4MjE3ODMx?cjc=3tlx..."
    }

    // Simple email validation
    LaunchedEffect(email) {
        isValidEmail = email.contains("@") && email.contains(".") && email.length > 5
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                // Title
                Text(
                    text = "Mời học viên",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp)
                )

                // Link section
                Text(
                    text = "Đường liên kết mời",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF333333),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = inviteLink,
                            fontSize = 14.sp,
                            color = Color(0xFF666666),
                            modifier = Modifier.weight(1f)
                        )

                        IconButton(
                            onClick = {
                                // Copy to clipboard
                                println("DEBUG: Copied invite link: $inviteLink")
                            },
                            modifier = Modifier.size(32.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                contentDescription = "Copy",
                                tint = Color(0xFF2196F3),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Email section
                Text(
                    text = "Nhập tên hoặc email",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF333333),
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("Nhập email học sinh", color = Color(0xFF999999)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFF2196F3),
                        unfocusedBorderColor = Color(0xFFBBDEFB),
                        focusedLabelColor = Color(0xFF2196F3),
                        unfocusedLabelColor = Color.Gray,
                        cursorColor = Color(0xFF2196F3)
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Cancel button
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

                    // Invite button
                    Button(
                        onClick = {
                            if (email.isNotEmpty()) {
                                onStudentJoined(email)
                            }
                        },
                        enabled = email.isNotEmpty(),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2196F3),
                            disabledContainerColor = Color(0xFFE0E0E0)
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 2.dp,
                            pressedElevation = 4.dp
                        )
                    ) {
                        Text(
                            text = "Mời",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (email.isNotEmpty()) Color.White else Color.Gray
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RemoveStudentDialog(
    student: Student,
    onDismiss: () -> Unit,
    onConfirmRemove: () -> Unit
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
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Icon cảnh báo
                Box(
                    modifier = Modifier
                        .size(80.dp)
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
                                tint = Color(0xFFF44336), // Màu đỏ
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }
                }

                // Tiêu đề
                Text(
                    text = "Xác nhận xóa học sinh",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Nội dung thông báo
                Text(
                    text = "Bạn có chắc chắn muốn xóa học sinh:",
                    fontSize = 16.sp,
                    color = Color(0xFF666666),
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                // Tên học sinh
                Text(
                    text = "\"${student.name}\"",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFF44336),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Thông tin bổ sung
                Text(
                    text = "Học sinh sẽ mất quyền truy cập vào lớp học!",
                    fontSize = 14.sp,
                    color = Color(0xFF999999),
                    textAlign = TextAlign.Center,
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
                        onClick = onConfirmRemove,
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
fun RemoveTeacherDialog(
    teacher: Student,
    onDismiss: () -> Unit,
    onConfirmRemove: () -> Unit
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
                    .padding(24.dp),
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
                                tint = Color(0xFFF44336), // Màu đỏ
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }
                }

                // Tiêu đề
                Text(
                    text = "Xác nhận xóa giáo viên",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF333333),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                // Nội dung thông báo
                Text(
                    text = "Bạn có chắc chắn muốn xóa giáo viên:",
                    fontSize = 16.sp,
                    color = Color(0xFF666666),
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp,
                    modifier = Modifier.padding(bottom = 4.dp)
                )

                // Tên giáo viên
                Text(
                    text = "\"${teacher.name}\"",
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
                        onClick = onConfirmRemove,
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
fun AssignmentsTab(posts: List<ClassPost>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(posts) { post ->
            PostCard(post = post)
        }
        
        if (posts.isEmpty()) {
            item {
                EmptyStateCard(
                    icon = Icons.Default.Assignment,
                    title = "Chưa có bài tập nào",
                    description = "Tạo bài tập đầu tiên cho lớp học của bạn"
                )
            }
        }
    }
}

@Composable
fun StudentsTab(
    studentCount: Int,
    onViewAllClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onViewAllClick() },
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
                    imageVector = Icons.Default.People,
                    contentDescription = "Học sinh",
                    tint = Color(0xFF2196F3),
                    modifier = Modifier.size(24.dp)
                )
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Danh sách học sinh",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "$studentCount học sinh",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
                
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Xem chi tiết",
                    tint = Color.Gray
                )
            }
        }
    }
}

@Composable
fun GradesTab(onViewGradesClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onViewGradesClick() },
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
                    imageVector = Icons.Default.Grade,
                    contentDescription = "Điểm số",
                    tint = Color(0xFFFF9800),
                    modifier = Modifier.size(24.dp)
                )
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Quản lý điểm số",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Xem và chỉnh sửa điểm số học sinh",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
                
                Icon(
                    imageVector = Icons.Default.ChevronRight,
                    contentDescription = "Xem chi tiết",
                    tint = Color.Gray
                )
            }
        }
    }
}



@Composable
fun AttachmentChip(
    attachment: AttachmentItem,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.wrapContentWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF3A3A3A)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Attachment icon
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
                tint = Color.White,
                modifier = Modifier.size(16.dp)
            )

            Spacer(modifier = Modifier.width(6.dp))

            // Attachment name
            Text(
                text = attachment.name,
                color = Color.White,
                fontSize = 12.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.width(6.dp))

            // Remove button
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
fun AttachmentMenuBottomSheet(
    onDismiss: () -> Unit,
    onAttachmentSelected: (AttachmentItem) -> Unit
) {
    val bottomSheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = bottomSheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header
            Text(
                text = "Thêm tệp đính kèm",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Attachment options
            AttachmentOption(
                icon = Icons.Default.CloudQueue,
                title = "Thêm từ Drive",
                description = "Chọn tệp từ Google Drive",
                onClick = {
                    onAttachmentSelected(
                        AttachmentItem(
                            id = System.currentTimeMillis().toString(),
                            name = "Document.docx",
                            type = AttachmentType.DRIVE
                        )
                    )
                }
            )

            AttachmentOption(
                icon = Icons.Default.Link,
                title = "Chèn liên kết",
                description = "Thêm URL hoặc liên kết",
                onClick = {
                    onAttachmentSelected(
                        AttachmentItem(
                            id = System.currentTimeMillis().toString(),
                            name = "https://example.com",
                            type = AttachmentType.LINK
                        )
                    )
                }
            )

            AttachmentOption(
                icon = Icons.Default.Upload,
                title = "Tải tệp lên",
                description = "Tải tệp từ thiết bị",
                onClick = {
                    onAttachmentSelected(
                        AttachmentItem(
                            id = System.currentTimeMillis().toString(),
                            name = "file.pdf",
                            type = AttachmentType.FILE
                        )
                    )
                }
            )

            AttachmentOption(
                icon = Icons.Default.Image,
                title = "Chọn ảnh",
                description = "Chọn ảnh từ thư viện",
                onClick = {
                    onAttachmentSelected(
                        AttachmentItem(
                            id = System.currentTimeMillis().toString(),
                            name = "image.jpg",
                            type = AttachmentType.IMAGE
                        )
                    )
                }
            )

            AttachmentOption(
                icon = Icons.Default.CameraAlt,
                title = "Chụp ảnh",
                description = "Chụp ảnh mới",
                onClick = {
                    onAttachmentSelected(
                        AttachmentItem(
                            id = System.currentTimeMillis().toString(),
                            name = "camera_photo.jpg",
                            type = AttachmentType.IMAGE
                        )
                    )
                }
            )

            AttachmentOption(
                icon = Icons.Default.Videocam,
                title = "Quay video",
                description = "Quay video mới",
                onClick = {
                    onAttachmentSelected(
                        AttachmentItem(
                            id = System.currentTimeMillis().toString(),
                            name = "video.mp4",
                            type = AttachmentType.VIDEO
                        )
                    )
                }
            )

            AttachmentOption(
                icon = Icons.Default.PictureAsPdf,
                title = "Tạo tệp PDF mới",
                description = "Tạo tài liệu PDF",
                onClick = {
                    onAttachmentSelected(
                        AttachmentItem(
                            id = System.currentTimeMillis().toString(),
                            name = "new_document.pdf",
                            type = AttachmentType.PDF
                        )
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun AttachmentOption(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icon
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = Color(0xFFF5F5F5),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color(0xFF666666),
                modifier = Modifier.size(20.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Text content
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
            Text(
                text = description,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun BulkDeleteConfirmationDialog(
    selectedCount: Int,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = "Warning",
                tint = Color(0xFFFF9800),
                modifier = Modifier.size(48.dp)
            )
        },
        title = {
            Text(
                text = "Xác nhận xóa",
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        text = {
            Text(
                text = "Bạn có chắc chắn muốn xóa $selectedCount học sinh đã chọn khỏi lớp học không?\n\nHành động này không thể hoàn tác.",
                color = Color.Black,
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Xóa",
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = Color.Gray
                ),
                border = BorderStroke(1.dp, Color.Gray),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = "Hủy",
                    fontWeight = FontWeight.Medium
                )
            }
        },
        containerColor = Color.White,
        shape = RoundedCornerShape(16.dp)
    )
}

@Composable
fun WelcomeMessageCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Welcome illustration
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .background(
                        color = Color(0xFFF5F5F5),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Educational icons arrangement
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Book icon
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = "Book",
                            tint = Color(0xFFBDBDBD),
                            modifier = Modifier.size(24.dp)
                        )

                        // Pencil/Edit icon
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit",
                            tint = Color(0xFFBDBDBD),
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // School icon
                    Icon(
                        imageVector = Icons.Default.School,
                        contentDescription = "School",
                        tint = Color(0xFFBDBDBD),
                        modifier = Modifier.size(32.dp)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // WiFi/Connection icon
                    Icon(
                        imageVector = Icons.Default.Wifi,
                        contentDescription = "Connection",
                        tint = Color(0xFFBDBDBD),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Main message
            Text(
                text = "Đây là bạn giao tiếp với lớp học của mình",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF333333),
                textAlign = TextAlign.Center,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Description
            Text(
                text = "Dùng bảng tin để thông báo, đăng bài tập và trả lời câu hỏi",
                fontSize = 14.sp,
                color = Color(0xFF666666),
                textAlign = TextAlign.Center,
                lineHeight = 20.sp
            )
        }
    }
}

@Composable
fun EmptyClassworkStateWithCat() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Cat illustration (similar to the mockup)
        Box(
            modifier = Modifier
                .size(200.dp)
                .background(
                    color = Color(0xFF2A2A2A),
                    shape = RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Cat body (simplified)
                Box(
                    modifier = Modifier
                        .size(120.dp, 100.dp)
                        .background(
                            color = Color(0xFF9E9E9E),
                            shape = RoundedCornerShape(
                                topStart = 60.dp,
                                topEnd = 60.dp,
                                bottomStart = 20.dp,
                                bottomEnd = 20.dp
                            )
                        )
                ) {
                    // Cat face details
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Eyes
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(
                                        color = Color(0xFF424242),
                                        shape = CircleShape
                                    )
                            )
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(
                                        color = Color(0xFF424242),
                                        shape = CircleShape
                                    )
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Nose
                        Box(
                            modifier = Modifier
                                .size(6.dp, 4.dp)
                                .background(
                                    color = Color(0xFF757575),
                                    shape = RoundedCornerShape(2.dp)
                                )
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        // Mouth (simple line)
                        Box(
                            modifier = Modifier
                                .size(12.dp, 2.dp)
                                .background(
                                    color = Color(0xFF757575),
                                    shape = RoundedCornerShape(1.dp)
                                )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Coffee cup
                Box(
                    modifier = Modifier
                        .size(32.dp, 24.dp)
                        .background(
                            color = Color(0xFF757575),
                            shape = RoundedCornerShape(4.dp)
                        )
                ) {
                    // Cup handle
                    Box(
                        modifier = Modifier
                            .size(8.dp, 12.dp)
                            .offset(x = 28.dp, y = 6.dp)
                            .background(
                                color = Color.Transparent,
                                shape = RoundedCornerShape(4.dp)
                            )
                            .border(
                                width = 2.dp,
                                color = Color(0xFF757575),
                                shape = RoundedCornerShape(4.dp)
                            )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Main message
        Text(
            text = "Đây là nơi giao bài tập",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Description
        Text(
            text = "Bạn có thể thêm bài tập và nhiệm vụ khác cho lớp rồi sắp xếp vào các chủ đề",
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Plus button hint
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        color = Color(0xFF2196F3),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "Hãy tạo bài tập đầu tiên",
                fontSize = 14.sp,
                color = Color.Gray,
                fontStyle = FontStyle.Italic
            )
        }
    }
}

@Composable
fun EmptyClassworkState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Empty state illustration
        Box(
            modifier = Modifier
                .size(180.dp)
                .background(
                    color = Color(0xFF2A2A2A),
                    shape = RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Cat illustration (simplified)
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(
                            color = Color(0xFFBDBDBD),
                            shape = RoundedCornerShape(40.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    // Cat face
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Ears
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(12.dp, 16.dp)
                                    .background(
                                        color = Color(0xFF9E9E9E),
                                        shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                                    )
                            )
                            Box(
                                modifier = Modifier
                                    .size(12.dp, 16.dp)
                                    .background(
                                        color = Color(0xFF9E9E9E),
                                        shape = RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)
                                    )
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Eyes
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .background(
                                        color = Color(0xFF424242),
                                        shape = CircleShape
                                    )
                            )
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .background(
                                        color = Color(0xFF424242),
                                        shape = CircleShape
                                    )
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // Nose
                        Box(
                            modifier = Modifier
                                .size(4.dp, 3.dp)
                                .background(
                                    color = Color(0xFF757575),
                                    shape = RoundedCornerShape(2.dp)
                                )
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Books/Papers
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.Bottom
                ) {
                    // Book 1
                    Box(
                        modifier = Modifier
                            .size(24.dp, 18.dp)
                            .background(
                                color = Color(0xFF757575),
                                shape = RoundedCornerShape(2.dp)
                            )
                    )

                    // Book 2 (taller)
                    Box(
                        modifier = Modifier
                            .size(20.dp, 24.dp)
                            .background(
                                color = Color(0xFF9E9E9E),
                                shape = RoundedCornerShape(2.dp)
                            )
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Coffee cup
                Box(
                    modifier = Modifier
                        .size(16.dp, 12.dp)
                        .background(
                            color = Color(0xFF757575),
                            shape = RoundedCornerShape(bottomStart = 8.dp, bottomEnd = 8.dp)
                        )
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Main message
        Text(
            text = "Đây là nơi giao bài tập",
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Description
        Text(
            text = "Bạn có thể thêm bài tập và nhiệm vụ khác cho lớp rồi sắp xếp vào các chủ đề",
            fontSize = 14.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Plus button hint
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(
                        color = Color(0xFF2196F3),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Text(
                text = "Nhấn + để tạo bài tập đầu tiên",
                fontSize = 14.sp,
                color = Color.Gray,
                fontStyle = FontStyle.Italic
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAssignmentBottomSheet(
    onDismiss: () -> Unit,
    onCreateAssignment: (String) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        contentColor = Color.Black,
        dragHandle = {
            Box(
                modifier = Modifier
                    .width(40.dp)
                    .height(4.dp)
                    .background(
                        color = Color.Gray,
                        shape = RoundedCornerShape(2.dp)
                    )
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Cat illustration at top
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                contentAlignment = Alignment.Center
            ) {
                // Simplified cat illustration
                Box(
                    modifier = Modifier
                        .size(120.dp, 100.dp)
                        .background(
                            color = Color(0xFF9E9E9E),
                            shape = RoundedCornerShape(
                                topStart = 60.dp,
                                topEnd = 60.dp,
                                bottomStart = 20.dp,
                                bottomEnd = 20.dp
                            )
                        )
                ) {
                    // Cat face
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Eyes
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(
                                        color = Color(0xFF424242),
                                        shape = CircleShape
                                    )
                            )
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .background(
                                        color = Color(0xFF424242),
                                        shape = CircleShape
                                    )
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Nose and mouth
                        Box(
                            modifier = Modifier
                                .size(6.dp, 4.dp)
                                .background(
                                    color = Color(0xFF757575),
                                    shape = RoundedCornerShape(2.dp)
                                )
                        )
                    }
                }

                // Coffee cup next to cat
                Box(
                    modifier = Modifier
                        .size(24.dp, 18.dp)
                        .offset(x = 60.dp, y = 30.dp)
                        .background(
                            color = Color(0xFF757575),
                            shape = RoundedCornerShape(4.dp)
                        )
                )
            }

            // Title
            Text(
                text = "Tạo",
                fontSize = 24.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Options
            CreateOptionItem(
                icon = Icons.Default.Assignment,
                title = "Bài tập",
                onClick = { onCreateAssignment("assignment") }
            )

            CreateOptionItem(
                icon = Icons.Default.Quiz,
                title = "Bài kiểm tra",
                onClick = { onCreateAssignment("quiz") }
            )

            CreateOptionItem(
                icon = Icons.Default.Help,
                title = "Câu hỏi",
                onClick = { onCreateAssignment("question") }
            )

            CreateOptionItem(
                icon = Icons.Default.Attachment,
                title = "Tài liệu",
                onClick = { onCreateAssignment("material") }
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun CreateOptionItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp, horizontal = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = Color(0xFF666666),
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = Color.Black
        )
    }
}

// Helper functions for class details
fun generateClassCode(classId: String): String {
    // Generate a random 6-character class code
    val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
    return (1..6)
        .map { chars.random() }
        .joinToString("")
}

fun generateInviteLink(classCode: String): String {
    return "https://classroom.google.com/c/$classCode"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClassDetailsDialog(
    teacherClass: TeacherClass,
    onDismiss: () -> Unit
) {
    val classCode = remember { generateClassCode(teacherClass.id) }
    val inviteLink = remember { generateInviteLink(classCode) }

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        title = {
            Text(
                text = "Thông tin chi tiết lớp học",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Class name
                Text(
                    text = "Tên lớp học",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray
                )
                Text(
                    text = teacherClass.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Class description
                Text(
                    text = "Mô tả",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray
                )
                Text(
                    text = teacherClass.description,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                // Class code
                Text(
                    text = "Mã lớp",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = classCode,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2196F3),
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = {
                            // TODO: Copy class code to clipboard
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy",
                            tint = Color(0xFF2196F3)
                        )
                    }
                }

                // Invite link
                Text(
                    text = "Đường liên kết của lời mời",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Gray
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = inviteLink,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        color = Color(0xFF2196F3),
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = {
                            // TODO: Copy invite link to clipboard
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "Copy",
                            tint = Color(0xFF2196F3)
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color(0xFF2196F3)
                )
            ) {
                Text(
                    text = "Đóng",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    )
}

enum class SortOption {
    BY_NAME, // Sắp xếp theo tên
    BY_SURNAME // Sắp xếp theo họ
}
