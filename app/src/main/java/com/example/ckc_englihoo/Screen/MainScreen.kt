package com.example.ckc_englihoo.Screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.ckc_englihoo.API.AppViewModel
import com.example.ckc_englihoo.Navigation.NavigationAppBar
import com.example.ckc_englihoo.Navigation.NavigationBarGraph

@Composable
fun MainScreen(
    navRootController: NavHostController,
    viewModel: AppViewModel,
    modifier: Modifier = Modifier) {
    val navItemController = rememberNavController()
    
    Scaffold(
        modifier = modifier.fillMaxSize(),
        bottomBar = {
            NavigationAppBar(navController = navItemController)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            NavigationBarGraph(
                NavItemController = navItemController,
                NavRootController = navRootController,
                viewModel = viewModel
            )
        }
    }
}
