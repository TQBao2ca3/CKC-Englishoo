package com.example.ckc_englihoo.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ckc_englihoo.LoginForm
import com.example.ckc_englihoo.UserType
import com.example.ckc_englihoo.OnboardingScreen

import com.example.ckc_englihoo.Screen.MyProfileStudent
import com.example.ckc_englihoo.Screen.StudyProgressStudent
import com.example.ckc_englihoo.Screen.CourseRegistrationStudent
import com.example.ckc_englihoo.Screen.CreateClassTeacher
import com.example.ckc_englihoo.Screen.HomeScreenTeacher
import com.example.ckc_englihoo.Screen.ClassDetailTeacher
import com.example.ckc_englihoo.Screen.StudentListTeacher
import com.example.ckc_englihoo.Screen.GradesTeacher
import com.example.ckc_englihoo.Screen.CreatePostTeacher
import com.example.ckc_englihoo.Screen.PostDetailTeacher
import com.example.ckc_englihoo.Screen.EditPostTeacher
import com.example.ckc_englihoo.Screen.StudentGradingTeacher
import com.example.ckc_englihoo.Screen.CreateAssignmentTeacher
import com.example.ckc_englihoo.Screen.CreateAnnouncementTeacher
import com.example.ckc_englihoo.Data.TeacherClass
import com.example.ckc_englihoo.Data.ClassRepository
import com.example.ckc_englihoo.Data.LegacyClassPost
import com.example.ckc_englihoo.Screen.MyProfileTeacher

sealed class RouteScreen(val route : String){
    object OnboardingScreen:RouteScreen(route = "onboarding_screen")
    object MyProfile:RouteScreen(route = "my_profile")
    object StudyProgress:RouteScreen(route = "study_progress")
    object Settings:RouteScreen(route = "settings")
    object AdminLogin:RouteScreen(route = "admin_login")
    object TeacherLogin:RouteScreen(route = "teacher_login")
    object StudentLogin:RouteScreen(route = "student_login")

    // Teacher screens
    object TeacherHome:RouteScreen(route = "teacher_home")
    object TeacherProfile:RouteScreen(route = "teacher_profile")
    object ClassDetail:RouteScreen(route = "class_detail/{classId}")
    object StudentList:RouteScreen(route = "student_list/{classId}")
    object Grades:RouteScreen(route = "grades/{classId}")
    object CreatePost:RouteScreen(route = "create_post/{classId}")
    object CreateAssignment:RouteScreen(route = "create_assignment/{assignmentType}/{classId}")
    object CreateAnnouncement:RouteScreen(route = "create_announcement/{classId}")
}

