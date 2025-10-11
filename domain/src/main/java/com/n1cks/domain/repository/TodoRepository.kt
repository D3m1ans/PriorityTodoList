package com.n1cks.domain.repository

import com.n1cks.domain.model.TaskFilter
import com.n1cks.domain.model.TaskModel
import com.n1cks.domain.model.TaskPriority
import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    fun getTasks(): Flow<List<TaskModel>>
    suspend fun addTask(task: TaskModel)
    suspend fun deleteTask(task: TaskModel)
    suspend fun updateTask(task: TaskModel)
    suspend fun toggleTaskCompleted(taskId: Int, isCompleted: Boolean)
    suspend fun updateTaskPriority(taskId: Int, priority: TaskPriority)
    fun getTasksWithFilters(filter: TaskFilter) : Flow<List<TaskModel>>
}