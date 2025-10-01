package com.n1cks.prioritytodolist.presentation.state

import com.n1cks.domain.model.TaskModel
import com.n1cks.domain.model.TaskPriority

data class TodoListState(
    val tasks: List<TaskModel> = emptyList(),
    val todoTaskEdit: TaskModel? = null,
    val editDialogTitle: String = "",
    val editDialogDesc: String = "",
    val selectedPriority: TaskPriority = TaskPriority.MEDIUM,
    val sortByPriority: Boolean = true
)