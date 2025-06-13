package com.example.ckc_englihoo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ckc_englihoo.R

enum class UserType {
    ADMIN,
    TEACHER,
    STUDENT
}

@Composable
fun LoginForm(
    userType: UserType = UserType.TEACHER,
    onLoginClick: (String, String) -> Unit = { _, _ -> },
    onForgotPasswordClick: () -> Unit = {},
    onBackClick: () -> Unit = {}
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    // Xác định tiêu đề dựa vào userType
    val loginTitle = when(userType) {
        UserType.ADMIN -> "Admin đăng nhập"
        UserType.TEACHER -> "Giảng viên đăng nhập"
        UserType.STUDENT -> "Sinh viên đăng nhập"
    }
    Box(modifier = Modifier.fillMaxSize()) {
        // Nút Back ở góc trên bên trái
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 35.dp, start = 10.dp)
                .size(48.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = Color(0xFF4355EE),
                modifier = Modifier.size(28.dp)
            )
        }
        
        // Nội dung chính
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(55.dp))

            // Logo ở đầu với hiệu ứng shadow
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 8.dp)
                ) {
                    // Logo ở bên trái
                    Image(
                        painter = painterResource(id = R.drawable.logocaothang),
                        contentDescription = "Logo",
                        modifier = Modifier
                            .size(48.dp)
                            .padding(end = 12.dp)
                    )
                    
                    // Cột chứa 2 dòng tiêu đề bên phải
                    Column {
                        // Tiêu đề dòng 1
                        Text(
                            text = "TRƯỜNG CĐ KỸ THUẬT CAO THẮNG",
                            color = Color(0xFF0066CC),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        
                        // Tiêu đề dòng 2
                        Text(
                            text = "TRUNG TÂM NGOẠI NGỮ",
                            color = Color(0xFFFF0000),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            // Card chứa form đăng nhập
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = Color(0xFFBBDEFB), // Màu xám trung tính nhẹ
                        shape = RoundedCornerShape(16.dp)
                    ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF0F0F0) // Màu xám nhạt
                ),
                shape = RoundedCornerShape(16.dp)
            )
            {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                ) {
                    // Tiêu đề Login - đã thay đổi theo userType
                    Text(
                        text = loginTitle,
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Text(
                        text = "Đăng nhập bằng tên người dùng và mật khẩu được cung cấp.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray,
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    // Label cho field tài khoản
                    Text(
                        text = "Tên Đăng Nhập",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = Color.DarkGray
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Trường nhập tài khoản với icon
                    OutlinedTextField(
                        value = username,
                        onValueChange = { username = it },
                        placeholder = { Text("Nhập tên đăng nhập") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF4355EE),
                            unfocusedBorderColor = Color.LightGray
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Username Icon",
                                tint = Color(0xFF4355EE)
                            )
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Label cho field mật khẩu
                    Text(
                        text = "Mật Khẩu",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = Color.DarkGray
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Trường nhập mật khẩu với icon
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = { Text("Nhập mật khẩu") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF4355EE),
                            unfocusedBorderColor = Color.LightGray
                        ),
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = "Password Icon",
                                tint = Color(0xFF4355EE)
                            )
                        },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    painter = painterResource(id = if (passwordVisible) R.drawable.eyehien else R.drawable.eyean),
                                    contentDescription = if (passwordVisible) "Ẩn mật khẩu" else "Hiện mật khẩu",
                                    tint = Color.Gray
                                )
                            }
                        }
                    )

                    // Text quên mật khẩu với icon
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.CenterEnd
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { onForgotPasswordClick() }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Quên mật khẩu Icon",
                                tint = Color.Red,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Nếu quên mật khẩu? Xin vui lòng liên hệ Mr.Hiền",
                                color = Color.Red,
                                fontSize = 14.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Nút đăng nhập với hiệu ứng shadow
                    Button(
                        onClick = { onLoginClick(username, password) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF4355EE)
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 6.dp,
                            pressedElevation = 8.dp
                        )
                    ) {
                        Text(
                            text = "Login",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            Icons.Filled.KeyboardArrowRight,
                            contentDescription = "Arrow Right"
                        )
                    }
                }
            }
        }
    }
}
