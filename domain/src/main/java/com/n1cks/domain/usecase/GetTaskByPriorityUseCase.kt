package com.n1cks.domain.usecase

import com.n1cks.domain.model.TaskModel
import com.n1cks.domain.model.TaskPriority
import com.n1cks.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTaskByPriorityUseCase @Inject constructor(
    private val repository: TodoRepository
) {
    operator fun invoke(priority: TaskPriority?) : Flow<List<TaskModel>> {
        return repository.getTaskByPriority(priority = priority)
    }
}