package com.example.ckc_englihoo.Navigation


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun NavigationAppBar(navController: NavController) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route



    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        color = Color.White,
        shadowElevation = 8.dp,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Course Tab
            NavigationTabItem(
                item = NavItem.Course,
                isSelected = currentRoute == NavItem.Course.route,
                onClick = {
                    navController.navigate(NavItem.Course.route) {
                        navController.graph.startDestinationRoute?.let {
                            popUpTo(it) { saveState = true }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )

            // Home Tab - Center với circle design
            Box(
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier
                        .size(56.dp)
                        .clickable {
                            navController.navigate(NavItem.Home.route) {
                                navController.graph.startDestinationRoute?.let {
                                    popUpTo(it) { saveState = true }
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                    shape = CircleShape,
                    color = if (currentRoute == NavItem.Home.route)
                        Color(0xFF4F46E5) else Color(0xFFF1F5F9),
                    shadowElevation = if (currentRoute == NavItem.Home.route) 8.dp else 4.dp
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.size(56.dp)
                    ) {
                        Icon(
                            imageVector = NavItem.Home.icon,
                            contentDescription = NavItem.Home.title,
                            tint = if (currentRoute == NavItem.Home.route)
                                Color.White else Color(0xFF64748B),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            // ClassStream Tab
            NavigationTabItem(
                item = NavItem.ClassStream,
                isSelected = currentRoute == NavItem.ClassStream.route,
                onClick = {
                    navController.navigate(NavItem.ClassStream.route) {
                        navController.graph.startDestinationRoute?.let {
                            popUpTo(it) { saveState = true }
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
private fun NavigationTabItem(
    item: NavItem,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(vertical = 8.dp, horizontal = 12.dp)
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.title,
            tint = if (isSelected) Color(0xFF4F46E5) else Color(0xFF94A3B8),
            modifier = Modifier.size(24.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = item.title,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
            color = if (isSelected) Color(0xFF4F46E5) else Color(0xFF94A3B8)
        )
    }
}