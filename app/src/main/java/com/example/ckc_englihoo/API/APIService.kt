package com.example.ckc_englihoo.API

import com.example.ckc_englihoo.DataClass.*
import retrofit2.Response
import retrofit2.http.*

interface APIService {

    // ==================== AUTHENTICATION ====================
    @GET("StudentDN/{taikhoan}/{matkhau}")
    suspend fun kiemTraDangNhap(
        @Path("taikhoan") taikhoan: String,
        @Path("matkhau") matkhau: String
    ): Response<List<Student>>

    @GET("TeacherDN/{taikhoan}/{matkhau}")
    suspend fun kiemTraDangNhapTeacher(
        @Path("taikhoan") taikhoan: String,
        @Path("matkhau") matkhau: String
    ): Response<List<Teacher>>

    // ==================== STUDENT MANAGEMENT ====================
    @GET("students/{studentId}")
    suspend fun getStudentById(@Path("studentId") studentId: Int): Response<Student>

    @GET("students")
    suspend fun getAllStudents(): Response<List<Student>>

    @PUT("students/{studentId}")
    suspend fun updateStudent(
        @Path("studentId") studentId: Int,
        @Body student: Student
    ): Response<Student>

    // ==================== COURSE MANAGEMENT ====================
    @GET("courses")
    suspend fun getAllCourses(): Response<List<Course>>

    @GET("courses/{courseId}")
    suspend fun getCourseById(@Path("courseId") courseId: Int): Response<Course>

    @GET("courses/student/{studentId}")
    suspend fun getCoursesByStudentId(@Path("studentId") studentId: Int): Response<List<Course>>

    @GET("courses/level/{level}")
    suspend fun getCoursesByLevel(@Path("level") level: String): Response<List<Course>>

    @GET("courses/{courseId}/students/count")
    suspend fun getCourseStudentCount(@Path("courseId") courseId: Int): Response<CourseStudentCount>

    // ==================== COURSE ENROLLMENT ====================
    @GET("enrollments/student/{studentId}")
    suspend fun getEnrollmentsByStudentId(@Path("studentId") studentId: Int): Response<List<CourseEnrollment>>

    @POST("enrollments")
    suspend fun enrollStudent(@Body enrollment: CourseEnrollment): Response<CourseEnrollment>

    @GET("enrollments/course/{courseId}")
    suspend fun getEnrollmentsByCourseId(@Path("courseId") courseId: Int): Response<List<CourseEnrollment>>

    // ==================== TEACHER MANAGEMENT ====================
    @GET("teachers")
    suspend fun getAllTeachers(): Response<List<Teacher>>

    @GET("teachers/{teacherId}")
    suspend fun getTeacherById(@Path("teacherId") teacherId: Int): Response<Teacher>

    @GET("teachers/course/{courseId}")
    suspend fun getTeachersByCourseId(@Path("courseId") courseId: Int): Response<List<Teacher>>

    // ==================== LESSON MANAGEMENT ====================
    @GET("lessons/course/{courseId}")
    suspend fun getLessonsByCourseId(@Path("courseId") courseId: Int): Response<List<Lesson>>

    @GET("lessons/{lessonId}")
    suspend fun getLessonById(@Path("lessonId") lessonId: Int): Response<Lesson>

    @GET("lessons/level/{level}")
    suspend fun getLessonsByLevel(@Path("level") level: String): Response<List<Lesson>>

    // ==================== LESSON PARTS ====================
    @GET("lesson-parts/lesson/{level}")
    suspend fun getLessonPartsByLevel(@Path("level") level: String): Response<List<LessonPart>>

    @GET("lesson-parts/{lessonPartId}")
    suspend fun getLessonPartById(@Path("lessonPartId") lessonPartId: Int): Response<LessonPart>

    @GET("lesson-part-contents/{lessonPartId}")
    suspend fun getLessonPartContents(@Path("lessonPartId") lessonPartId: Int): Response<List<LessonPartContent>>

    // ==================== SCORES & PROGRESS ====================
    @GET("scores/student/{studentId}")
    suspend fun getScoresByStudentId(@Path("studentId") studentId: Int): Response<List<LessonPartScore>>

    @GET("scores/lesson-part/{lessonPartId}/student/{studentId}")
    suspend fun getScoreByLessonPartAndStudent(
        @Path("lessonPartId") lessonPartId: Int,
        @Path("studentId") studentId: Int
    ): Response<List<LessonPartScore>>

