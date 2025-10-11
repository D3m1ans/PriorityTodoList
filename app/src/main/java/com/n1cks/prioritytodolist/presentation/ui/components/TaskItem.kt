package com.n1cks.prioritytodolist.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.n1cks.domain.model.TaskModel
import com.n1cks.prioritytodolist.R
import com.n1cks.prioritytodolist.presentation.events.TodoListEvents
import com.n1cks.prioritytodolist.presentation.utilits.getPriorityColor
import com.n1cks.prioritytodolist.ui.theme.Roboto


@Composable
fun TaskItem(
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