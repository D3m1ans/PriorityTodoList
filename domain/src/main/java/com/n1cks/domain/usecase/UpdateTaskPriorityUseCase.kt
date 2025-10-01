package com.n1cks.domain.usecase

import com.n1cks.domain.model.TaskModel
import com.n1cks.domain.model.TaskPriority
import com.n1cks.domain.repository.TodoRepository
import javax.inject.Inject

class UpdateTaskPriorityUseCase @Inject constructor(
    private val repository: TodoRepository
) {
    suspend operator fun invoke(task: TaskModel, priority: TaskPriority){
        val updatedTask = task.copy(priority = priority)
        repository.updateTask(updatedTask)
    }
}