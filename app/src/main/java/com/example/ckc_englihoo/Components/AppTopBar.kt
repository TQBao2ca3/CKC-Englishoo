package com.example.ckc_englihoo.Components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ckc_englihoo.API.AppViewModel
import com.example.ckc_englihoo.DataClass.Student
import com.example.ckc_englihoo.DataClass.Teacher

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: String,
    viewModel: AppViewModel,
    navController: NavController,
    showLogout: Boolean = true
) {
    val currentStudent by viewModel.currentStudent.collectAsState()
    val currentTeacher by viewModel.currentTeacher.collectAsState()
    
    var showLogoutDialog by remember { mutableStateOf(false) }
    
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        },
        actions = {
            if (showLogout) {
                // User info
                currentStudent?.let { student ->
                    UserInfoSection(
                        name = student.fullname,
                        userType = "Học sinh",
                        onLogoutClick = { showLogoutDialog = true }
                    )
                }

                currentTeacher?.let { teacher ->
                    UserInfoSection(
                        name = teacher.fullname,
                        userType = "Giáo viên",
                        onLogoutClick = { showLogoutDialog = true }
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White,
            titleContentColor = Color.Black,
            actionIconContentColor = Color.Black
        )
    )
    
    // Logout confirmation dialog
    if (showLogoutDialog) {
        LogoutConfirmationDialog(
            onConfirm = {
                viewModel.logout()
                navController.navigate("login") {
                    popUpTo(0) { inclusive = true }
                }
                showLogoutDialog = false
            },
            onDismiss = { showLogoutDialog = false }
        )
    }
}

@Composable
fun UserInfoSection(
    name: String,
    userType: String,
    onLogoutClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        // User info
        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier.padding(end = 8.dp)
        ) {
            Text(
                text = name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = userType,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        
        // User avatar
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = "User Avatar",
            modifier = Modifier
                .size(32.dp)
                .padding(end = 8.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        
        // Logout button
        IconButton(onClick = onLogoutClick) {
            Icon(
                imageVector = Icons.Default.ExitToApp,
                contentDescription = "Đăng xuất",
                tint = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
fun LogoutConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Xác nhận đăng xuất",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Text(
                text = "Bạn có chắc chắn muốn đăng xuất khỏi ứng dụng?",
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error
                )
            ) {
                Text("Đăng xuất")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleAppTopBar(
    title: String,
    onBackClick: (() -> Unit)? = null
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.Bold
                )
            )
        },
        navigationIcon = {
            onBackClick?.let { backClick ->
                IconButton(onClick = backClick) {
                    Icon(
                        imageVector = Icons.Default.ExitToApp,
                        contentDescription = "Quay lại"
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.White,
            titleContentColor = Color.Black,
            navigationIconContentColor = Color.Black
        )
    )
}
