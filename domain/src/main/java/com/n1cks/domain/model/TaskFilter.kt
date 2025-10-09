package com.n1cks.domain.model

data class TaskFilter(
    val priority: TaskPriority? = null,
    val isCompleted: Boolean? = null
) {
    companion object {
        val DEFAULT = TaskFilter()
    }

    fun hasActiveFilters(): Boolean {
        return priority != null || isCompleted != null
    }
}