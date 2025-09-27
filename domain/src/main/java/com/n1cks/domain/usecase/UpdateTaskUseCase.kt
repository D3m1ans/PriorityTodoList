package com.n1cks.domain.usecase

import com.n1cks.domain.model.TaskModel
import com.n1cks.domain.repository.TodoRepository
import javax.inject.Inject

class UpdateTaskUseCase @Inject constructor(
    private val repository: TodoRepository
) {
    suspend operator fun invoke(task: TaskModel){
        repository.updateTask(task = task)
    }
}