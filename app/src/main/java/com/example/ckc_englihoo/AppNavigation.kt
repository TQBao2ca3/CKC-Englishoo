import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ckc_englihoo.MyProfile
import com.example.ckc_englihoo.StudyProgressScreen

sealed class RouteScreen(val route : String){
    object OnboardingScreen:RouteScreen(route = "onboarding_screen")
    object MyProfile:RouteScreen(route = "my_profile")
    object StudyProgress:RouteScreen(route = "study_progress")
    object Settings:RouteScreen(route = "settings")
    object AdminLogin:RouteScreen(route = "admin_login")
    object TeacherLogin:RouteScreen(route = "teacher_login")
    object StudentLogin:RouteScreen(route = "student_login")
}

@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = RouteScreen.MyProfile.route) {
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
            MyProfile(
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
            StudyProgressScreen(
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
                    navController.navigate(RouteScreen.MyProfile.route)
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
    }
}
