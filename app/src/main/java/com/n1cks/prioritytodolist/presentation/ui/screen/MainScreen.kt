package com.n1cks.prioritytodolist.presentation.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.n1cks.prioritytodolist.R
import com.n1cks.prioritytodolist.presentation.events.TodoListEvents
import com.n1cks.prioritytodolist.presentation.ui.components.FilterDialog
import com.n1cks.prioritytodolist.presentation.ui.components.TaskEditDialog
import com.n1cks.prioritytodolist.presentation.ui.components.TaskItem
import com.n1cks.prioritytodolist.presentation.viewmodel.TodoListViewModel
import com.n1cks.prioritytodolist.ui.theme.Lato

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
                    BadgedBox(
                        badge = {
                            if (state.value.isFiltering) {
                                Badge {
                                    Text("!")
                                }
                            }
                        }
                    ) {
                        IconButton(
                            onClick = {
                                viewmodel.onEvent(TodoListEvents.OnFilterClick)
                            }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_filter),
                                contentDescription = "filter button"
                            )
                        }
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

        if (state.value.showFilterDialog) {
            FilterDialog(
                state = state.value,
                onEvent = viewmodel::onEvent
            )
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