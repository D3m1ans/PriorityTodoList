package com.n1cks.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.n1cks.data.local.entity.TaskEntity
import com.n1cks.domain.model.TaskPriority
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Query("SELECT * FROM task")
    fun getTasks(): Flow<List<TaskEntity>>

    @Insert
    suspend fun addTask(task: TaskEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Query("UPDATE task SET isCompleted = :isCompleted WHERE id = :taskId")
    suspend fun toggleTaskCompleted(taskId: Int, isCompleted: Boolean)

    @Query("UPDATE task SET priority = :priority WHERE id = :taskId")
    suspend fun updateTaskPriority(taskId: Int, priority: TaskPriority)
    @Query("SELECT * FROM task WHERE priority = :priority")
    fun getTaskByPriority(priority: TaskPriority): Flow<List<TaskEntity>>

    @Query("SELECT * FROM task WHERE isCompleted = :isCompleted")
    fun getTaskByStatus(isCompleted: Boolean): Flow<List<TaskEntity>>

    @Query("SELECT * FROM task WHERE priority = :priority AND isCompleted = :isCompleted")
    fun getTaskByPriorityAndStatus(
        priority: TaskPriority,
        isCompleted: Boolean
    ): Flow<List<TaskEntity>>
}