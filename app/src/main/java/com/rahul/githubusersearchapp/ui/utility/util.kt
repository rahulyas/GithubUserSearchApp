package com.rahul.githubusersearchapp.ui.utility

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.graphics.Color

fun formatNumber(number: Int): String {
    return when {
        number >= 1000000 -> "${(number / 1000000.0).let { "%.1f".format(it) }}M"
        number >= 1000 -> "${(number / 1000.0).let { "%.1f".format(it) }}K"
        else -> number.toString()
    }
}

fun getLanguageColor(language: String): Color {
    return when (language.lowercase()) {
        "kotlin" -> Color(0xFF7F52FF)
        "java" -> Color(0xFFED8B00)
        "javascript" -> Color(0xFFF7DF1E)
        "python" -> Color(0xFF3776AB)
        "swift" -> Color(0xFFFA7343)
        "dart" -> Color(0xFF0175C2)
        "go" -> Color(0xFF00ADD8)
        "rust" -> Color(0xFF000000)
        "c++" -> Color(0xFF00599C)
        "c" -> Color(0xFFA8B9CC)
        else -> Color(0xFF586069)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatDate(dateString: String): String {
    // Implement date formatting logic
    return try {
        val instant = java.time.Instant.parse(dateString)
        val formatter = java.time.format.DateTimeFormatter.ofPattern("MMM yyyy")
        formatter.format(instant.atZone(java.time.ZoneId.systemDefault()))
    } catch (e: Exception) {
        dateString.substring(0, 7) // Fallback to YYYY-MM format
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatRelativeTime(dateString: String): String {
    return try {
        val instant = java.time.Instant.parse(dateString)
        val now = java.time.Instant.now()
        val duration = java.time.Duration.between(instant, now)

        when {
            duration.toDays() > 30 -> "${duration.toDays() / 30} months ago"
            duration.toDays() > 0 -> "${duration.toDays()} days ago"
            duration.toHours() > 0 -> "${duration.toHours()} hours ago"
            duration.toMinutes() > 0 -> "${duration.toMinutes()} minutes ago"
            else -> "Just now"
        }
    } catch (e: Exception) {
        "Recently"
    }
}