package com.example.ckc_englihoo.Navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material.icons.rounded.PlayLesson
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

sealed class NavItem (var icon: ImageVector, var item_route:String){
    object Home: NavItem(Icons.Rounded.Home,"home")
    object Course: NavItem(Icons.Rounded.PlayLesson,"course")
    object Lesson: NavItem(Icons.Rounded.MenuBook,"lesson")
}

@Composable
fun NavigationBarGraph(NavItemController: NavHostController, NavRootController: NavHostController){
    NavHost(
        navController = NavItemController,
        startDestination =  NavItem.Home.item_route
    ) {
        composable(NavItem.Home.item_route){

        }
        composable(NavItem.Course.item_route){

        }
        composable(NavItem.Lesson.item_route){

        }
    }
}