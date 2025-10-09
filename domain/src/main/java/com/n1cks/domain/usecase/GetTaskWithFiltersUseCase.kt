package com.n1cks.domain.usecase

import com.n1cks.domain.model.TaskFilter
import com.n1cks.domain.model.TaskModel
import com.n1cks.domain.model.TaskPriority
import com.n1cks.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTaskWithFiltersUseCase @Inject constructor(
    private val repository: TodoRepository
) {
    operator fun invoke(filter: TaskFilter): Flow<List<TaskModel>> {
        return repository.getTaskWithFilters(
            priority = filter.priority, isCompleted = filter.isCompleted
        )
    }
}