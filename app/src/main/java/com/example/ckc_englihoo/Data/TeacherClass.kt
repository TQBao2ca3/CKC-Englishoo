package com.example.ckc_englihoo.Data

import androidx.compose.ui.graphics.Color
import com.example.ckc_englihoo.R

data class TeacherClass(
    val id: String,
    val name: String,
    val description: String,
    val academicYear: String,
    val studentCount: Int,
    val backgroundColor: Color,
    val iconResource: Int = R.drawable.student
)
