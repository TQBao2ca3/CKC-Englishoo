package com.example.ckc_englihoo.Navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.Forum
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.ckc_englihoo.API.AppViewModel
import com.example.ckc_englihoo.Screen.CourseScreenStudent
import com.example.ckc_englihoo.Screen.ClassStreamScreen
import com.example.ckc_englihoo.Screen.CourseListScreen
import com.example.ckc_englihoo.Screen.HomeScreenStudent

sealed class NavItem(val icon: ImageVector, val route: String, val title: String) {
    object Home : NavItem(Icons.Rounded.Home, "home", "Home")
    object Course : NavItem(Icons.Rounded.School, "course", "Course")
    object ClassStream : NavItem(Icons.Rounded.Forum, "class_stream", "Lớp học")
}

@Composable
fun NavigationBarGraph(NavItemController: NavHostController, NavRootController: NavHostController,viewModel: AppViewModel){
    NavHost(
        navController = NavItemController,
        startDestination =  NavItem.Home.route
    ) {
        composable(NavItem.Home.route){
            HomeScreenStudent(navController = NavRootController,viewModel = viewModel)
        }
        composable(NavItem.Course.route){
            CourseScreenStudent(navController = NavRootController,viewModel = viewModel)
        }
        composable(NavItem.ClassStream.route){
            CourseListScreen(navController = NavRootController, viewModel = viewModel)
        }
    }
}