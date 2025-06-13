package com.example.ckc_englihoo.DataClass

import androidx.compose.runtime.Composable

data class CourseCardData(
    val title: String,
    val subtitle: String,
    val icon: @Composable () -> Unit,
    val score: Int,
    val coins: Int,
    val progress: Float,     // 0f..1f
    val onClick: () -> Unit
)