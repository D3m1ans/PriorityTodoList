package com.n1cks.prioritytodolist.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.n1cks.domain.model.TaskPriority
import com.n1cks.prioritytodolist.presentation.utilits.displayName
import com.n1cks.prioritytodolist.presentation.utilits.getPriorityColor

@Composable
fun TaskEditDialog(
    currentTitle: String,
    currentDesc: String,
    currentPriority: TaskPriority,
    onDismiss: (String?, String?, TaskPriority?) -> Unit
) {

    var title by remember { mutableStateOf(currentTitle) }
    var desc by remember { mutableStateOf(currentDesc) }
    var selectedPriority by remember { mutableStateOf(currentPriority) }

    LaunchedEffect(currentTitle, currentDesc) {
        title = currentTitle
        desc = currentDesc
    }

    AlertDialog(
        onDismissRequest = { onDismiss(null, null, null) },
        title = { Text("Редактирование задачи") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Название задачи") },
                    singleLine = true
                )

                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = desc,
                    onValueChange = { desc = it },
                    label = { Text("Описание задачи") }
                )

                Column {
                    Text(
                        text = "Приоритет",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        items(TaskPriority.entries) { priority ->
                            FilterChip(
                                selected = priority == selectedPriority,
                                onClick = { selectedPriority = priority },
                                label = {
                                    Text(
                                        priority.displayName,
                                        fontSize = 12.sp
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = getPriorityColor(priority),
                                    selectedLabelColor = Color.White
                                )
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onDismiss(title, desc, selectedPriority) },
                enabled = title.isNotBlank()
            ) {
                Text("Сохранить")
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onDismiss(null, null, currentPriority) }
            ) {
                Text("Отмена")
            }
        }
    )
}