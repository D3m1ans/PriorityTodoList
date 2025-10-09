package com.n1cks.prioritytodolist.presentation.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.n1cks.domain.model.TaskModel
import com.n1cks.domain.model.TaskPriority
import com.n1cks.prioritytodolist.R
import com.n1cks.prioritytodolist.presentation.events.TodoListEvents
import com.n1cks.prioritytodolist.presentation.viewmodel.TodoListViewModel
import com.n1cks.prioritytodolist.ui.theme.Lato
import com.n1cks.prioritytodolist.ui.theme.Roboto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview
fun TodoListScreen(
    viewmodel: TodoListViewModel = hiltViewModel()
) {
    val state = viewmodel.state.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = colorResource(R.color.background_color),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Reminds",
                        color = Color.Black,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(
                        onClick = {}
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_filter),
                            contentDescription = "filter button"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = colorResource(R.color.background_color)
                ),
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(16.dp)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewmodel.onEvent(TodoListEvents.OnAddTask) },
                modifier = Modifier.size(64.dp),
                shape = CircleShape,
                containerColor = Color.White
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = "Add button",
                    tint = colorResource(R.color.icon_color)
                )
            }
        }
    ) { paddingValues ->
        if (state.value.tasks.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.empty_list),
                    color = colorResource(R.color.text_color),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Light,
                    fontFamily = Lato
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                items(state.value.tasks) { task ->
                    TaskItem(
                        task = task,
                        onEvent = viewmodel::onEvent,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                    )
                }
            }
        }

        state.value.todoTaskEdit?.let { task ->
            TaskEditDialog(
                currentTitle = state.value.editDialogTitle,
                currentDesc = state.value.editDialogDesc,
                currentPriority = state.value.selectedPriority,
                onDismiss = { newTitle, newDesc, newPriority ->
                    viewmodel.onEvent(
                        TodoListEvents.OnEditDialogDismiss(
                            task = task,
                            newTitle = newTitle,
                            newDesc = newDesc,
                            newPriority = newPriority
                        )
                    )
                }
            )
        }
    }
}

@Composable
private fun TaskItem(
    task: TaskModel,
    onEvent: (TodoListEvents) -> Unit,
    modifier: Modifier = Modifier
) {
    val priorityColor = getPriorityColor(task.priority)

    Card(
        onClick = { onEvent(TodoListEvents.OnEditTask(task)) },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(30.dp),
                clip = true,
                ambientColor = priorityColor,
                spotColor = priorityColor
            ),
        shape = RoundedCornerShape(30.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 56.dp)
                .padding(vertical = 8.dp, horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            IconButton(
                onClick = { onEvent(TodoListEvents.OnToggleTaskCompleted(task)) },
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = 8.dp)
            ) {
                Icon(
                    painter = painterResource(if (!task.isCompleted) R.drawable.ic_circle else R.drawable.ic_circle_fill),
                    contentDescription = "Task status",
                    modifier = Modifier.size(20.dp),
                    tint = colorResource(R.color.background_color)
                )
            }

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = task.title,
                    fontFamily = Roboto,
                    fontWeight = FontWeight.Normal,
                    fontSize = 20.sp,
                    color = Color.Black
                )

                if (!task.desc.isNullOrBlank()) {
                    Text(
                        text = task.desc!!,
                        fontFamily = Roboto,
                        fontWeight = FontWeight.Light,
                        fontSize = 16.sp,
                        color = colorResource(R.color.text_color),
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }

            IconButton(
                onClick = { onEvent(TodoListEvents.OnDeleteTask(task)) },
                modifier = Modifier
                    .size(24.dp)
                    .padding(end = 8.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_delete),
                    contentDescription = "Delete task",
                    modifier = Modifier.size(20.dp),
                    tint = priorityColor
                )
            }
        }
    }
}

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

@Composable
private fun getPriorityColor(priority: TaskPriority): Color {
    return when (priority){
        TaskPriority.LOW -> Color(0xFF8CFF00)
        TaskPriority.MEDIUM -> Color(0xFFFFCC00)
        TaskPriority.HIGH -> Color(0xFFFF6201)
        TaskPriority.CRITICAL -> Color(0xFFFF0000)
    }
}

private val TaskPriority.displayName: String
    get() = when (this) {
        TaskPriority.LOW -> "Низкий"
        TaskPriority.MEDIUM -> "Средний"
        TaskPriority.HIGH -> "Высокий"
        TaskPriority.CRITICAL -> "Срочный"
    }