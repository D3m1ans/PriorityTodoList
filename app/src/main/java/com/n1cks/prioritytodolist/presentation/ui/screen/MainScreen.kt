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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
        if (state.value.tasks.isEmpty()){
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
                items(state.value.tasks){ task->
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
    }
}

@Composable
private fun TaskItem(
    task: TaskModel,
    onEvent: (TodoListEvents) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = {},
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .shadow(
                elevation = 16.dp,
                shape = RoundedCornerShape(30.dp),
                clip = false,
                ambientColor = Color(0xFFFFCC00).copy(alpha = 0.5f),
                spotColor = Color(0xFFFFCC00).copy(alpha = 0.6f)
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
                    .padding(end =  8.dp)
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
                    .padding(end =  8.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_delete),
                    contentDescription = "Delete task",
                    modifier = Modifier.size(20.dp),
                    tint = Color(0xFFFFCC00)
                )
            }
        }
    }
}