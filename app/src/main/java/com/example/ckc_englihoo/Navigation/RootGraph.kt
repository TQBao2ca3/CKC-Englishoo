package com.example.ckc_englihoo.Navigation

import LoginForm
import OnboardingScreen
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material.icons.rounded.PlayLesson
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable


sealed class NavGrah (var route:String){
    object SplashScreen: NavGrah("splash_screen")
    object LoginScreen: NavGrah("login_screen")
    object RegisterScreen: NavGrah("register_screen")
    object HomeScreen: NavGrah("home_screen")
}

@Composable
fun NavigationBarGraph(NavRootController: NavHostController){
    NavHost(
        navController = NavRootController,
        startDestination =  NavGrah.SplashScreen.route
    ) {
        composable(NavGrah.SplashScreen.route){
            OnboardingScreen(navController = NavRootController,{},{})
        }
        composable(NavGrah.LoginScreen.route){
            LoginForm(navController = NavRootController)
        }
        composable(NavGrah.HomeScreen.route){

        }
    }
}