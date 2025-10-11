package com.n1cks.prioritytodolist.presentation.utilits

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.n1cks.domain.model.TaskPriority

@Composable
fun getPriorityColor(priority: TaskPriority): Color {
    return when (priority){
        TaskPriority.LOW -> Color(0xFF8CFF00)
        TaskPriority.MEDIUM -> Color(0xFFFFCC00)
        TaskPriority.HIGH -> Color(0xFFFF6201)
        TaskPriority.CRITICAL -> Color(0xFFFF0000)
    }
}

val TaskPriority.displayName: String
    get() = when (this) {
        TaskPriority.LOW -> "Низкий"
        TaskPriority.MEDIUM -> "Средний"
        TaskPriority.HIGH -> "Высокий"
        TaskPriority.CRITICAL -> "Срочный"
    }