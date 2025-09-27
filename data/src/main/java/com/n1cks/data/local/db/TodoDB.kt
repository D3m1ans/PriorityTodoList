package com.n1cks.data.local.db

import androidx.room.Database
import com.n1cks.data.local.dao.TodoDao
import com.n1cks.data.local.entity.TaskEntity

@Database(
    entities = [TaskEntity::class],
    version = 1
)
abstract class TodoDB {
    abstract fun todoDao() : TodoDao
}