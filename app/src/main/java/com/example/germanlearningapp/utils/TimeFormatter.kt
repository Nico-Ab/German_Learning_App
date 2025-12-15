package com.example.germanlearningapp.utils

import java.util.concurrent.TimeUnit

object TimeFormatter {
    fun formatDuration(millis: Long): String {
        val days = TimeUnit.MILLISECONDS.toDays(millis)
        val hours = TimeUnit.MILLISECONDS.toHours(millis) % 24
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60

        return when {
            days > 0 -> "in $days day(s)"
            hours > 0 -> "in $hours hour(s)"
            minutes > 0 -> "in $minutes min(s)"
            else -> "in <1 min"
        }
    }
}
