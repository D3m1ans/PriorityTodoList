package com.n1cks.data.local.converters

import androidx.room.TypeConverter
import com.n1cks.domain.model.TaskPriority

class TaskConverters {

    @TypeConverter
    fun fromPriority(priority: TaskPriority): String{
        return priority.name
    }

    @TypeConverter
    fun toPriority(priority: String): TaskPriority{
        return TaskPriority.valueOf(priority)
    }
}