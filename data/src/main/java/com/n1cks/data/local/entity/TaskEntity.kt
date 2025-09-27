package com.n1cks.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.n1cks.domain.model.TaskPriority

@Entity(tableName = "task")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val title : String,
    val desc: String? = null,
    val isCompleted: Boolean = false,
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val createdAt: Long = System.currentTimeMillis()
)