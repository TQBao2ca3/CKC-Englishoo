import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
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
import com.example.ckc_englihoo.R
@Composable
fun LoginForm(
    onLoginClick: (String, String) -> Unit = { _, _ -> },
    onForgotPasswordClick: () -> Unit = {}
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // Logo ở đầu
        Row(
            modifier = Modifier.padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.group),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(30.dp)
                    .padding(end = 8.dp),
                colorFilter = ColorFilter.tint(Color.Blue)
            )
            Text(
                text = "CKC ENGLISHOO",
                color = Color(0xFF4355EE),
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Tiêu đề Login
        Text(
            text = "Login",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.align(Alignment.Start)
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Login with your phone number provided while registration",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(32.dp))
        
        // Label cho field tài khoản
        Text(
            text = "Number Phone",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.align(Alignment.Start),
            color = Color.DarkGray
        )
        
        Spacer(modifier = Modifier.height(8.dp))

        // Trường nhập tài khoản
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            placeholder = { Text("Input account") },
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
            )
        )

        Spacer(modifier = Modifier.height(16.dp))
        
        // Label cho field mật khẩu
        Text(
            text = "Password",
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.align(Alignment.Start),
            color = Color.DarkGray
        )
        
        Spacer(modifier = Modifier.height(8.dp))

        // Trường nhập mật khẩu
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("Input password") },
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

        // Text quên mật khẩu
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = "Forgot Password?",
                color = Color.Red,
                modifier = Modifier.clickable { onForgotPasswordClick() },
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Nút đăng nhập
        Button(
            onClick = { onLoginClick(username, password) },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4355EE)
            )
        ) {
            Text(
                text = "Login",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
