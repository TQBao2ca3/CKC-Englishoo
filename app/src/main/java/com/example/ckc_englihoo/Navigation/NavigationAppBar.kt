package com.example.ckceng.Navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun NavigationApppBar(navController: NavController){
    val items = listOf(
        NavItem.Home,
        NavItem.Course,
        NavItem.Lesson,
    )
    NavigationBar (

    ){
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = ""
                    )
                },
                selected = currentRoute?.hierarchy?.any(){
                    it.route == item.item_route
                } == true,
                onClick = {
                    navController.navigate(item.item_route){
                        navController.graph.startDestinationRoute?.let {
                            popUpTo(it){
                                saveState = true
                            }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}