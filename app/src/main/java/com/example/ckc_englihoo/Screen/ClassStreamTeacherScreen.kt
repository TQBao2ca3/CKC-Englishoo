package com.example.ckc_englihoo.Screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.ckc_englihoo.DataClass.*
import com.example.ckc_englihoo.Utils.formatTimeAgo
import java.util.Date

@Composable
fun ClassStreamTeacherScreen(
    navController: NavController,
    courseId: Int = 1,
    teacherId: Int = 1,
    classTitle: String = "Tiếng Anh 101",
    onCreatePostClick: () -> Unit = {} // Callback khi ấn nút tạo post
) {
    // Sample data for preview - In production, use ViewModel
    val classPosts = remember {
        listOf(
            ClassPostDisplayUI(
                classPostId = 1,
                teacherName = "Ngô Kim Phụng",
                teacherAvatar = null,
                time = "2 giờ trước",
                content = "Chào mừng các bạn đến với khóa học Tiếng Anh 101! Hôm nay chúng ta sẽ bắt đầu với bài học đầu tiên về giới thiệu bản thân.",
                courseName = classTitle
            ),
            ClassPostDisplayUI(
                classPostId = 2,
                teacherName = "Ngô Kim Phụng",
                teacherAvatar = null,
                time = "1 ngày trước",
                content = "Đã có thay đổi lịch thi cuối kỳ. Kỳ thi sẽ được dời từ ngày 15/12 sang ngày 20/12. Các bạn vui lòng kiểm tra email để biết thêm chi tiết.",
                courseName = classTitle
            )
        )
    }

    // Sample teacher data - In production, use ViewModel
    val currentTeacher = "Ngô Kim Phụng"

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            // Tiêu đề khóa học
            Text(
                text = classTitle,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            // Thông tin teacher hiện tại
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Avatar giáo viên",
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = currentTeacher,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "Giáo viên",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Danh sách bài đăng đã có
            Text(
                text = "Thông báo đã đăng (${classPosts.size})",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(classPosts) { post ->
                    TeacherClassPostCard(post = post)
                }
            }
        }

        // Floating Action Button để tạo post mới
        FloatingActionButton(
            onClick = onCreatePostClick,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Tạo thông báo mới",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun TeacherClassPostCard(post: ClassPostDisplayUI) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header với avatar và thông tin giáo viên
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Avatar giáo viên",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = post.teacherName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = post.time,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))
            
            // Nội dung bài đăng
            Text(
                text = post.content,
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = MaterialTheme.typography.bodyMedium.lineHeight
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Stats và actions
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.Comment,
                    contentDescription = "Bình luận",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Xem bình luận",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.weight(1f))
                
                // Edit/Delete buttons có thể thêm ở đây
                Text(
                    text = "Chỉnh sửa",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

