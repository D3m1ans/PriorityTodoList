package com.n1cks.prioritytodolist.di

import android.content.Context
import androidx.room.Room
import com.n1cks.data.local.db.TodoDB
import com.n1cks.data.repository.TodoRepositoryImpl
import com.n1cks.domain.repository.TodoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTodoDB(@ApplicationContext context: Context) : TodoDB{
        return Room.databaseBuilder(
            context,
            TodoDB::class.java,
            "todo_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideTodoRepository(db: TodoDB): TodoRepository{
        return TodoRepositoryImpl(db.todoDao())
    }
}