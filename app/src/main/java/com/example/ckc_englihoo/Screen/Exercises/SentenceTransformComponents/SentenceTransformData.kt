package com.example.ckc_englihoo.Screen.Exercises.SentenceTransformComponents

import androidx.compose.ui.graphics.Color

// Data class cho câu hỏi đảo ngữ
data class SentenceTransformQuestion(
    val id: Int,
    val imageUrl: String,
    val correctAnswer: String,
    val letters: List<String>,
    val explanation: String
)

// Sample data đơn giản cho 10 câu hỏi
val sampleSentenceTransformQuestions = listOf(
    SentenceTransformQuestion(
        id = 1,
        imageUrl = "https://cdn.mediamart.vn/images/news/nhung-thoi-quen-can-loai-bo-trong-thoi-tiet-nong-nuc_41ecb4e0.png",
        correctAnswer = "hot",
        letters = listOf("h", "o", "t"),
        explanation = "Hot nghĩa là nóng - trạng thái có nhiệt độ cao. Từ này được sử dụng để mô tả thời tiết, đồ vật, hoặc cảm giác khi nhiệt độ cao. Ví dụ: 'It's hot today' (Hôm nay trời nóng), 'The coffee is hot' (Cà phê nóng), 'I feel hot' (Tôi cảm thấy nóng). Từ này cũng có thể được sử dụng trong nhiều ngữ cảnh khác như 'hot news' (tin nóng), 'hot topic' (chủ đề nóng). Đây là một tính từ cơ bản và quan trọng trong tiếng Anh."
    ),
    SentenceTransformQuestion(
        id = 2,
        imageUrl = "https://i.pinimg.com/originals/09/74/72/097472efee176075f11c88a8a8d4c49e.jpg",
        correctAnswer = "dog",
        letters = listOf("d", "o", "g"),
        explanation = "Dog là con chó - bạn thân thiết của con người. Chó là loài động vật được thuần hóa từ rất lâu và được coi là người bạn đồng hành trung thành nhất của con người. Chúng có thể được huấn luyện để làm nhiều công việc khác nhau như canh gác, dẫn đường cho người khiếm thị, tìm kiếm cứu nạn, và làm thú cưng. Từ 'dog' trong tiếng Anh có thể được sử dụng trong nhiều cụm từ như 'hot dog' (bánh mì kẹp xúc xích), 'dog days' (những ngày nóng nhất trong năm)."
    ),
    SentenceTransformQuestion(
        id = 3,
        imageUrl = "https://tse2.mm.bing.net/th?id=OIP.g83R7mBiMfzvOABlsHtZ-wHaHw&pid=Api&P=0&h=220",
        correctAnswer = "sun",
        letters = listOf("s", "u", "n"),
        explanation = "Sun là mặt trời - nguồn ánh sáng và năng lượng."
    ),
    SentenceTransformQuestion(
        id = 4,
        imageUrl = "https://tse2.mm.bing.net/th?id=OIP.h0qeR3xlqRRTEqIVYId9vgHaFG&pid=Api&P=0&h=220",
        correctAnswer = "book",
        letters = listOf("b", "o", "o", "k"),
        explanation = "Book là cuốn sách - nguồn tri thức quan trọng."
    ),
    SentenceTransformQuestion(
        id = 5,
        imageUrl = "https://images.unsplash.com/photo-1441974231531-c6227db76b6e?w=400&h=400&fit=crop",
        correctAnswer = "tree",
        letters = listOf("t", "r", "e", "e"),
        explanation = "Tree là cây cối - cung cấp oxy cho môi trường."
    ),
    SentenceTransformQuestion(
        id = 6,
        imageUrl = "https://tse1.mm.bing.net/th?id=OIP.cXq_BQubeI_x6m4Be0LnCgHaE8&pid=Api&P=0&h=220",
            correctAnswer = "water",
        letters = listOf("w", "a", "t", "e", "r"),
        explanation = "Water là nước - cần thiết cho sự sống."
    ),
    SentenceTransformQuestion(
        id = 7,
        imageUrl = "https://i.pinimg.com/originals/14/51/c7/1451c7cb1a29c76c73beb6e24e794e67.jpg",
        correctAnswer = "house",
        letters = listOf("h", "o", "u", "s", "e"),
        explanation = "House là ngôi nhà - nơi ở của con người."
    ),
    SentenceTransformQuestion(
        id = 8,
        imageUrl = "https://img.thuthuatphanmem.vn/uploads/2018/09/28/anh-hoa-sen-hong-dep-2_024512682.jpg",
        correctAnswer = "flower",
        letters = listOf("f", "l", "o", "w", "e", "r"),
        explanation = "Flower là bông hoa - biểu tượng của vẻ đẹp."
    ),
    SentenceTransformQuestion(
        id = 9,
        imageUrl = "https://images.unsplash.com/photo-1560806887-1e4cd0b6cbd6?w=400&h=400&fit=crop",
        correctAnswer = "apple",
        letters = listOf("a", "p", "p", "l", "e"),
        explanation = "Apple là quả táo - trái cây bổ dưỡng."
    ),
    SentenceTransformQuestion(
        id = 10,
        imageUrl = "https://petdemy.com/wp-content/uploads/2023/06/Black-Bird-With-Orange-Stripe-On-Wings.jpg",
        correctAnswer = "bird",
        letters = listOf("b", "i", "r", "d"),
        explanation = "Bird là con chim - loài động vật biết bay.Bird là con chim - loài động vật biết bay.Bird là con chim - loài động vật biết bay.Bird là con chim - loài động vật biết bay."
    )
)

// Colors cho UI
object SentenceTransformColors {
    val Primary = Color(0xFF2196F3)
    val Secondary = Color(0xFFBBDEFB)
    val Background = Color(0xFFE3F2FD)
    val Surface = Color.White
    val Correct = Color(0xFF4CAF50)
    val Incorrect = Color(0xFFF44336)
    val Text = Color(0xFF1A1A1A)
    val TextSecondary = Color(0xFF666666)

    // Letter colors - tông xanh dương nhạt
    val LetterColors = listOf(
        Color(0xFF2196F3), // Blue
        Color(0xFF4CAF50), // Green
        Color(0xFFFF9800), // Orange
        Color(0xFFF44336), // Red
        Color(0xFF9C27B0), // Purple
        Color(0xFF00BCD4)  // Cyan
    )
}
