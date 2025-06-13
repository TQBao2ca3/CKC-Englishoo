package com.example.ckc_englihoo

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Data classes cho điểm thi
data class BasicEnglishGrade(
    val stt: Int,
    val courseCode: String,
    val date: String,
    val ta1: String = "",
    val ta2: String = "",
    val ta3: String = ""
)

data class CertificationGrade(
    val level: String,
    val reading: String,
    val writing: String,
    val listening: String,
    val speaking: String,
    val result: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentGradesScreen(
    onBackClick: () -> Unit = {}
) {
    val primaryColor = Color(0xFF0066B3)
    val uriHandler = LocalUriHandler.current
    
    // Dữ liệu mẫu cho điểm Anh văn cơ bản
    val basicGrades = remember {
        listOf(
            BasicEnglishGrade(1, "ON12", "21/02/2022", ta2 = "6.6"),
            BasicEnglishGrade(2, "ON10", "08/11/2021", ta1 = "6.5")
        )
    }
    
    // Dữ liệu mẫu cho điểm chứng nhận
    val certificationGrades = remember {
        listOf(
            CertificationGrade("2/6", "5.8", "0", "6.4", "5.9", "Không đạt"),
            CertificationGrade("2/6", "5.6", "5", "5.2", "4.0", "Không đạt"),
            CertificationGrade("2/6", "4.6", "5", "4.4", "3", "Không đạt"),
            CertificationGrade("2/6", "4.8", "2.2", "3.2", "6.9", "Không đạt"),
            CertificationGrade("2/6", "4.6", "2.4", "4", "6.9", "Không đạt")
        )
    }

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
                            "Kết Quả Điểm Thi",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
                        
                        // Spacer để cân bằng bên phải (48dp)
                        Spacer(modifier = Modifier.width(80.dp))
                    }
                },
                navigationIcon = {
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = primaryColor
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color.White)
        ) {
            // Phần scroll được (bảng điểm)
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(bottom = 120.dp) // Để không bị che bởi phần cố định
            ) {
                // Header trường học (lấy từ CourseRegistrationScreen)
                SchoolHeader()

                // Thông tin sinh viên
                StudentInfoSection()

                // Bảng điểm Anh văn cơ bản
                BasicEnglishGradesSection(basicGrades)

                // Bảng điểm chứng nhận
                CertificationGradesSection(certificationGrades)
            }

            // Phần cố định ở dưới
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(top = 8.dp)
            ) {
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
    }
}

@Composable
fun StudentInfoSection() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0E0E0)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Họ & Tên: Vương Tâm",
                fontSize = 14.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Ngày Sinh: 06/07/2004",
                fontSize = 14.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = "Lớp: CĐ TH 22DĐ D",
                fontSize = 14.sp,
                color = Color.Black
            )
        }
    }
}

@Composable
fun BasicEnglishGradesSection(grades: List<BasicEnglishGrade>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Title
        Text(
            text = "Điểm Tổng Kết Anh Văn Cơ Bản A1, A2, A3.",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 12.dp)
        )
        
        // Bảng với scroll ngang
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(8.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0))
        ) {
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(8.dp)
            ) {
                Column {
                    // Header
                    BasicEnglishTableHeader()
                    
                    // Data rows
                    grades.forEach { grade ->
                        BasicEnglishTableRow(grade)
                    }
                }
            }
        }
    }
}

@Composable
fun BasicEnglishTableHeader() {
    Row(
        modifier = Modifier.background(Color(0xFF0066B3)),
        horizontalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        BasicGradeHeaderCell("TT", 50.dp)
        BasicGradeHeaderCell("Khóa", 80.dp)
        BasicGradeHeaderCell("Ngày", 100.dp)
        BasicGradeHeaderCell("TA1", 60.dp)
        BasicGradeHeaderCell("TA2", 60.dp)
        BasicGradeHeaderCell("TA3", 60.dp)
    }
}

@Composable
fun BasicGradeHeaderCell(text: String, width: Dp) {
    Box(
        modifier = Modifier
            .width(width)
            .height(48.dp)
            .background(Color(0xFF0066B3))
            .border(0.5.dp, Color.White)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun BasicEnglishTableRow(grade: BasicEnglishGrade) {
    Row(
        modifier = Modifier.background(Color.White),
        horizontalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        BasicGradeDataCell(grade.stt.toString(), 50.dp)
        BasicGradeDataCell(grade.courseCode, 80.dp)
        BasicGradeDataCell(grade.date, 100.dp)
        BasicGradeDataCell(grade.ta1, 60.dp)
        BasicGradeDataCell(grade.ta2, 60.dp)
        BasicGradeDataCell(grade.ta3, 60.dp)
    }
}

@Composable
fun BasicGradeDataCell(text: String, width: Dp) {
    Box(
        modifier = Modifier
            .width(width)
            .height(48.dp)
            .background(Color.White)
            .border(0.5.dp, Color(0xFFE0E0E0))
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.Black,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CertificationGradesSection(grades: List<CertificationGrade>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Title
        Text(
            text = "Điểm Tổng Kết Thi Chứng Nhận Tiếng Anh.",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        // Bảng với scroll ngang
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            shape = RoundedCornerShape(8.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE0E0E0))
        ) {
            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .padding(8.dp)
            ) {
                Column {
                    // Header
                    CertificationTableHeader()

                    // Data rows
                    grades.forEach { grade ->
                        CertificationTableRow(grade)
                    }
                }
            }
        }
    }
}

@Composable
fun CertificationTableHeader() {
    Row(
        modifier = Modifier.background(Color(0xFF0066B3)),
        horizontalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        CertificationHeaderCell("Trình Độ", 80.dp)
        CertificationHeaderCell("Đọc", 60.dp)
        CertificationHeaderCell("Viết", 60.dp)
        CertificationHeaderCell("Nghe", 60.dp)
        CertificationHeaderCell("Nói", 60.dp)
        CertificationHeaderCell("Kết Quả", 100.dp)
    }
}

@Composable
fun CertificationHeaderCell(text: String, width: Dp) {
    Box(
        modifier = Modifier
            .width(width)
            .height(48.dp)
            .background(Color(0xFF0066B3))
            .border(0.5.dp, Color.White)
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CertificationTableRow(grade: CertificationGrade) {
    Row(
        modifier = Modifier.background(Color.White),
        horizontalArrangement = Arrangement.spacedBy(0.dp)
    ) {
        CertificationDataCell(grade.level, 80.dp)
        CertificationDataCell(grade.reading, 60.dp,)
        CertificationDataCell(grade.writing, 60.dp,)
        CertificationDataCell(grade.listening, 60.dp)
        CertificationDataCell(grade.speaking, 60.dp,)
        CertificationDataCell(grade.result, 100.dp)
    }
}

@Composable
fun CertificationDataCell(text: String, width: Dp, isHighlighted: Boolean = false) {
    Box(
        modifier = Modifier
            .width(width)
            .height(48.dp)
            .background(if (isHighlighted) Color(0xFFFFEB3B) else Color.White) // Màu vàng highlight
            .border(0.5.dp, Color(0xFFE0E0E0))
            .padding(8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (isHighlighted) Color.Red else Color.Black, // Màu đỏ cho số highlight
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            fontWeight = if (isHighlighted) FontWeight.Bold else FontWeight.Normal
        )
    }
}


