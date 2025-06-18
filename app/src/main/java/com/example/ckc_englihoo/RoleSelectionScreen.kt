package com.example.ckc_englihoo

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import com.example.ckc_englihoo.API.AppViewModel
import com.example.ckc_englihoo.Navigation.NavGraph
import com.example.ckc_englihoo.R
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// Định nghĩa màu chủ đạo của ứng dụng
val primaryColor = Color(0xFF4355EE)
val primaryColorButton = Color(0xFF4355EE) // màu xanh dương
val pressedColor = Color(0xFF63A4FF) // màu xanh dương nhạt
@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnboardingScreen(
    navController: NavController,
    viewModel: AppViewModel,
    onStudentClick: () -> Unit = {},
    onTeacherClick: () -> Unit = {}
) {
    var showSplash by remember { mutableStateOf(true) }
    var showRoleDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    // Ẩn splash screen sau 2 giây
    LaunchedEffect(key1 = true) {
        delay(2000)
        showSplash = false
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(
            visible = showSplash,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            // Màn hình Splash
            SplashScreen()
        }

        AnimatedVisibility(
            visible = !showSplash,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            // Màn hình Onboarding
            OnboardingContent(
                onLoginClick = { showRoleDialog = true }
            )
        }

        // Dialog chọn vai trò
        if (showRoleDialog) {
            RoleSelectionDialog(
                onDismiss = { showRoleDialog = false },
                onStudentClick = {
                    showRoleDialog = false
                    onStudentClick()
                },
                onTeacherClick = {
                    showRoleDialog = false
                    onTeacherClick()
                }
            )
        }
    }
}

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF4355EE)),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.group),
                contentDescription = "Logo",
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "CKC ENGLISHOO",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun OnboardingContent(
    onLoginClick: () -> Unit
) {
    val pages = listOf(
        OnboardingPage(
            image = R.drawable.image2,
            title = "Do your lessons online",
            description = "You can do your lessons and homework online and watch videos for lesson topics"
        ),
        OnboardingPage(
            image = R.drawable.image3,
            title = "A full library on your phone",
            description = "The app contains learning resources for all levels and you can use them whenever you want"
        ),
        OnboardingPage(
            image = R.drawable.image4,
            title = "Fulfill your vocabulary",
            description = "The app contains wide range of vocabulary which is categorized for different topics and themes"
        ),
        OnboardingPage(
            image = R.drawable.image5,
            title = "Events",
            description = "You can choose any event you are willing to attend and register for it. No in-person registration or paper-based booking is required."
        ),
        OnboardingPage(
            image = R.drawable.image6,
            title = "Ranking",
            description = "You can analyze your Ranking status among other students. You can see your ranking by your level, group, branch and whole education center."
        ),
        OnboardingPage(
            image = R.drawable.image7,
            title = "Store",
            description = "You collect coins during lessons while completing tasks. You can buy specific gifts and event vouchers for your coins."
        )
    )

    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo ở đầu
        Row(
            modifier = Modifier.padding(vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.group),
                contentDescription = "Logo",
                modifier = Modifier.size(30.dp).padding(end = 8.dp),
                colorFilter = ColorFilter.tint(Color.Blue),
            )
            Text(
                text = "CKC ENGLISHOO",
                color = Color(0xFF4355EE),
                fontSize = 18.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }

        Spacer(modifier = Modifier.weight(0.1f))

        // Sử dụng HorizontalPager để hỗ trợ vuốt
        HorizontalPager(
            count = pages.size,
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { page ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Hình ảnh minh họa
                Image(
                    painter = painterResource(id = pages[page].image),
                    contentDescription = "Onboarding Image",
                    modifier = Modifier
                        .size(280.dp)
                        .padding(16.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Tiêu đề
                Text(
                    text = pages[page].title,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Mô tả
                Text(
                    text = pages[page].description,
                    fontSize = 16.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Indicator dots và nút điều hướng
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Indicator dots
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(vertical = 16.dp)
            ) {
                repeat(pages.size) { index ->
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 6.dp)
                            .size(12.dp)
                            .background(
                                color = if (pagerState.currentPage == index) Color(0xFF4355EE) else Color.LightGray,
                                shape = CircleShape
                            )
                            .clickable {
                                scope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(0.1f))

        // Nút Login
        Button(
            onClick = onLoginClick,
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
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                Icons.Filled.KeyboardArrowRight,
                contentDescription = "Arrow Right"
            )
        }

        Spacer(modifier = Modifier.height(40.dp))
    }
}

@Composable
fun RoleSelectionDialog(
    onDismiss: () -> Unit,
    onStudentClick: () -> Unit,
    onTeacherClick: () -> Unit
) {
    val navController = androidx.navigation.compose.rememberNavController()
    var showRoleOptions by remember { mutableStateOf(true) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false // Cho phép tự chỉnh độ rộng
        )
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            )
        )
        {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Chọn vai trò của bạn",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = primaryColor
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Admin card
                RoleCard(
                    imageRes = R.drawable.admin,
                    title = "Quản trị viên",
                    description = "Quản lý người dùng và hệ thống",
                    onClick = {
                        onDismiss()
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Teacher card
                RoleCard(
                    imageRes = R.drawable.teacher,
                    title = "Giảng viên",
                    description = "Quản lý lớp học và giảng dạy",
                    onClick = {
                        onTeacherClick()
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))
                // Student card
                RoleCard(
                    imageRes = R.drawable.student,
                    title = "Sinh viên",
                    description = "Truy cập khóa học và bài tập",
                    onClick = {
                        onStudentClick()
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.End),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = primaryColorButton
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Hủy",
                        color = Color.White,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun RoleCard(
    @DrawableRes imageRes: Int,  // nhận vào drawable resource id
    title: String,
    description: String,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),

                    )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

//            Icon(
//                imageVector = Icons.Default.KeyboardArrowRight,
//                contentDescription = null,
//                modifier = Modifier.size(24.dp)
//            )
        }
    }
}


@Composable
fun RoleSelectionScreen(
    onLoginClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Logo ở đầu
        Row(
            modifier = Modifier.padding(vertical = 32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.group),
                contentDescription = "Logo",
                modifier = Modifier.size(40.dp).padding(end = 12.dp),
                colorFilter = ColorFilter.tint(Color(0xFF4355EE))
            )
            Text(
                text = "CKC ENGLISHOO",
                color = Color(0xFF4355EE),
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Tiêu đề
        Text(
            text = "Chọn vai trò của bạn",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Vui lòng chọn vai trò để tiếp tục sử dụng ứng dụng",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Role cards
        RoleCard(
            imageRes = R.drawable.admin,
            title = "Quản trị viên",
            description = "Quản lý người dùng và hệ thống",
            onClick = onLoginClick
        )

        Spacer(modifier = Modifier.height(16.dp))

        RoleCard(
            imageRes = R.drawable.teacher,
            title = "Giảng viên",
            description = "Quản lý lớp học và giảng dạy",
            onClick = onLoginClick
        )

        Spacer(modifier = Modifier.height(16.dp))

        RoleCard(
            imageRes = R.drawable.student,
            title = "Sinh viên",
            description = "Truy cập khóa học và bài tập",
            onClick = onLoginClick
        )

        Spacer(modifier = Modifier.weight(1f))
    }
}

data class OnboardingPage(
    val image: Int,
    val title: String,
    val description: String
)