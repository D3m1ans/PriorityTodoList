package com.n1cks.domain.usecase

import com.n1cks.domain.model.TaskModel
import com.n1cks.domain.repository.TodoRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class GetTaskOrderedByPriorityUseCase @Inject constructor(
    private val repository: TodoRepository
) {
    operator fun invoke() : Flow<List<TaskModel>>{
        return repository.getTasksOrderByPriority()
    }
}