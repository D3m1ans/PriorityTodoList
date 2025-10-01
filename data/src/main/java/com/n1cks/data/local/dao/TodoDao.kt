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
    fun getTasks() : Flow<List<TaskEntity>>

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

    @Query("""
        SELECT * FROM task 
        ORDER BY 
            CASE priority 
                WHEN 'URGENT' THEN 1 
                WHEN 'HIGH' THEN 2 
                WHEN 'MEDIUM' THEN 3 
                WHEN 'LOW' THEN 4 
            END, 
            createdAt DESC
    """)
    fun getTaskOrderByPriority(): Flow<List<TaskEntity>>
}