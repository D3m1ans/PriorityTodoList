package com.n1cks.domain.repository

import com.n1cks.domain.model.TaskModel
import com.n1cks.domain.model.TaskPriority
import kotlinx.coroutines.flow.Flow

interface TodoRepository {
    fun getTasks(): Flow<List<TaskModel>>
    fun getTasksOrderByPriority(): Flow<List<TaskModel>>
    suspend fun addTask(task: TaskModel)
    suspend fun deleteTask(task: TaskModel)
    suspend fun updateTask(task: TaskModel)
    suspend fun toggleTaskCompleted(taskId: Int, isCompleted: Boolean)
    suspend fun updateTaskPriority(taskId: Int, priority: TaskPriority)

    fun getTaskByPriority(priority: TaskPriority?) : Flow<List<TaskModel>>
    fun getTaskByStatus(isCompleted: Boolean?) : Flow<List<TaskModel>>
    fun getTaskWithFilters(priority: TaskPriority?, isCompleted: Boolean?): Flow<List<TaskModel>>
}