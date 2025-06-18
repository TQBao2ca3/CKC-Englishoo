package com.example.ckc_englihoo.API

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ckc_englihoo.DataClass.*
import com.example.ckc_englihoo.Utils.UserPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import android.util.Log

class AppViewModel(application: Application) : AndroidViewModel(application) {

    private val apiService = RetrofitClient.apiService
    private val userPreferences = UserPreferences(application)

    // ==================== UI STATE ====================
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    // ==================== AUTHENTICATION STATE ====================
    private val _currentStudent = MutableStateFlow<Student?>(null)
    val currentStudent: StateFlow<Student?> = _currentStudent.asStateFlow()

    private val _currentTeacher = MutableStateFlow<Teacher?>(null)
    val currentTeacher: StateFlow<Teacher?> = _currentTeacher.asStateFlow()

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    // ==================== COURSE STATE ====================
    private val _courses = MutableStateFlow<List<Course>>(emptyList())
    val courses: StateFlow<List<Course>> = _courses.asStateFlow()

    private val _studentCourses = MutableStateFlow<List<Course>>(emptyList())
    val studentCourses: StateFlow<List<Course>> = _studentCourses.asStateFlow()

    private val _courseEnrollments = MutableStateFlow<List<CourseEnrollment>>(emptyList())
    val courseEnrollments: StateFlow<List<CourseEnrollment>> = _courseEnrollments.asStateFlow()

    private val _courseStudentCounts = MutableStateFlow<Map<Int, CourseStudentCount>>(emptyMap())
    val courseStudentCounts: StateFlow<Map<Int, CourseStudentCount>> = _courseStudentCounts.asStateFlow()

    // ==================== LESSON STATE ====================
    private val _lessons = MutableStateFlow<List<Lesson>>(emptyList())
    val lessons: StateFlow<List<Lesson>> = _lessons.asStateFlow()

    private val _lessonParts = MutableStateFlow<List<LessonPart>>(emptyList())
    val lessonParts: StateFlow<List<LessonPart>> = _lessonParts.asStateFlow()

    private val _lessonPartContents = MutableStateFlow<List<LessonPartContent>>(emptyList())
    val lessonPartContents: StateFlow<List<LessonPartContent>> = _lessonPartContents.asStateFlow()

    // ==================== SCORE & PROGRESS STATE ====================
    private val _lessonPartScores = MutableStateFlow<List<LessonPartScore>>(emptyList())
    val lessonPartScores: StateFlow<List<LessonPartScore>> = _lessonPartScores.asStateFlow()

    private val _studentProgress = MutableStateFlow<List<StudentProgress>>(emptyList())
    val studentProgress: StateFlow<List<StudentProgress>> = _studentProgress.asStateFlow()

    private val _examResults = MutableStateFlow<List<ExamResult>>(emptyList())
    val examResults: StateFlow<List<ExamResult>> = _examResults.asStateFlow()

    // ==================== ASSIGNMENT STATE ====================
    private val _assignments = MutableStateFlow<List<Assignment>>(emptyList())
    val assignments: StateFlow<List<Assignment>> = _assignments.asStateFlow()

    private val _questions = MutableStateFlow<List<Question>>(emptyList())
    val questions: StateFlow<List<Question>> = _questions.asStateFlow()

    private val _studentAnswers = MutableStateFlow<List<StudentAnswer>>(emptyList())
    val studentAnswers: StateFlow<List<StudentAnswer>> = _studentAnswers.asStateFlow()

    // ==================== COMMUNICATION STATE ====================
    private val _classPosts = MutableStateFlow<List<ClassPost>>(emptyList())
    val classPosts: StateFlow<List<ClassPost>> = _classPosts.asStateFlow()

    private val _answers = MutableStateFlow<List<Answer>>(emptyList())
    val answers: StateFlow<List<Answer>> = _answers.asStateFlow()

    private val _classPostReplies = MutableStateFlow<List<ClassPostComment>>(emptyList())
    val classPostReplies: StateFlow<List<ClassPostComment>> = _classPostReplies.asStateFlow()

    // ==================== NOTIFICATION STATE ====================
    private val _notifications = MutableStateFlow<List<Notification>>(emptyList())
    val notifications: StateFlow<List<Notification>> = _notifications.asStateFlow()

