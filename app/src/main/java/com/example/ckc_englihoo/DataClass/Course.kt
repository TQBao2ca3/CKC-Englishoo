package com.example.ckc_englihoo.DataClass

// Course data class - Match với API response
data class Course(
    val course_id: Int,
    val course_name: String = "",
    val level: String = "",
    val year: String = "",
    val description: String = "",
    val status: String = "", // "Đang mở lớp" | "Đã hoàn thành"
    val starts_date: String = "",
    val created_at: String = "",
    val updated_at: String = "",
    val lesson: Lesson? = null, // Nested lesson từ course APIs
    val teachers: List<TeacherWithPivot> = emptyList(), // Nested teachers với pivot
    val students: List<StudentWithPivot> = emptyList() // Chỉ có trong single course API
)

// Course với pivot data cho Teacher
data class CourseWithPivot(
    val course_id: Int,
    val course_name: String = "",
    val level: String = "",
    val year: String = "",
    val description: String = "",
    val status: String = "",
    val starts_date: String = "",
    val created_at: String = "",
    val updated_at: String = "",
    val pivot: TeacherCoursePivot
)

// Teacher với pivot data
data class TeacherWithPivot(
    val teacher_id: Int,
    val fullname: String = "",
    val username: String = "",
    val password: String = "",
    val date_of_birth: String = "",
    val gender: Int = 0,
    val email: String = "",
    val is_status: Int = 1,
    val created_at: String = "",
    val updated_at: String = "",
    val pivot: TeacherCoursePivot
)

// Student với pivot data
data class StudentWithPivot(
    val student_id: Int,
    val avatar: String = "",
    val fullname: String = "",
    val username: String = "",
    val password: String = "",
    val date_of_birth: String = "",
    val gender: Int = 0,
    val email: String = "",
    val is_status: Int = 1,
    val created_at: String = "",
    val updated_at: String = "",
    val pivot: StudentCoursePivot
)

// Teacher-Course pivot data
data class TeacherCoursePivot(
    val teacher_id: Int,
    val course_id: Int,
    val role: String, // "Main Teacher" | "Assistant Teacher"
    val assigned_at: String
)

// Student-Course pivot data
data class StudentCoursePivot(
    val assigned_course_id: Int,
    val student_id: Int
)

// Course Enrollment data class - Match với API response
data class CourseEnrollment(
    val enrollment_id: Int,
    val student_id: Int,
    val assigned_course_id: Int,
    val registration_date: String,
    val status: Int, // 0=Pending, 1=Active, 2=Completed
    val created_at: String = "",
    val updated_at: String = "",
    val course: Course? = null, // Nested course
    val student: Student? = null // Nested student
)

// Course Student Count data class - For API response
data class CourseStudentCount(
    val course_id: Int,
    val total_students: Int,
    val active_students: Int,
    val completed_students: Int
)