@Composable
fun RootGraph(navController: NavHostController) {


    NavHost(navController = navController, startDestination = RouteScreen.OnboardingScreen.route) {
        composable(RouteScreen.OnboardingScreen.route) {
            OnboardingScreen(
                navController = navController,
                onStudentClick = {
                    navController.navigate(RouteScreen.StudentLogin.route)
                },
                onTeacherClick = {
                    navController.navigate(RouteScreen.TeacherLogin.route)
                },
                onAdminClick = {
                    navController.navigate(RouteScreen.AdminLogin.route)
                }
            )
        }

        composable(RouteScreen.MyProfile.route) {
            MyProfileStudent(
                onNavigateToStudyProgress = {
                    navController.navigate(RouteScreen.StudyProgress.route)
                },
                onNavigateToSettings = {
                    navController.navigate(RouteScreen.Settings.route)
                },
                onLogout = {
                    navController.navigate(RouteScreen.OnboardingScreen.route) {
                        popUpTo(RouteScreen.MyProfile.route) { inclusive = true }
                    }
                }
            )
        }

        composable(RouteScreen.StudyProgress.route) {
            StudyProgressStudent(
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(RouteScreen.AdminLogin.route) {
            LoginForm(
                userType = UserType.ADMIN,
                onLoginClick = { username, password ->
                    // TODO: Xử lý đăng nhập admin
                    navController.navigate(RouteScreen.MyProfile.route)
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(RouteScreen.TeacherLogin.route) {
            LoginForm(
                userType = UserType.TEACHER,
                onLoginClick = { username, password ->
                    // TODO: Xử lý đăng nhập giảng viên
                    navController.navigate(RouteScreen.TeacherHome.route)
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(RouteScreen.StudentLogin.route) {
            LoginForm(
                userType = UserType.STUDENT,
                onLoginClick = { username, password ->
                    // TODO: Xử lý đăng nhập sinh viên
                    navController.navigate(RouteScreen.MyProfile.route)
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        // Teacher screens
        composable(RouteScreen.TeacherHome.route) {
            HomeScreenTeacher(
                onClassClick = { teacherClass ->
                    println("DEBUG: Navigating to class detail with ID: ${teacherClass.id}")
                    navController.navigate("class_detail/${teacherClass.id}")
                },
                onCreateClassClick = {
                    println("DEBUG: Create class clicked")
                    navController.navigate("create_class")
                },
                onProfileClick = {
                    println("DEBUG: Profile clicked")
                    navController.navigate(RouteScreen.TeacherProfile.route)
                }
            )
        }

        composable(RouteScreen.TeacherProfile.route) {
            MyProfileTeacher(
                onBackClick = {
                    navController.navigate(RouteScreen.TeacherHome.route) {
                        popUpTo(RouteScreen.TeacherHome.route) { inclusive = true }
                    }
                },
                onNavigateToClasses = {
                    navController.navigate(RouteScreen.TeacherHome.route)
                },
                onNavigateToSettings = {
                    // TODO: Navigate to teacher settings
                },
                onLogout = {
                    navController.navigate(RouteScreen.OnboardingScreen.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable("class_detail/{classId}") { backStackEntry ->
            val classId = backStackEntry.arguments?.getString("classId") ?: ""
            // Lấy dữ liệu thực tế từ ClassRepository
            val teacherClass = ClassRepository.getClassById(classId) ?: ClassRepository.getAllClasses().first()

            ClassDetailTeacher(
                navController = navController,
                teacherClass = teacherClass,
                onBackClick = {
                    println("DEBUG: Back clicked from ClassDetail")
                    navController.popBackStack()
                },
                onCreatePostClick = {
                    println("DEBUG: Create post clicked for class: $classId")
                    navController.navigate("create_post/$classId")
                },
                onStudentListClick = {
                    println("DEBUG: Student list clicked for class: $classId")
                    navController.navigate("student_list/$classId")
                },
                onGradesClick = {
                    println("DEBUG: Grades clicked for class: $classId")
                    navController.navigate("grades/$classId")
                },
                onClassClick = { selectedClass ->
                    navController.navigate("class_detail/${selectedClass.id}") {
                        popUpTo("class_detail/{classId}") { inclusive = true }
                    }
                },
                onPostDetailClick = { postId ->
                    navController.navigate("post_detail/$classId/$postId")
                },
                onEditPostClick = { postId ->
                    navController.navigate("edit_post/$classId/$postId")
                }
            )
        }

        composable("student_grading/{studentId}/{studentName}") { backStackEntry ->
            val studentId = backStackEntry.arguments?.getString("studentId") ?: ""
            val studentName = backStackEntry.arguments?.getString("studentName") ?: ""

            StudentGradingTeacher(
                navController = navController,
                studentId = studentId,
                studentName = studentName
            )
        }

        composable("student_list/{classId}") { backStackEntry ->
            val classId = backStackEntry.arguments?.getString("classId") ?: ""
            val teacherClass = ClassRepository.getClassById(classId) ?: ClassRepository.getAllClasses().first()

            StudentListTeacher(
                teacherClass = teacherClass,
                onBackClick = {
                    navController.popBackStack()
                },
                onStudentClick = { student ->
                    // TODO: Navigate to student detail
                },
                onInviteStudentClick = {
                    // TODO: Navigate to invite student screen
                }
            )
        }

        composable("grades/{classId}") { backStackEntry ->
            val classId = backStackEntry.arguments?.getString("classId") ?: ""
            val teacherClass = ClassRepository.getClassById(classId) ?: ClassRepository.getAllClasses().first()

            GradesTeacher(
                teacherClass = teacherClass,
                onBackClick = {
                    navController.popBackStack()
                },
                onAddAssignmentClick = {
                    // TODO: Handle add assignment
                }
            )
        }

        composable("create_post/{classId}") { backStackEntry ->
            val classId = backStackEntry.arguments?.getString("classId") ?: ""
            val teacherClass = ClassRepository.getClassById(classId) ?: ClassRepository.getAllClasses().first()

            CreatePostTeacher(
                teacherClass = teacherClass,
                onBackClick = {
                    navController.popBackStack()
                },
                onPostCreated = { post: LegacyClassPost ->
                    // Convert and save post to ClassRepository
                    val classPost = ClassRepository.convertLegacyPost(post)
                    ClassRepository.addPostToClass(classId, classPost)
                    println("DEBUG: Post created and saved: ${post.title}")
                    // Navigate back to class detail
                    navController.popBackStack()
                }
            )
        }

        composable("post_detail/{classId}/{postId}") { backStackEntry ->
            val classId = backStackEntry.arguments?.getString("classId") ?: ""
            val postId = backStackEntry.arguments?.getString("postId") ?: ""
            val post = ClassRepository.getPostById(classId, postId)

            if (post != null) {
                PostDetailTeacher(
                    post = post,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onEditClick = {
                        navController.navigate("edit_post/$classId/$postId")
                    },
                    onDeleteClick = {
                        ClassRepository.deletePostFromClass(classId, postId)
                        navController.popBackStack()
                    }
                )
            }
        }

        composable("edit_post/{classId}/{postId}") { backStackEntry ->
            val classId = backStackEntry.arguments?.getString("classId") ?: ""
            val postId = backStackEntry.arguments?.getString("postId") ?: ""
            val post = ClassRepository.getPostById(classId, postId)

            if (post != null) {
                EditPostTeacher(
                    post = post,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onSaveClick = { newContent, newAttachments ->
                        val updatedPost = post.copy(
                            content = newContent,
                            attachments = newAttachments,
                            timestamp = "Đã chỉnh sửa"
                        )
                        ClassRepository.updatePostInClass(classId, updatedPost)
                        navController.popBackStack()
                    }
                )
            }
        }

        // Create Class Screen
        composable("create_class") {
            CreateClassTeacher(
                onBackClick = { navController.popBackStack() },
                onClassCreated = { newClass ->
                    // Thêm class mới vào repository
                    val success = ClassRepository.addClass(newClass)
                    if (success) {
                        // Navigate về HomeScreenTeacher
                        navController.popBackStack()
                    }
                }
            )
        }

        // Create Assignment Screen
        composable("create_assignment/{assignmentType}/{classId}") { backStackEntry ->
            val assignmentType = backStackEntry.arguments?.getString("assignmentType") ?: "assignment"
            val classId = backStackEntry.arguments?.getString("classId") ?: ""

            CreateAssignmentTeacher(
                navController = navController,
                assignmentType = assignmentType,
                classId = classId
            )
        }

        // Create Announcement Screen
        composable("create_announcement/{classId}") { backStackEntry ->
            val classId = backStackEntry.arguments?.getString("classId") ?: ""

            CreateAnnouncementTeacher(
                navController = navController,
                classId = classId
            )
        }
    }
}