import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

sealed class RouteScreen(val route : String){
    object OnboardingScreen:RouteScreen(route = "onboarding_screen")
}

//@Composable
//fun AppNavigation(navController: NavHostController) {
//
//    NavHost(navController = navController, startDestination = RouteScreen.OnboardingScreen.route) {
//        composable(RouteScreen.OnboardingScreen.route) {
//            OnboardingScreen(navController = navController,)
//        }
//        // Tạm thời bỏ các màn hình navigation cho đến khi giải quyết lỗi
//        // composable("admin_login") { LoginForm("Đăng nhập Admin") }
//        // composable("teacher_login") { LoginForm("Đăng nhập Giảng viên") }
//        // composable("student_login") { LoginForm("Đăng nhập Sinh viên") }
//    }
//}
