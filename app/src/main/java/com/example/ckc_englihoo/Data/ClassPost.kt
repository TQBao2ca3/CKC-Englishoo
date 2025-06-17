package com.example.ckc_englihoo.Data

import com.example.ckc_englihoo.R

// Data classes for attachments
data class AttachmentItem(
    val id: String,
    val name: String,
    val type: AttachmentType,
    val url: String? = null,
    val size: String? = null
)

enum class AttachmentType {
    DRIVE, LINK, FILE, IMAGE, VIDEO, PDF
}

// Google Classroom style post
data class ClassPost(
    val id: String,
    val authorName: String,
    val authorAvatar: Int,
    val content: String,
    val timestamp: String,
    val comments: List<PostComment> = emptyList(),
    val attachments: List<AttachmentItem> = emptyList()
)

data class PostComment(
    val id: String,
    val authorName: String,
    val authorAvatar: Int,
    val content: String,
    val timestamp: String
)

// Legacy post for CreatePostTeacher
data class LegacyClassPost(
    val id: String,
    val title: String,
    val content: String,
    val type: PostType,
    val timestamp: String,
    val dueDate: String? = null,
    val attachments: List<String> = emptyList()
)

enum class PostType {
    ANNOUNCEMENT, ASSIGNMENT, MATERIAL
}

data class Student(
    val id: String,
    val name: String,
    val email: String,
    val avatar: Int = R.drawable.student,
    val invitationStatus: InvitationStatus = InvitationStatus.ACCEPTED,
    val isSelected: Boolean = false
)

enum class InvitationStatus {
    PENDING, // Đã được mời
    ACCEPTED, // Đã chấp nhận
    HIDDEN // Ẩn
}

// Assignment data for Classwork tab
data class Assignment(
    val id: String,
    val title: String,
    val description: String,
    val dueDate: String,
    val type: AssignmentType,
    val points: String?
)

enum class AssignmentType {
    ASSIGNMENT, MATERIAL, QUIZ
}
