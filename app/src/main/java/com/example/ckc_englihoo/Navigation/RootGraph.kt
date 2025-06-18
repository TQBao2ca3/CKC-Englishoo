package com.example.ckc_englihoo.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.example.ckc_englihoo.API.AppViewModel
import com.example.ckc_englihoo.Screen.MainScreen
import com.example.ckc_englihoo.Screen.SplashScreen
import com.example.ckc_englihoo.Screen.LoginScreen
import com.example.ckc_englihoo.Screen.ClassStreamScreen
import com.example.ckc_englihoo.Screen.CourseRegistrationScreen

sealed class NavGraph(var route: String) {
    object Splash: NavGraph("splash")
    object Login: NavGraph("login")
    object Main: NavGraph("main")
}

@Composable
fun RootGraph(navRootController: NavHostController, viewModel: AppViewModel) {
    NavHost(
        navController = navRootController,
        startDestination = NavGraph.Splash.route
    ) {
        composable(NavGraph.Splash.route) {
            SplashScreen(navController = navRootController, viewModel = viewModel)
        }
        composable(NavGraph.Login.route) {
            LoginScreen(navController = navRootController, viewModel = viewModel)
        }
        composable(NavGraph.Main.route) {
            MainScreen(navRootController = navRootController, viewModel = viewModel)
        }

        // ClassStream route with course parameters
        composable(
            route = "class_stream/{courseId}/{courseName}",
            arguments = listOf(
                navArgument("courseId") { type = NavType.IntType },
                navArgument("courseName") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val courseId = backStackEntry.arguments?.getInt("courseId") ?: 1
            val courseName = backStackEntry.arguments?.getString("courseName") ?: "Khóa học"

            ClassStreamScreen(
                navController = navRootController,
                viewModel = viewModel,
                courseId = courseId,
                classTitle = courseName
            )
        }

        // Course Registration route
        composable("course_registration") {
            CourseRegistrationScreen(
                navController = navRootController,
                viewModel = viewModel
            )
        }
    }
}