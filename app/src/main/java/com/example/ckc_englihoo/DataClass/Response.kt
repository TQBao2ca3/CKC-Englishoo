package com.example.ckc_englihoo.DataClass

import java.util.Date

// API Response wrapper
data class ApiResponse<T>(
    val success: Boolean,
    val message: String,
    val data: T? = null,
    val error: String? = null
)

// Login Request data class
data class LoginRequest(
    val username: String,
    val password: String
)

// Login Response data class
data class LoginResponse(
    val success: Boolean,
    val message: String,
    val token: String? = null,
    val userType: UserType? = null,
    val userId: Int? = null,
    val expiresAt: Date? = null
)

// User Session data class
data class UserSession(
    val sessionId: String,
    val userId: Int,
    val userType: UserType,
    val token: String,
    val createdAt: Date,
    val expiresAt: Date,
    val isActive: Boolean = true
)

// Password Reset data class
data class PasswordReset(
    val resetId: Int,
    val userId: Int,
    val resetToken: String,
    val requestedAt: Date,
    val expiresAt: Date,
    val isUsed: Boolean = false
)

// Feedback data class
data class Feedback(
    val feedbackId: Int,
    val studentId: Int,
    val teacherId: Int,
    val courseId: Int,
    val feedbackText: String,
    val rating: Int, // 1-5 stars
    val createdAt: Date
)

// System Settings data class
data class SystemSettings(
    val settingId: Int,
    val settingKey: String,
    val settingValue: String,
    val description: String? = null,
    val updatedAt: Date
)
