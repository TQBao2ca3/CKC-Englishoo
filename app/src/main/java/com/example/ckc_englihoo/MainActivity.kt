package com.example.ckc_englihoo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.ckc_englihoo.Navigation.RootGraph

import com.example.ckc_englihoo.ui.theme.CKC_EnglihooTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CKC_EnglihooTheme {
                val navController = rememberNavController()
                RootGraph(navController = navController)
            }
        }
    }
}