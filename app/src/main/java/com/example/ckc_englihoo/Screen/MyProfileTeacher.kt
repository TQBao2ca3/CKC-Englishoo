package com.example.ckc_englihoo.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ckc_englihoo.R
import com.example.ckc_englihoo.LogoutConfirmationDialog
import com.example.ckc_englihoo.LoadingScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyProfileTeacher(
    onBackClick: () -> Unit = {},
    onNavigateToClasses: () -> Unit = {},
    onNavigateToSettings: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    // Định nghĩa màu sắc
    val primaryColor = Color(0xFF1976D2) // Màu xanh dương đậm cho teacher
    val secondaryColor = Color(0xFFF5F5F5) // Màu nền xám nhạt
    val uriHandler = LocalUriHandler.current
    
    // State cho dropdown menu
    var showMenu by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    var isRefreshing by remember { mutableStateOf(false) }

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
                            "Hồ Sơ Giảng Viên",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            letterSpacing = 0.5.sp,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )

                        // Spacer để cân bằng bên phải (48dp - tương đương với icon menu)
                        Spacer(modifier = Modifier.width(48.dp))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primaryColor
                ),
                navigationIcon = {
                    // Nút back với hiệu ứng ripple
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Quay lại",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                },
                actions = {
                    // Menu settings với thiết kế mới
                    Box {
                        // Đơn giản với IconButton
                        IconButton(
                            onClick = { showMenu = true },
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Tùy chọn",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        // Dropdown menu với thiết kế mới
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false },
                            modifier = Modifier
                                .width(240.dp)
                                .background(
                                    color = Color.White,
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .clip(RoundedCornerShape(16.dp)),
                            offset = DpOffset(x = (-195).dp, y = 8.dp)
                        ) {
                            // 1. Thông tin tài khoản
                            DropdownMenuItem(
                                text = {
                                    MenuItemContentTeacher(
                                        icon = Icons.Default.Info,
                                        title = "Thông tin tài khoản",
                                        iconColor = Color(0xFF2196F3)
                                    )
                                },
                                onClick = {
                                    // Hiển thị loading khi refresh
                                    showMenu = false
                                    isRefreshing = true
                                },
                                modifier = Modifier
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            )

                            // 2. Quản lý lớp học
                            DropdownMenuItem(
                                text = {
                                    MenuItemContentTeacher(
                                        icon = Icons.Default.School,
                                        title = "Quản lý lớp học",
                                        iconColor = Color(0xFF4CAF50)
                                    )
                                },
                                onClick = {
                                    // Chuyển sang màn hình quản lý lớp học
                                    showMenu = false
                                    onNavigateToClasses()
                                },
                                modifier = Modifier
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            )
                            // 3. Đăng xuất
                            DropdownMenuItem(
                                text = {
                                    MenuItemContentTeacher(
                                        icon = Icons.Default.ExitToApp,
                                        title = "Đăng xuất",
                                        iconColor = Color(0xFFF44336),
                                        titleColor = Color(0xFFF44336)
                                    )
                                },
                                onClick = {
                                    // Hiển thị dialog xác nhận đăng xuất
                                    showMenu = false
                                    showLogoutDialog = true
                                },
                                modifier = Modifier
                                    .padding(horizontal = 8.dp, vertical = 2.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            )

                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))
                }
            )
        }
    ) { innerPadding ->
        // Main content
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .background(secondaryColor)
        ) {
            // Header với thông tin cơ bản và avatar
            ProfileHeaderTeacher(
                primaryColor = primaryColor,
                onAvatarClick = { /* TODO: Thay đổi avatar */ }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Các card thông tin giảng viên
            TeacherInfoCard()

            Spacer(modifier = Modifier.height(16.dp))

            // Thông tin trung tâm
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 6.dp
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.facebook),
                        contentDescription = "Facebook icon",
                        modifier = Modifier.size(24.dp)
                    )

                    ClickableText(
                        text = AnnotatedString("Trung Tâm Ngoại Ngữ Trường Cao Thắng"),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            color = primaryColor,
                            fontWeight = FontWeight.Medium
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp),
                        onClick = {
                            uriHandler.openUri("https://www.facebook.com/englishcenter.caothang.edu.vn")
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Dialog xác nhận đăng xuất
    if (showLogoutDialog) {
        LogoutConfirmationDialog(
            onDismiss = { showLogoutDialog = false },
            onConfirm = {
                showLogoutDialog = false
                onLogout()
            }
        )
    }

    // Loading overlay khi refresh thông tin tài khoản
    if (isRefreshing) {
        LoadingScreen(
            loadingText = "Đang tải lại thông tin...",
            onLoadingComplete = { isRefreshing = false }
        )
    }
}

// Component tái sử dụng cho menu item
@Composable
fun MenuItemContentTeacher(
    icon: ImageVector,
    title: String,
    iconColor: Color,
    titleColor: Color = Color.Black
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        // Icon với background gradient
        Box(
            modifier = Modifier
                .size(42.dp)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            iconColor.copy(alpha = 0.15f),
                            iconColor.copy(alpha = 0.05f)
                        )
                    ),
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = iconColor,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        // Text content
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                color = titleColor,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
fun ProfileHeaderTeacher(
    primaryColor: Color,
    onAvatarClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        color = primaryColor
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 16.dp, bottom = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .offset(y = 40.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 18.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .border(
                            width = 3.dp,
                            color = Color(0xFFBBDEFB),
                            shape = RoundedCornerShape(16.dp)
                        )
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .padding(16.dp)
                            .size(120.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        // Avatar
                        Surface(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .border(4.dp, Color.White, CircleShape),
                            color = MaterialTheme.colorScheme.surface
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.teacher),
                                contentDescription = "Teacher Photo",
                                contentScale = ContentScale.Crop
                            )
                        }

                        // Icon dấu cộng ở giữa
                        IconButton(
                            onClick = onAvatarClick,
                            modifier = Modifier
                                .size(36.dp)
                                .background(Color(0x99000000), CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Change Avatar",
                                tint = Color.White
                            )
                        }
                    }
                }
            }
        }
    }

    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
fun TeacherInfoCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Header with avatar space
            Spacer(modifier = Modifier.height(10.dp))

            // Main title
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Thông tin giảng viên",
                    tint = Color(0xFF1976D2)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "THÔNG TIN GIẢNG VIÊN",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1976D2)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Divider()
            Spacer(modifier = Modifier.height(16.dp))

            // Teacher info
            InfoRowTeacher(
                icon = Icons.Default.AccountCircle,
                label = "Họ & Tên:",
                value = "Mr. Nhâm Chí Bửu"
            )
            InfoRowTeacher(
                icon = Icons.Default.Badge,
                label = "Mã giảng viên:",
                value = "GV001"
            )
            InfoRowTeacher(
                icon = Icons.Default.DateRange,
                label = "Ngày sinh:",
                value = "15/05/1985"
            )
            InfoRowTeacher(
                icon = Icons.Default.School,
                label = "Chuyên môn:",
                value = "Tiếng Anh"
            )
            InfoRowTeacher(
                icon = Icons.Default.Email,
                label = "Email:",
                value = "nham.chi.buu@caothang.edu.vn"
            )
            InfoRowTeacher(
                icon = Icons.Default.Phone,
                label = "Điện thoại:",
                value = "0901234567"
            )
        }
    }
}

@Composable
fun InfoRowTeacher(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color.Gray,
            modifier = Modifier.size(20.dp)
        )

        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 8.dp, end = 4.dp),
            color = Color.Gray
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}