    // ==================== TEACHER STATE ====================
    private val _teachers = MutableStateFlow<List<Teacher>>(emptyList())
    val teachers: StateFlow<List<Teacher>> = _teachers.asStateFlow()

    // ==================== AUTHENTICATION METHODS ====================
    fun loginStudent(username: String, password: String, rememberLogin: Boolean = true) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = null

                val response = apiService.kiemTraDangNhap(username, password)
                if (response.isSuccessful) {
                    val students = response.body()
                    if (!students.isNullOrEmpty()) {
                        val student = students.first()
                        _currentStudent.value = student
                        _isLoggedIn.value = true

                        // Lưu thông tin đăng nhập
                        userPreferences.saveStudentLogin(student, username, password, rememberLogin)

                        Log.d("AppViewModel", "Student login successful: ${student.fullname}")
                    } else {
                        _errorMessage.value = "Tài khoản hoặc mật khẩu không đúng"
                    }
                } else {
                    _errorMessage.value = "Lỗi đăng nhập: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Lỗi kết nối: ${e.message}"
                Log.e("AppViewModel", "Login error", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loginTeacher(username: String, password: String, rememberLogin: Boolean = true) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _errorMessage.value = null

                val response = apiService.kiemTraDangNhapTeacher(username, password)
                if (response.isSuccessful) {
                    val teachers = response.body()
                    if (!teachers.isNullOrEmpty()) {
                        val teacher = teachers.first()
                        _currentTeacher.value = teacher
                        _isLoggedIn.value = true

                        // Lưu thông tin đăng nhập
                        userPreferences.saveTeacherLogin(teacher, username, password, rememberLogin)

                        Log.d("AppViewModel", "Teacher login successful: ${teacher.fullname}")
                    } else {
                        _errorMessage.value = "Tài khoản hoặc mật khẩu không đúng"
                    }
                } else {
                    _errorMessage.value = "Lỗi đăng nhập: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Lỗi kết nối: ${e.message}"
                Log.e("AppViewModel", "Teacher login error", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun logout() {
        // Xóa thông tin đăng nhập khỏi SharedPreferences
        userPreferences.logout()

        // Clear ViewModel states
        _currentStudent.value = null
        _currentTeacher.value = null
        _isLoggedIn.value = false
        clearAllData()

        Log.d("AppViewModel", "User logged out successfully")
    }

    // ==================== AUTO LOGIN METHODS ====================

    /**
     * Kiểm tra và khôi phục đăng nhập từ SharedPreferences
     */
    fun checkSavedLogin() {
        viewModelScope.launch {
            try {
                if (userPreferences.isLoggedIn()) {
                    val userType = userPreferences.getUserType()

                    when (userType) {
                        UserPreferences.USER_TYPE_STUDENT -> {
                            val savedStudent = userPreferences.getSavedStudent()
                            if (savedStudent != null) {
                                _currentStudent.value = savedStudent
                                _isLoggedIn.value = true
                                Log.d("AppViewModel", "Student auto-login successful: ${savedStudent.fullname}")
                            } else {
                                userPreferences.logout() // Clear invalid data
                            }
                        }
                        UserPreferences.USER_TYPE_TEACHER -> {
                            val savedTeacher = userPreferences.getSavedTeacher()
                            if (savedTeacher != null) {
                                _currentTeacher.value = savedTeacher
                                _isLoggedIn.value = true
                                Log.d("AppViewModel", "Teacher auto-login successful: ${savedTeacher.fullname}")
                            } else {
                                userPreferences.logout() // Clear invalid data
                            }
                        }
                        else -> {
                            userPreferences.logout() // Clear invalid data
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("AppViewModel", "Auto-login error", e)
                userPreferences.logout() // Clear invalid data on error
            }
        }
    }

    /**
     * Thử đăng nhập tự động với thông tin đã lưu
     */
    fun attemptAutoLogin() {
        viewModelScope.launch {
            try {
                val autoLoginData = userPreferences.getAutoLoginData()
                if (autoLoginData != null) {
                    _isLoading.value = true

                    when (autoLoginData.userType) {
                        UserPreferences.USER_TYPE_STUDENT -> {
                            loginStudent(autoLoginData.username, autoLoginData.password, true)
                        }
                        UserPreferences.USER_TYPE_TEACHER -> {
                            loginTeacher(autoLoginData.username, autoLoginData.password, true)
                        }
                    }
                } else {
                    // Chỉ khôi phục thông tin đã lưu mà không gọi API
                    checkSavedLogin()
                }
            } catch (e: Exception) {
                Log.e("AppViewModel", "Auto-login attempt error", e)
                userPreferences.logout()
            }
        }
    }

    /**
     * Lấy username đã lưu để hiển thị trong form login
     */
    fun getSavedUsername(): String? {
        return userPreferences.getSavedUsername()
    }

    /**
     * Lấy password đã lưu (nếu remember login = true)
     */
    fun getSavedPassword(): String? {
        return userPreferences.getSavedPassword()
    }

    /**
     * Kiểm tra có remember login không
     */
    fun isRememberLogin(): Boolean {
        return userPreferences.isRememberLogin()
    }

    private fun clearAllData() {
        _courses.value = emptyList()
        _studentCourses.value = emptyList()
        _courseEnrollments.value = emptyList()
        _courseStudentCounts.value = emptyMap()
        _lessons.value = emptyList()
        _lessonParts.value = emptyList()
        _lessonPartContents.value = emptyList()
        _lessonPartScores.value = emptyList()
        _studentProgress.value = emptyList()
        _examResults.value = emptyList()
        _assignments.value = emptyList()
        _questions.value = emptyList()
        _studentAnswers.value = emptyList()
        _classPosts.value = emptyList()
        _answers.value = emptyList()
        _classPostReplies.value = emptyList()
        _notifications.value = emptyList()
        _teachers.value = emptyList()
    }

    // ==================== COURSE METHODS ====================
    fun loadAllCourses() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = apiService.getAllCourses()
                if (response.isSuccessful) {
                    _courses.value = response.body() ?: emptyList()
                    Log.d("AppViewModel", "Loaded ${_courses.value.size} courses")
                } else {
                    _errorMessage.value = "Lỗi tải khóa học: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Lỗi kết nối: ${e.message}"
                Log.e("AppViewModel", "Load courses error", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadStudentCourses(studentId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = apiService.getCoursesByStudentId(studentId)
                if (response.isSuccessful) {
                    _studentCourses.value = response.body() ?: emptyList()
                    Log.d("AppViewModel", "Loaded ${_studentCourses.value.size} student courses")
                } else {
                    _errorMessage.value = "Lỗi tải khóa học của học sinh: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Lỗi kết nối: ${e.message}"
                Log.e("AppViewModel", "Load student courses error", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadCoursesByLevel(level: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = apiService.getCoursesByLevel(level)
                if (response.isSuccessful) {
                    _courses.value = response.body() ?: emptyList()
                    Log.d("AppViewModel", "Loaded ${_courses.value.size} courses for level $level")
                } else {
                    _errorMessage.value = "Lỗi tải khóa học theo cấp độ: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Lỗi kết nối: ${e.message}"
                Log.e("AppViewModel", "Load courses by level error", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadCourseStudentCount(courseId: Int) {
        viewModelScope.launch {
            try {
                val response = apiService.getCourseStudentCount(courseId)
                if (response.isSuccessful) {
                    val studentCount = response.body()
                    if (studentCount != null) {
                        val currentCounts = _courseStudentCounts.value.toMutableMap()
                        currentCounts[courseId] = studentCount
                        _courseStudentCounts.value = currentCounts
                        Log.d("AppViewModel", "Loaded student count for course $courseId: ${studentCount.total_students}")
                    }
                } else {
                    Log.e("AppViewModel", "Error loading student count for course $courseId: ${response.code()}")
                }
            } catch (e: Exception) {
                Log.e("AppViewModel", "Load course student count error", e)
            }
        }
    }

    fun loadMultipleCourseStudentCounts(courseIds: List<Int>) {
        courseIds.forEach { courseId ->
            loadCourseStudentCount(courseId)
        }
    }

    // ==================== ENROLLMENT METHODS ====================
    fun loadStudentEnrollments(studentId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = apiService.getEnrollmentsByStudentId(studentId)
                if (response.isSuccessful) {
                    _courseEnrollments.value = response.body() ?: emptyList()
                    Log.d("AppViewModel", "Loaded ${_courseEnrollments.value.size} enrollments")
                } else {
                    _errorMessage.value = "Lỗi tải đăng ký khóa học: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Lỗi kết nối: ${e.message}"
                Log.e("AppViewModel", "Load enrollments error", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun enrollStudentInCourse(enrollment: CourseEnrollment) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = apiService.enrollStudent(enrollment)
                if (response.isSuccessful) {
                    // Refresh enrollments
                    loadStudentEnrollments(enrollment.student_id)
                    Log.d("AppViewModel", "Student enrolled successfully")
                } else {
                    _errorMessage.value = "Lỗi đăng ký khóa học: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Lỗi kết nối: ${e.message}"
                Log.e("AppViewModel", "Enroll student error", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    // ==================== LESSON METHODS ====================
    fun loadLessonsByCourse(courseId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = apiService.getLessonsByCourseId(courseId)
                if (response.isSuccessful) {
                    _lessons.value = response.body() ?: emptyList()
                    Log.d("AppViewModel", "Loaded ${_lessons.value.size} lessons for course $courseId")
                } else {
                    _errorMessage.value = "Lỗi tải bài học: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Lỗi kết nối: ${e.message}"
                Log.e("AppViewModel", "Load lessons error", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadLessonPartsByLevel(level: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = apiService.getLessonPartsByLevel(level)
                if (response.isSuccessful) {
                    _lessonParts.value = response.body() ?: emptyList()
                    Log.d("AppViewModel", "Loaded ${_lessonParts.value.size} lesson parts for level $level")
                } else {
                    _errorMessage.value = "Lỗi tải phần bài học: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Lỗi kết nối: ${e.message}"
                Log.e("AppViewModel", "Load lesson parts error", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadLessonPartContents(lessonPartId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = apiService.getLessonPartContents(lessonPartId)
                if (response.isSuccessful) {
                    _lessonPartContents.value = response.body() ?: emptyList()
                    Log.d("AppViewModel", "Loaded ${_lessonPartContents.value.size} contents for lesson part $lessonPartId")
                } else {
                    _errorMessage.value = "Lỗi tải nội dung bài học: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Lỗi kết nối: ${e.message}"
                Log.e("AppViewModel", "Load lesson part contents error", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    // ==================== SCORE & PROGRESS METHODS ====================
    fun loadStudentScores(studentId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = apiService.getScoresByStudentId(studentId)
                if (response.isSuccessful) {
                    _lessonPartScores.value = response.body() ?: emptyList()
                    Log.d("AppViewModel", "Loaded ${_lessonPartScores.value.size} scores for student $studentId")
                } else {
                    _errorMessage.value = "Lỗi tải điểm số: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Lỗi kết nối: ${e.message}"
                Log.e("AppViewModel", "Load scores error", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun submitScore(score: LessonPartScore) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = apiService.submitScore(score)
                if (response.isSuccessful) {
                    // Refresh scores
                    loadStudentScores(score.student_id)
                    Log.d("AppViewModel", "Score submitted successfully")
                } else {
                    _errorMessage.value = "Lỗi nộp điểm: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Lỗi kết nối: ${e.message}"
                Log.e("AppViewModel", "Submit score error", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadStudentProgressData(studentId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                // Use new Progress API instead
                val response = apiService.getOverallProgress(studentId)
                if (response.isSuccessful) {
                    // Convert OverallProgress to simplified StudentProgress for compatibility
                    val overallProgress = response.body()?.data
                    if (overallProgress != null) {
                        // Create simplified progress records for UI compatibility
                        val progressRecords = listOf(
                            StudentProgress(
                                progressId = 1,
                                studentId = studentId,
                                courseId = 1, // Simplified
                                completionStatus = overallProgress.overall_progress_percentage >= 80,
                                lastUpdated = ""
                            )
                        )
                        _studentProgress.value = progressRecords
                        Log.d("AppViewModel", "Loaded overall progress: ${overallProgress.overall_progress_percentage}%")
                    }
                } else {
                    _errorMessage.value = "Lỗi tải tiến độ học tập: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Lỗi kết nối: ${e.message}"
                Log.e("AppViewModel", "Load progress error", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    // ==================== ASSIGNMENT METHODS ====================
    fun loadAssignmentsByCourse(courseId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = apiService.getAssignmentsByCourseId(courseId)
                if (response.isSuccessful) {
                    _assignments.value = response.body() ?: emptyList()
                    Log.d("AppViewModel", "Loaded ${_assignments.value.size} assignments for course $courseId")
                } else {
                    _errorMessage.value = "Lỗi tải bài tập: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Lỗi kết nối: ${e.message}"
                Log.e("AppViewModel", "Load assignments error", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadQuestionsByAssignment(assignmentId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = apiService.getQuestionsByAssignmentId(assignmentId)
                if (response.isSuccessful) {
                    _questions.value = response.body() ?: emptyList()
                    Log.d("AppViewModel", "Loaded ${_questions.value.size} questions for assignment $assignmentId")
                } else {
                    _errorMessage.value = "Lỗi tải câu hỏi: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Lỗi kết nối: ${e.message}"
                Log.e("AppViewModel", "Load questions error", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun submitAnswer(answer: StudentAnswer) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = apiService.submitAnswer(answer)
                if (response.isSuccessful) {
                    // Refresh answers
                    loadStudentAnswers(answer.student_id)
                    Log.d("AppViewModel", "Answer submitted successfully")
                } else {
                    _errorMessage.value = "Lỗi nộp câu trả lời: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Lỗi kết nối: ${e.message}"
                Log.e("AppViewModel", "Submit answer error", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadStudentAnswers(studentId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = apiService.getAnswersByStudentId(studentId)
                if (response.isSuccessful) {
                    _studentAnswers.value = response.body() ?: emptyList()
                    Log.d("AppViewModel", "Loaded ${_studentAnswers.value.size} answers for student $studentId")
                } else {
                    _errorMessage.value = "Lỗi tải câu trả lời: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Lỗi kết nối: ${e.message}"
                Log.e("AppViewModel", "Load answers error", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    // ==================== CLASS COMMUNICATION METHODS ====================
    fun loadClassPostsByCourse(courseId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = apiService.getClassPostsByCourseId(courseId)
                if (response.isSuccessful) {
                    _classPosts.value = response.body() ?: emptyList()
                    Log.d("AppViewModel", "Loaded ${_classPosts.value.size} class posts for course $courseId")
                } else {
                    _errorMessage.value = "Lỗi tải bài đăng lớp học: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Lỗi kết nối: ${e.message}"
                Log.e("AppViewModel", "Load class posts error", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createClassPost(post: ClassPost) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = apiService.createClassPost(post)
                if (response.isSuccessful) {
                    // Refresh class posts
                    loadClassPostsByCourse(post.course_id)
                    Log.d("AppViewModel", "Class post created successfully")
                } else {
                    _errorMessage.value = "Lỗi tạo bài đăng: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Lỗi kết nối: ${e.message}"
                Log.e("AppViewModel", "Create class post error", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadQuestionAnswers(questionId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = apiService.getAnswersByQuestionId(questionId)
                if (response.isSuccessful) {
                    _answers.value = response.body() ?: emptyList()
                    Log.d("AppViewModel", "Loaded ${_answers.value.size} answers for question $questionId")
                } else {
                    _errorMessage.value = "Lỗi tải câu trả lời: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Lỗi kết nối: ${e.message}"
                Log.e("AppViewModel", "Load question answers error", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createAnswer(answer: Answer) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = apiService.createAnswer(answer)
                if (response.isSuccessful) {
                    // Refresh answers for the question
                    loadQuestionAnswers(answer.questions_id)
                    Log.d("AppViewModel", "Answer created successfully")
                } else {
                    _errorMessage.value = "Lỗi tạo câu trả lời: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Lỗi kết nối: ${e.message}"
                Log.e("AppViewModel", "Create answer error", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadClassPostReplies(postId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = apiService.getClassPostReplies(postId)
                if (response.isSuccessful) {
                    _classPostReplies.value = response.body() ?: emptyList()
                    Log.d("AppViewModel", "Loaded ${_classPostReplies.value.size} replies for post $postId")
                } else {
                    _errorMessage.value = "Lỗi tải phản hồi bài đăng: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Lỗi kết nối: ${e.message}"
                Log.e("AppViewModel", "Load post replies error", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun createClassPostReply(reply: ClassPostComment) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = apiService.createClassPostReply(reply)
                if (response.isSuccessful) {
                    // Refresh replies
                    loadClassPostReplies(reply.post_id)
                    Log.d("AppViewModel", "Class post reply created successfully")
                } else {
                    _errorMessage.value = "Lỗi tạo phản hồi: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Lỗi kết nối: ${e.message}"
                Log.e("AppViewModel", "Create reply error", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    // ==================== NOTIFICATION METHODS ====================
    fun loadStudentNotifications(studentId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = apiService.getNotificationsByStudentId(studentId)
                if (response.isSuccessful) {
                    _notifications.value = response.body() ?: emptyList()
                    Log.d("AppViewModel", "Loaded ${_notifications.value.size} notifications for student $studentId")
                } else {
                    _errorMessage.value = "Lỗi tải thông báo: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Lỗi kết nối: ${e.message}"
                Log.e("AppViewModel", "Load notifications error", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun markNotificationAsRead(notificationId: Int) {
        viewModelScope.launch {
            try {
                val response = apiService.markNotificationAsRead(notificationId)
                if (response.isSuccessful) {
                    // Refresh notifications
                    _currentStudent.value?.let { student ->
                        loadStudentNotifications(student.student_id)
                    }
                    Log.d("AppViewModel", "Notification marked as read")
                } else {
                    _errorMessage.value = "Lỗi đánh dấu đã đọc: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Lỗi kết nối: ${e.message}"
                Log.e("AppViewModel", "Mark notification read error", e)
            }
        }
    }

    // ==================== EXAM & EVALUATION METHODS ====================
    fun loadStudentExamResults(studentId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = apiService.getExamResultsByStudentId(studentId)
                if (response.isSuccessful) {
                    _examResults.value = response.body() ?: emptyList()
                    Log.d("AppViewModel", "Loaded ${_examResults.value.size} exam results for student $studentId")
                } else {
                    _errorMessage.value = "Lỗi tải kết quả thi: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Lỗi kết nối: ${e.message}"
                Log.e("AppViewModel", "Load exam results error", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun submitExamResult(result: ExamResult) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = apiService.submitExamResult(result)
                if (response.isSuccessful) {
                    // Refresh exam results
                    loadStudentExamResults(result.student_id)
                    Log.d("AppViewModel", "Exam result submitted successfully")
                } else {
                    _errorMessage.value = "Lỗi nộp kết quả thi: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Lỗi kết nối: ${e.message}"
                Log.e("AppViewModel", "Submit exam result error", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    // ==================== TEACHER METHODS ====================
    fun loadAllTeachers() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = apiService.getAllTeachers()
                if (response.isSuccessful) {
                    _teachers.value = response.body() ?: emptyList()
                    Log.d("AppViewModel", "Loaded ${_teachers.value.size} teachers")
                } else {
                    _errorMessage.value = "Lỗi tải danh sách giáo viên: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Lỗi kết nối: ${e.message}"
                Log.e("AppViewModel", "Load teachers error", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadTeachersByCourse(courseId: Int) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = apiService.getTeachersByCourseId(courseId)
                if (response.isSuccessful) {
                    _teachers.value = response.body() ?: emptyList()
                    Log.d("AppViewModel", "Loaded ${_teachers.value.size} teachers for course $courseId")
                } else {
                    _errorMessage.value = "Lỗi tải giáo viên khóa học: ${response.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Lỗi kết nối: ${e.message}"
                Log.e("AppViewModel", "Load course teachers error", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    // ==================== UTILITY METHODS ====================
    fun clearError() {
        _errorMessage.value = null
    }

    fun refreshCurrentStudentData() {
        _currentStudent.value?.let { student ->
            loadStudentCourses(student.student_id)
            loadStudentEnrollments(student.student_id)
            loadStudentScores(student.student_id)
            loadStudentProgressData(student.student_id)
            loadStudentNotifications(student.student_id)
            loadStudentExamResults(student.student_id)
        }
    }

    fun refreshAllData() {
        loadAllCourses()
        loadAllTeachers()
        refreshCurrentStudentData()
    }
}