    @POST("scores")
    suspend fun submitScore(@Body score: LessonPartScore): Response<LessonPartScore>



    // ==================== PROGRESS TRACKING ====================
    @GET("progress/lesson-part/{lessonPartId}/student/{studentId}")
    suspend fun getLessonPartProgress(
        @Path("lessonPartId") lessonPartId: Int,
        @Path("studentId") studentId: Int
    ): Response<ProgressResponse<LessonPartProgress>>

    @GET("progress/lesson/{level}/student/{studentId}")
    suspend fun getLessonProgress(
        @Path("level") level: String,
        @Path("studentId") studentId: Int
    ): Response<ProgressResponse<LessonProgress>>

    @GET("progress/student/{studentId}/overall")
    suspend fun getOverallProgress(@Path("studentId") studentId: Int): Response<ProgressResponse<OverallProgress>>

    // ==================== ASSIGNMENTS & QUESTIONS ====================
    @GET("assignments/course/{courseId}")
    suspend fun getAssignmentsByCourseId(@Path("courseId") courseId: Int): Response<List<Assignment>>

    @GET("assignments/{assignmentId}")
    suspend fun getAssignmentById(@Path("assignmentId") assignmentId: Int): Response<Assignment>

    @GET("questions/assignment/{assignmentId}")
    suspend fun getQuestionsByAssignmentId(@Path("assignmentId") assignmentId: Int): Response<List<Question>>

    @GET("questions/{questionId}")
    suspend fun getQuestionById(@Path("questionId") questionId: Int): Response<Question>

    @POST("student-answers")
    suspend fun submitAnswer(@Body answer: StudentAnswer): Response<StudentAnswer>

    @GET("student-answers/student/{studentId}")
    suspend fun getAnswersByStudentId(@Path("studentId") studentId: Int): Response<List<StudentAnswer>>

    // ==================== CLASS POSTS & COMMUNICATION ====================
    @GET("class-posts/course/{courseId}")
    suspend fun getClassPostsByCourseId(@Path("courseId") courseId: Int): Response<List<ClassPost>>

    @GET("class-posts/{postId}")
    suspend fun getClassPostById(@Path("postId") postId: Int): Response<ClassPost>

    @POST("class-posts")
    suspend fun createClassPost(@Body post: ClassPost): Response<ClassPost>

    @GET("answers/question/{questionId}")
    suspend fun getAnswersByQuestionId(@Path("questionId") questionId: Int): Response<List<Answer>>

    @POST("answers")
    suspend fun createAnswer(@Body answer: Answer): Response<Answer>

    @GET("class-post-replies/post/{postId}")
    suspend fun getClassPostReplies(@Path("postId") postId: Int): Response<List<ClassPostComment>>

    @POST("class-post-replies")
    suspend fun createClassPostReply(@Body reply: ClassPostComment): Response<ClassPostComment>

    // ==================== NOTIFICATIONS ====================
    @GET("notifications/student/{studentId}")
    suspend fun getNotificationsByStudentId(@Path("studentId") studentId: Int): Response<List<Notification>>

    @GET("notifications/{notificationId}")
    suspend fun getNotificationById(@Path("notificationId") notificationId: Int): Response<Notification>

    @PUT("notifications/{notificationId}/read")
    suspend fun markNotificationAsRead(@Path("notificationId") notificationId: Int): Response<Notification>

    // ==================== EXAM RESULTS & EVALUATION ====================
    @GET("exam-results/student/{studentId}")
    suspend fun getExamResultsByStudentId(@Path("studentId") studentId: Int): Response<List<ExamResult>>

    @GET("exam-results/course/{courseId}")
    suspend fun getExamResultsByCourseId(@Path("courseId") courseId: Int): Response<List<ExamResult>>

    @POST("exam-results")
    suspend fun submitExamResult(@Body result: ExamResult): Response<ExamResult>

    @GET("evaluations/student/{studentId}")
    suspend fun getStudentEvaluations(@Path("studentId") studentId: Int): Response<List<StudentEvaluation>>

    @POST("evaluations")
    suspend fun createStudentEvaluation(@Body evaluation: StudentEvaluation): Response<StudentEvaluation>
}