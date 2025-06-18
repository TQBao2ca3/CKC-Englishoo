package com.example.ckc_englihoo.Screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ckc_englihoo.API.AppViewModel
import com.example.ckc_englihoo.R
import com.example.ckc_englihoo.Utils.UserPreferences

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: AppViewModel = viewModel()
) {
    // States
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isStudent by remember { mutableStateOf(true) }
    var rememberLogin by remember { mutableStateOf(true) }
    var passwordVisible by remember { mutableStateOf(false) }
    
    // ViewModel states
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    
    // Load saved login data
    LaunchedEffect(Unit) {
        val savedUsername = viewModel.getSavedUsername()
        val savedPassword = viewModel.getSavedPassword()
        val isRemember = viewModel.isRememberLogin()
        
        if (savedUsername != null) {
            username = savedUsername
        }
        if (savedPassword != null && isRemember) {
            password = savedPassword
            rememberLogin = true
        }
    }
    
    // Navigate when login successful
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            navController.navigate("main") {
                popUpTo("login") { inclusive = true }
            }
        }
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Logo
        Image(
            painter = painterResource(id = R.drawable.group),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(120.dp)
                .padding(bottom = 32.dp)
        )
        
        // Title
        Text(
            text = "Đăng nhập",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Chào mừng bạn quay trở lại!",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // User type selection
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FilterChip(
                    onClick = { isStudent = true },
                    label = { Text("Học sinh") },
                    selected = isStudent,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                FilterChip(
                    onClick = { isStudent = false },
                    label = { Text("Giáo viên") },
                    selected = !isStudent,
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Username field
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Tài khoản") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Password field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Mật khẩu") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = if (passwordVisible) "Ẩn mật khẩu" else "Hiện mật khẩu"
                    )
                }
            }
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Remember login checkbox
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = rememberLogin,
                onCheckedChange = { rememberLogin = it }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Ghi nhớ đăng nhập",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        // Error message
        errorMessage?.let { error ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    text = error,
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        
        // Login button
        Button(
            onClick = {
                viewModel.clearError()
                if (isStudent) {
                    viewModel.loginStudent(username, password, rememberLogin)
                } else {
                    viewModel.loginTeacher(username, password, rememberLogin)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = !isLoading && username.isNotBlank() && password.isNotBlank()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("Đang đăng nhập...")
            } else {
                Text(
                    text = "Đăng nhập",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Additional options
        TextButton(
            onClick = { /* TODO: Implement forgot password */ }
        ) {
            Text("Quên mật khẩu?")
        }
    }
}
