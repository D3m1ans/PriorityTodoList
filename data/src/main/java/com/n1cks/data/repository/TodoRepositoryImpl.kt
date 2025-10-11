package com.n1cks.data.repository

import com.n1cks.data.local.dao.TodoDao
import com.n1cks.data.local.entity.TaskEntity
import com.n1cks.domain.model.TaskFilter
import com.n1cks.domain.model.TaskModel
import com.n1cks.domain.model.TaskPriority
import com.n1cks.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(
    private val dao: TodoDao
) : TodoRepository {
    override fun getTasks(): Flow<List<TaskModel>> {
        return dao.getTasks().map { entity ->
            entity.map { it.toTask() }
        }
    }

    override suspend fun addTask(task: TaskModel) {
        return dao.addTask(task = task.toEntity())
    }

    override suspend fun deleteTask(task: TaskModel) {
        return dao.deleteTask(task = task.toEntity())
    }

    override suspend fun updateTask(task: TaskModel) {
        return dao.updateTask(task = task.toEntity())
    }

    override suspend fun toggleTaskCompleted(taskId: Int, isCompleted: Boolean) {
        return dao.toggleTaskCompleted(taskId = taskId, isCompleted = isCompleted)
    }

    override suspend fun updateTaskPriority(
        taskId: Int,
        priority: TaskPriority
    ) {
        dao.updateTaskPriority(taskId = taskId, priority = priority)
    }

    override fun getTasksWithFilters(filter: TaskFilter): Flow<List<TaskModel>> {

        val priority = filter.priority
        val isCompleted = filter.isCompleted

        return when {
            priority != null && isCompleted != null -> {
                dao.getTaskByPriorityAndStatus(priority, isCompleted).map { entities ->
                    entities.map { it.toTask() }
                }
            }
            priority != null -> {
                dao.getTaskByPriority(priority).map { entities ->
                    entities.map { it.toTask() }
                }
            }
            isCompleted != null -> {
                dao.getTaskByStatus(isCompleted).map { entities ->
                    entities.map { it.toTask() }
                }
            }
            else -> getTasks()
        }
    }

    private fun TaskEntity.toTask(): TaskModel {
        return TaskModel(
            id = id,
            title = title,
            desc = desc,
            isCompleted = isCompleted,
            priority = priority,
            createdAt = createdAt
        )
    }

    private fun TaskModel.toEntity(): TaskEntity {
        return TaskEntity(
            id = id,
            title = title,
            desc = desc,
            isCompleted = isCompleted,
            priority = priority,
            createdAt = createdAt
        )
    }

}