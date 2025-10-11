package com.n1cks.prioritytodolist.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.n1cks.domain.model.TaskPriority
import com.n1cks.prioritytodolist.presentation.events.TodoListEvents
import com.n1cks.prioritytodolist.presentation.state.TodoListState
import com.n1cks.prioritytodolist.presentation.utilits.displayName
import com.n1cks.prioritytodolist.presentation.utilits.getPriorityColor

@Composable
fun FilterDialog(
    state: TodoListState,
    onEvent: (TodoListEvents) -> Unit,
) {
    AlertDialog(
        onDismissRequest = { onEvent(TodoListEvents.OnFilterClick) },
        title = { Text("Фильтры") },
        text = {
            Column {
                Text("Статус задачи:", style = androidx.compose.material3.MaterialTheme.typography.labelLarge)
                Row(
                    modifier = Modifier.padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = state.currentFilter.isCompleted == null,
                        onClick = { onEvent(TodoListEvents.OnStatusFilter(null)) },
                        label = { Text("Все") }
                    )
                    FilterChip(
                        selected = state.currentFilter.isCompleted == true,
                        onClick = { onEvent(TodoListEvents.OnStatusFilter(true)) },
                        label = { Text("Выполненные") }
                    )
                    FilterChip(
                        selected = state.currentFilter.isCompleted == false,
                        onClick = { onEvent(TodoListEvents.OnStatusFilter(false)) },
                        label = { Text("Активные") }
                    )
                }

                Text("Приоритет:", style = androidx.compose.material3.MaterialTheme.typography.labelLarge)
                LazyRow(
                    modifier = Modifier.padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    item {
                        FilterChip(
                            selected = state.currentFilter.priority == null,
                            onClick = { onEvent(TodoListEvents.OnPriorityFilter(null)) },
                            label = { Text("Все") }
                        )
                    }
                    items(TaskPriority.entries) { priority ->
                        FilterChip(
                            selected = state.currentFilter.priority == priority,
                            onClick = { onEvent(TodoListEvents.OnPriorityFilter(priority)) },
                            label = { Text(priority.displayName) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = getPriorityColor(priority)
                            )
                        )
                    }
                }

                if (state.currentFilter.hasActiveFilters()) {
                    Text(
                        text = "Активные фильтры: ${
                            listOfNotNull(
                                if (state.currentFilter.priority != null) "приоритет" else null,
                                if (state.currentFilter.isCompleted != null) "статус" else null
                            ).joinToString(", ")
                        }",
                        style = androidx.compose.material3.MaterialTheme.typography.bodySmall,
                        color = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        confirmButton = {
            if (state.currentFilter.hasActiveFilters()) {
                TextButton(
                    onClick = { onEvent(TodoListEvents.OnClearFilter) }
                ) {
                    Text("Сбросить фильтры")
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onEvent(TodoListEvents.OnFilterClick) }
            ) {
                Text("Закрыть")
            }
        }
    )
}