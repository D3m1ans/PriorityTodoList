package com.n1cks.prioritytodolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.n1cks.prioritytodolist.presentation.ui.screen.TodoListScreen
import com.n1cks.prioritytodolist.ui.theme.PriorityTodoListTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PriorityTodoListTheme {
                TodoListScreen()
            }
        }
    }
}

