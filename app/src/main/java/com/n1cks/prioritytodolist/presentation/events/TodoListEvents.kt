package com.n1cks.prioritytodolist.presentation.events

import com.n1cks.domain.model.TaskModel
import com.n1cks.domain.model.TaskPriority

sealed interface TodoListEvents {

    data object OnAddTask : TodoListEvents

    data class OnDeleteTask(val task: TaskModel) : TodoListEvents

    data class OnToggleTaskCompleted(val task: TaskModel): TodoListEvents

    data class OnEditTask(val task: TaskModel): TodoListEvents

    data class OnEditDialogDismiss(
        val task: TaskModel?,
        val newTitle: String?,
        val newDesc: String?,
        val newPriority: TaskPriority?
    ) : TodoListEvents

    data class OnPriorityChange(val task: TaskModel, val priority: TaskPriority) : TodoListEvents

    data object OnToggleSort : TodoListEvents

    data class OnPriorityFilter(val priority: TaskPriority?) : TodoListEvents
}