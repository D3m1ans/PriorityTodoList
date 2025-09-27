package com.n1cks.domain.model

enum class TaskPriority {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL
}

data class TaskModel(
    val id: Int? = null,
    val title: String,
    val desc: String? = null,
    val isCompleted: Boolean = false,
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val createdAt: Long = System.currentTimeMillis()
)