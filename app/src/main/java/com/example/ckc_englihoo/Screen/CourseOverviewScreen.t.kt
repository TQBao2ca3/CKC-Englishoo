package com.example.ckc_englihoo.Screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ckc_englihoo.Card.CourseCard
import com.example.ckc_englihoo.DataClass.CourseCardData

@Composable
fun UnitOverviewScreen(modules: List<CourseCardData>) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        MaterialTheme.colorScheme.background,
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                    )
                )
            )
            .padding(16.dp)
    ) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(items = modules) { module ->
                Surface(
                    shape = MaterialTheme.shapes.medium,
                    tonalElevation = 4.dp,
                    shadowElevation = 4.dp,
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    CourseCard(module)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewUnitOverviewScreen() {
    val modules = listOf(
        CourseCardData(
            title = "Vocabulary",
            subtitle = "(150 words)",
            icon = { Icon(Icons.Default.ListAlt, contentDescription = null, tint = Color(0xFF5E4AE3)) },
            score = 30,
            coins = 12,
            progress = 0.9f,
            onClick = {}
        ),
        CourseCardData(
            title = "Theory",
            subtitle = "",
            icon = { Icon(Icons.Default.School, contentDescription = null, tint = Color(0xFF5E4AE3)) },
            score = 30,
            coins = 12,
            progress = 0.55f,
            onClick = {}
        ),
        CourseCardData(
            title = "Homework",
            subtitle = "",
            icon = { Icon(Icons.Default.Book, contentDescription = null, tint = Color(0xFF5E4AE3)) },
            score = 30,
            coins = 12,
            progress = 0.65f,
            onClick = {}
        )
    )
    UnitOverviewScreen(modules)
}
