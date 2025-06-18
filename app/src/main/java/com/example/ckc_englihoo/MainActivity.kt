package com.example.ckc_englihoo


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.ckc_englihoo.API.AppViewModel
import com.example.ckc_englihoo.Navigation.RootGraph
import com.example.ckc_englihoo.ui.theme.CKC_EnglihooTheme

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<AppViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CKC_EnglihooTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navRootController = rememberNavController()
                    RootGraph(navRootController = navRootController,viewModel = viewModel)
                }
            }
        }
    }
}