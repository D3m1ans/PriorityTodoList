package com.n1cks.domain.usecase

import com.n1cks.domain.model.TaskModel
import com.n1cks.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTaskByStatusUseCase @Inject constructor(
    private val repository: TodoRepository
) {
    operator fun invoke(isCompleted: Boolean?) : Flow<List<TaskModel>> {
        return repository.getTaskByStatus(isCompleted = isCompleted)
    }
}