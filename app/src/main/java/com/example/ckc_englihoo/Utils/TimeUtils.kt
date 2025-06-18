package com.example.ckc_englihoo.Utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Utility functions for time formatting
 */

/**
 * Format a Date to a human-readable "time ago" string
 * @param date The date to format
 * @return Formatted string like "2h trước", "Hôm qua", etc.
 */
fun formatTimeAgo(date: Date): String {
    val now = Date()
    val diffInMillis = now.time - date.time
    val diffInHours = diffInMillis / (1000 * 60 * 60)
    val diffInDays = diffInHours / 24

    return when {
        diffInHours < 1 -> "Vừa xong"
        diffInHours < 24 -> "${diffInHours}h trước"
        diffInDays == 1L -> "Hôm qua"
        diffInDays < 7 -> "${diffInDays} ngày trước"
        else -> {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            dateFormat.format(date)
        }
    }
}

/**
 * Format a String date to a human-readable "time ago" string
 * @param dateString The date string to format (ISO format from API)
 * @return Formatted string like "2h trước", "Hôm qua", etc.
 */
fun formatTimeAgo(dateString: String): String {
    return try {
        // Try to parse ISO date format from API (e.g., "2023-12-25T14:30:00Z")
        val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault())
        val date = isoFormat.parse(dateString)
        if (date != null) {
            formatTimeAgo(date)
        } else {
            // Fallback: try simple date format
            val simpleFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val simpleDate = simpleFormat.parse(dateString)
            if (simpleDate != null) {
                formatTimeAgo(simpleDate)
            } else {
                "Không xác định"
            }
        }
    } catch (e: Exception) {
        // If parsing fails, return the original string or a default
        "Không xác định"
    }
}

/**
 * Format a Date to a specific format
 * @param date The date to format
 * @param pattern The pattern to use (default: "dd/MM/yyyy")
 * @return Formatted date string
 */
fun formatDate(date: Date, pattern: String = "dd/MM/yyyy"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale.getDefault())
    return dateFormat.format(date)
}

/**
 * Format a Date to time only
 * @param date The date to format
 * @return Formatted time string like "14:30"
 */
fun formatTime(date: Date): String {
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return timeFormat.format(date)
}

/**
 * Format a Date to full date and time
 * @param date The date to format
 * @return Formatted string like "25/12/2023 14:30"
 */
fun formatDateTime(date: Date): String {
    val dateTimeFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return dateTimeFormat.format(date)
}
