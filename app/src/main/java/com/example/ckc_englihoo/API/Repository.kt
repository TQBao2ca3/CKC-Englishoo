package com.example.ckc_englihoo.API

import com.example.ckc_englihoo.DataClass.*
import retrofit2.Response
import android.util.Log

/**
 * Repository class để quản lý dữ liệu từ API
 * Cung cấp interface sạch cho ViewModel và xử lý logic business
 */
class AppRepository {
    
    private val apiService = RetrofitClient.apiService
    
    // ==================== AUTHENTICATION ====================
    suspend fun loginStudent(username: String, password: String): Result<Student> {
        return try {
            val response = apiService.kiemTraDangNhap(username, password)
            if (response.isSuccessful) {
                val students = response.body()
                if (!students.isNullOrEmpty()) {
                    Result.success(students.first())
                } else {
                    Result.failure(Exception("Tài khoản hoặc mật khẩu không đúng"))
                }
            } else {
                Result.failure(Exception("Lỗi đăng nhập: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e("AppRepository", "Login error", e)
            Result.failure(e)
        }
    }
    
    suspend fun loginTeacher(username: String, password: String): Result<Teacher> {
        return try {
            val response = apiService.kiemTraDangNhapTeacher(username, password)
            if (response.isSuccessful) {
                val teachers = response.body()
                if (!teachers.isNullOrEmpty()) {
                    Result.success(teachers.first())
                } else {
                    Result.failure(Exception("Tài khoản hoặc mật khẩu không đúng"))
                }
            } else {
                Result.failure(Exception("Lỗi đăng nhập: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e("AppRepository", "Teacher login error", e)
            Result.failure(e)
        }
    }
    
    // ==================== COURSE MANAGEMENT ====================
    suspend fun getAllCourses(): Result<List<Course>> {
        return handleApiCall { apiService.getAllCourses() }
    }
    
    suspend fun getCourseById(courseId: Int): Result<Course> {
        return handleApiCall { apiService.getCourseById(courseId) }
    }
    
    suspend fun getCoursesByStudentId(studentId: Int): Result<List<Course>> {
        return handleApiCall { apiService.getCoursesByStudentId(studentId) }
    }
    
    suspend fun getCoursesByLevel(level: String): Result<List<Course>> {
        return handleApiCall { apiService.getCoursesByLevel(level) }
    }

    suspend fun getCourseStudentCount(courseId: Int): Result<CourseStudentCount> {
        return handleApiCall { apiService.getCourseStudentCount(courseId) }
    }
    
    // ==================== ENROLLMENT MANAGEMENT ====================
    suspend fun getEnrollmentsByStudentId(studentId: Int): Result<List<CourseEnrollment>> {
        return handleApiCall { apiService.getEnrollmentsByStudentId(studentId) }
    }
    
    suspend fun enrollStudent(enrollment: CourseEnrollment): Result<CourseEnrollment> {
        return handleApiCall { apiService.enrollStudent(enrollment) }
    }
    
    suspend fun getEnrollmentsByCourseId(courseId: Int): Result<List<CourseEnrollment>> {
        return handleApiCall { apiService.getEnrollmentsByCourseId(courseId) }
    }
    
    // ==================== LESSON MANAGEMENT ====================
    suspend fun getLessonsByCourseId(courseId: Int): Result<List<Lesson>> {
        return handleApiCall { apiService.getLessonsByCourseId(courseId) }
    }
    
    suspend fun getLessonById(lessonId: Int): Result<Lesson> {
        return handleApiCall { apiService.getLessonById(lessonId) }
    }
    
    suspend fun getLessonPartsByLevel(level: String): Result<List<LessonPart>> {
        return handleApiCall { apiService.getLessonPartsByLevel(level) }
    }

    suspend fun getLessonPartContents(lessonPartId: Int): Result<List<LessonPartContent>> {
        return handleApiCall { apiService.getLessonPartContents(lessonPartId) }
    }
    
    // ==================== SCORE & PROGRESS ====================
    suspend fun getScoresByStudentId(studentId: Int): Result<List<LessonPartScore>> {
        return handleApiCall { apiService.getScoresByStudentId(studentId) }
    }
    
    suspend fun submitScore(score: LessonPartScore): Result<LessonPartScore> {
        return handleApiCall { apiService.submitScore(score) }
    }
    

    
    // Progress APIs - Updated to match backend
    suspend fun getLessonPartProgress(lessonPartId: Int, studentId: Int): Result<ProgressResponse<LessonPartProgress>> {
        return handleApiCall { apiService.getLessonPartProgress(lessonPartId, studentId) }
    }

    suspend fun getLessonProgress(level: String, studentId: Int): Result<ProgressResponse<LessonProgress>> {
        return handleApiCall { apiService.getLessonProgress(level, studentId) }
    }

    suspend fun getOverallProgress(studentId: Int): Result<ProgressResponse<OverallProgress>> {
        return handleApiCall { apiService.getOverallProgress(studentId) }
    }
    
    // ==================== ASSIGNMENTS & QUESTIONS ====================
    suspend fun getAssignmentsByCourseId(courseId: Int): Result<List<Assignment>> {
        return handleApiCall { apiService.getAssignmentsByCourseId(courseId) }
    }
    
    suspend fun getQuestionsByAssignmentId(assignmentId: Int): Result<List<Question>> {
        return handleApiCall { apiService.getQuestionsByAssignmentId(assignmentId) }
    }
    
    suspend fun submitAnswer(answer: StudentAnswer): Result<StudentAnswer> {
        return handleApiCall { apiService.submitAnswer(answer) }
    }
    
    suspend fun getAnswersByStudentId(studentId: Int): Result<List<StudentAnswer>> {
        return handleApiCall { apiService.getAnswersByStudentId(studentId) }
    }
    
    // ==================== CLASS COMMUNICATION ====================
    suspend fun getClassPostsByCourseId(courseId: Int): Result<List<ClassPost>> {
        return handleApiCall { apiService.getClassPostsByCourseId(courseId) }
    }
    
    suspend fun createClassPost(post: ClassPost): Result<ClassPost> {
        return handleApiCall { apiService.createClassPost(post) }
    }
    
    suspend fun getAnswersByQuestionId(questionId: Int): Result<List<Answer>> {
        return handleApiCall { apiService.getAnswersByQuestionId(questionId) }
    }

    suspend fun createAnswer(answer: Answer): Result<Answer> {
        return handleApiCall { apiService.createAnswer(answer) }
    }

    suspend fun getClassPostReplies(postId: Int): Result<List<ClassPostComment>> {
        return handleApiCall { apiService.getClassPostReplies(postId) }
    }

    suspend fun createClassPostReply(reply: ClassPostComment): Result<ClassPostComment> {
        return handleApiCall { apiService.createClassPostReply(reply) }
    }
    
    // ==================== NOTIFICATIONS ====================
    suspend fun getNotificationsByStudentId(studentId: Int): Result<List<Notification>> {
        return handleApiCall { apiService.getNotificationsByStudentId(studentId) }
    }
    
    suspend fun markNotificationAsRead(notificationId: Int): Result<Notification> {
        return handleApiCall { apiService.markNotificationAsRead(notificationId) }
    }
    
    // ==================== EXAM RESULTS ====================
    suspend fun getExamResultsByStudentId(studentId: Int): Result<List<ExamResult>> {
        return handleApiCall { apiService.getExamResultsByStudentId(studentId) }
    }
    
    suspend fun submitExamResult(result: ExamResult): Result<ExamResult> {
        return handleApiCall { apiService.submitExamResult(result) }
    }
    
    // ==================== TEACHERS ====================
    suspend fun getAllTeachers(): Result<List<Teacher>> {
        return handleApiCall { apiService.getAllTeachers() }
    }
    
    suspend fun getTeachersByCourseId(courseId: Int): Result<List<Teacher>> {
        return handleApiCall { apiService.getTeachersByCourseId(courseId) }
    }
    
    // ==================== UTILITY METHODS ====================
    private suspend fun <T> handleApiCall(apiCall: suspend () -> Response<T>): Result<T> {
        return try {
            val response = apiCall()
            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    Result.success(body)
                } else {
                    Result.failure(Exception("Dữ liệu trống"))
                }
            } else {
                Result.failure(Exception("Lỗi API: ${response.code()} - ${response.message()}"))
            }
        } catch (e: Exception) {
            Log.e("AppRepository", "API call error", e)
            Result.failure(e)
        }
    }
    
    // ==================== COMPLEX DATA OPERATIONS ====================
    suspend fun getStudentDashboardData(studentId: Int): Result<StudentDashboardData> {
        return try {
            val coursesResult = getCoursesByStudentId(studentId)
            val enrollmentsResult = getEnrollmentsByStudentId(studentId)
            val progressResult = getOverallProgress(studentId)
            val notificationsResult = getNotificationsByStudentId(studentId)
            val scoresResult = getScoresByStudentId(studentId)
            
            if (coursesResult.isSuccess && enrollmentsResult.isSuccess && 
                progressResult.isSuccess && notificationsResult.isSuccess && 
                scoresResult.isSuccess) {
                
                val dashboardData = StudentDashboardData(
                    courses = coursesResult.getOrNull() ?: emptyList(),
                    enrollments = enrollmentsResult.getOrNull() ?: emptyList(),
                    progress = progressResult.getOrNull()?.data?.let { overallProgress ->
                        // Convert OverallProgress to List<StudentProgress> for compatibility
                        listOf(
                            StudentProgress(
                                progressId = 1,
                                studentId = studentId,
                                courseId = 1, // Simplified
                                completionStatus = overallProgress.overall_progress_percentage >= 80,
                                lastUpdated = ""
                            )
                        )
                    } ?: emptyList(),
                    notifications = notificationsResult.getOrNull() ?: emptyList(),
                    scores = scoresResult.getOrNull() ?: emptyList()
                )
                Result.success(dashboardData)
            } else {
                Result.failure(Exception("Lỗi tải dữ liệu dashboard"))
            }
        } catch (e: Exception) {
            Log.e("AppRepository", "Dashboard data error", e)
            Result.failure(e)
        }
    }
    
    suspend fun getCourseDetailData(courseId: Int, studentId: Int): Result<CourseDetailData> {
        return try {
            val courseResult = getCourseById(courseId)
            val lessonsResult = getLessonsByCourseId(courseId)
            val teachersResult = getTeachersByCourseId(courseId)
            val assignmentsResult = getAssignmentsByCourseId(courseId)
            val progressResult = getOverallProgress(studentId)
            
            if (courseResult.isSuccess && lessonsResult.isSuccess && teachersResult.isSuccess) {
                val courseDetailData = CourseDetailData(
                    course = courseResult.getOrNull()!!,
                    lessons = lessonsResult.getOrNull() ?: emptyList(),
                    teachers = teachersResult.getOrNull() ?: emptyList(),
                    assignments = assignmentsResult.getOrNull() ?: emptyList(),
                    studentProgress = progressResult.getOrNull()?.data?.let { overallProgress ->
                        // Convert OverallProgress to StudentProgress for compatibility
                        StudentProgress(
                            progressId = 1,
                            studentId = studentId,
                            courseId = courseId,
                            completionStatus = overallProgress.overall_progress_percentage >= 80,
                            lastUpdated = ""
                        )
                    }
                )
                Result.success(courseDetailData)
            } else {
                Result.failure(Exception("Lỗi tải chi tiết khóa học"))
            }
        } catch (e: Exception) {
            Log.e("AppRepository", "Course detail error", e)
            Result.failure(e)
        }
    }
}

// ==================== DATA MODELS ====================
data class StudentDashboardData(
    val courses: List<Course>,
    val enrollments: List<CourseEnrollment>,
    val progress: List<StudentProgress>,
    val notifications: List<Notification>,
    val scores: List<LessonPartScore>
)

data class CourseDetailData(
    val course: Course,
    val lessons: List<Lesson>,
    val teachers: List<Teacher>,
    val assignments: List<Assignment>,
    val studentProgress: StudentProgress?
)
