package com.example.ckc_englihoo.Screen

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.ckc_englihoo.API.AppViewModel
import com.example.ckc_englihoo.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: AppViewModel = viewModel()
) {
    // Animation states
    var startAnimation by remember { mutableStateOf(false) }
    val alphaAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1000),
        label = "alpha"
    )
    val scaleAnim = animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0.3f,
        animationSpec = tween(durationMillis = 1000),
        label = "scale"
    )
    
    // ViewModel states
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    // Start animation and check login
    LaunchedEffect(Unit) {
        startAnimation = true
        
        // Check for saved login
        viewModel.checkSavedLogin()
        
        // Minimum splash duration
        delay(2000)
        
        // Navigate based on login status
        if (isLoggedIn) {
            navController.navigate("main") {
                popUpTo("splash") { inclusive = true }
            }
        } else {
            navController.navigate("login") {
                popUpTo("splash") { inclusive = true }
            }
        }
    }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.primary),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo with animation
            Image(
                painter = painterResource(id = R.drawable.group),
                contentDescription = "App Logo",
                modifier = Modifier
                    .size(120.dp)
                    .scale(scaleAnim.value)
                    .alpha(alphaAnim.value)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // App name with animation
            Text(
                text = "CKC Englishoo",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.alpha(alphaAnim.value)
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Học tiếng Anh hiệu quả",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.8f),
                modifier = Modifier.alpha(alphaAnim.value)
            )
            
            Spacer(modifier = Modifier.height(48.dp))
            
            // Loading indicator
            if (isLoading) {
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 3.dp,
                    modifier = Modifier
                        .size(32.dp)
                        .alpha(alphaAnim.value)
                )
            } else {
                // Dots loading animation
                DotsLoadingAnimation(
                    modifier = Modifier.alpha(alphaAnim.value)
                )
            }
        }
    }
}

@Composable
fun DotsLoadingAnimation(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "dots")
    
    val alpha1 by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha1"
    )
    
    val alpha2 by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, delayMillis = 200),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha2"
    )
    
    val alpha3 by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, delayMillis = 400),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha3"
    )
    
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        repeat(3) { index ->
            val alpha = when (index) {
                0 -> alpha1
                1 -> alpha2
                else -> alpha3
            }
            
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(
                        Color.White.copy(alpha = alpha),
                        shape = androidx.compose.foundation.shape.CircleShape
                    )
            )
        }
    }
}
