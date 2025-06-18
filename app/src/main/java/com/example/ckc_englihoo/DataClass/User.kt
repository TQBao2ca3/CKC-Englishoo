package com.example.ckc_englihoo.DataClass

import java.util.Date

// Student data class - Match hoàn toàn với API response
data class Student(
    val student_id: Int,
    val avatar: String = "",
    val fullname: String = "",
    val username: String = "",
    val password: String = "", // API trả về password hash
    val date_of_birth: String = "",
    val gender: Int = 0, // 0=Female, 1=Male
    val email: String = "",
    val is_status: Int = 1,
    val created_at: String = "",
    val updated_at: String = ""
)

// Teacher data class - Match với API response
data class Teacher(
    val teacher_id: Int,
    val fullname: String = "",
    val username: String = "",
    val password: String = "",
    val date_of_birth: String = "",
    val gender: Int = 0, // 0=Female, 1=Male
    val email: String = "",
    val is_status: Int = 1,
    val created_at: String = "",
    val updated_at: String = "",
    val courses: List<CourseWithPivot> = emptyList(), // Nested courses từ /api/teachers
    val assignments: List<TeacherAssignment> = emptyList() // Chỉ có trong single teacher API
)

// Teacher Course Assignment (pivot data)
data class TeacherAssignment(
    val assignment_id: Int,
    val teacher_id: Int,
    val course_id: Int,
    val role: String, // "Main Teacher" | "Assistant Teacher"
    val assigned_at: String,
    val created_at: String = "",
    val updated_at: String = ""
)

// Admin data class - Dựa trên bảng Admin trong database
data class Admin(
    val adminId: Int, // admin_id: Int
    val username: String, // username: String
    val email: String, // email: String
    val password: String // password: String
)
