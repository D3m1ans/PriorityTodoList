package com.n1cks.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.n1cks.data.local.converters.TaskConverters
import com.n1cks.data.local.dao.TodoDao
import com.n1cks.data.local.entity.TaskEntity

@Database(
    entities = [TaskEntity::class],
    version = 1
)
@TypeConverters(TaskConverters::class)
abstract class TodoDB : RoomDatabase() {
    abstract fun todoDao() : TodoDao
}