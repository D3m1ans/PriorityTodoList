package com.n1cks.prioritytodolist.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.n1cks.domain.model.TaskFilter
import com.n1cks.domain.model.TaskModel
import com.n1cks.domain.usecase.AddTaskUseCase
import com.n1cks.domain.usecase.DeleteTaskUseCase
import com.n1cks.domain.usecase.GetAllTasksUseCase
import com.n1cks.domain.usecase.GetTasksWithFiltersUseCase
import com.n1cks.domain.usecase.ToggleTaskCompletedUseCase
import com.n1cks.domain.usecase.UpdateTaskPriorityUseCase
import com.n1cks.domain.usecase.UpdateTaskUseCase
import com.n1cks.prioritytodolist.presentation.events.TodoListEvents
import com.n1cks.prioritytodolist.presentation.state.TodoListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoListViewModel @Inject constructor(
    private val addTaskUseCase: AddTaskUseCase,
    private val deleteTaskUseCase: DeleteTaskUseCase,
    private val getAllTaskUseCase: GetAllTasksUseCase,
    private val toggleTaskCompleted: ToggleTaskCompletedUseCase,
    private val updateTaskPriorityUseCase: UpdateTaskPriorityUseCase,
    private val updateTaskUseCase: UpdateTaskUseCase,
    private val getTasksWithFiltersUseCase: GetTasksWithFiltersUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(TodoListState())
    val state = _state.asStateFlow()

    private var getTaskJob: Job? = null

    init {
        getTasks()
    }

    fun onEvent(event: TodoListEvents) {
        when (event) {
            is TodoListEvents.OnAddTask -> {
                val newTaskTitle = "Новая задача №${_state.value.tasks.size + 1}"
                val newTask = TaskModel(
                    title = newTaskTitle,
                    priority = _state.value.selectedPriority
                )

                viewModelScope.launch {
                    addTaskUseCase(task = newTask)
                }
            }

            is TodoListEvents.OnDeleteTask -> {
                viewModelScope.launch {
                    deleteTaskUseCase(task = event.task)
                }
            }

            is TodoListEvents.OnEditDialogDismiss -> {
                if (event.task != null && event.task.id != null) {
                    viewModelScope.launch {
                        val updatedTask = event.task.copy(
                            title = event.newTitle ?: event.task.title,
                            desc = event.newDesc ?: event.task.desc,
                            priority = event.newPriority ?: event.task.priority
                        )
                        if (updatedTask != event.task) {
                            updateTaskUseCase(updatedTask)
                        }
                    }
                }
                _state.value = _state.value.copy(
                    todoTaskEdit = null,
                    editDialogTitle = "",
                    editDialogDesc = ""
                )
            }

            is TodoListEvents.OnEditTask -> {
                _state.value = _state.value.copy(
                    todoTaskEdit = event.task,
                    editDialogTitle = event.task.title,
                    editDialogDesc = event.task.desc ?: "",
                    selectedPriority = event.task.priority
                )
            }

            is TodoListEvents.OnPriorityChange -> {
                viewModelScope.launch {
                    updateTaskPriorityUseCase(event.task, event.priority)
                }
            }

            is TodoListEvents.OnPriorityFilter -> {
                val newFilter = _state.value.currentFilter.copy(priority = event.priority)
                getTaskWithFilter(newFilter)
            }

            is TodoListEvents.OnToggleSort -> {
                _state.value = _state.value.copy(
                    sortByPriority = !_state.value.sortByPriority
                )
                getTasks()
            }

            is TodoListEvents.OnToggleTaskCompleted -> {
                viewModelScope.launch {
                    val updatedTask = event.task.copy(isCompleted = !event.task.isCompleted)
                    toggleTaskCompleted(
                        taskId = updatedTask.id ?: return@launch,
                        isCompleted = updatedTask.isCompleted
                    )
                }
            }

            TodoListEvents.OnClearFilter -> {
                getTaskWithFilter(TaskFilter.DEFAULT)
            }

            TodoListEvents.OnFilterClick -> {
                _state.value = _state.value.copy(
                    showFilterDialog = !_state.value.showFilterDialog
                )
            }

            is TodoListEvents.OnStatusFilter -> {
                val newFilter = _state.value.currentFilter.copy(isCompleted = event.isCompleted)
                getTaskWithFilter(newFilter)
            }
        }
    }

    private fun getTasks() {
        getTaskWithFilter(TaskFilter.DEFAULT)
    }

    private fun getTaskWithFilter(filter: TaskFilter) {
        getTaskJob?.cancel()

        getTaskJob = if (filter.hasActiveFilters()) {
            getTasksWithFiltersUseCase(filter)
        } else {
            getAllTaskUseCase().map { tasks ->
                tasks.sortedWith(
                    compareBy<TaskModel> { it.isCompleted }
                        .thenByDescending { it.createdAt }
                )
            }
        }
            .onEach { tasks ->
                _state.value = state.value.copy(
                    tasks = tasks,
                    currentFilter = filter,
                    isFiltering = filter.hasActiveFilters()
                )
            }
            .launchIn(viewModelScope)
    }
}