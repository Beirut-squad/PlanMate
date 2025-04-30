package org.example.data.repositories.task_repository

import org.example.data.datasource.log_data_source.LogDataSource
import org.example.data.datasource.task_data_source.TaskDataSource
import org.example.logic.exceptions.TaskCreationException
import org.example.logic.exceptions.TaskEditException
import org.example.logic.repositories.task_repository.TaskRepository

import org.example.models.Task
import java.time.LocalDateTime
import java.util.*

class TaskRepositoryImpl(
    private val taskDataSource: TaskDataSource,
) : TaskRepository {
    override fun createTask(task: Task): Result<Unit> {
        return taskDataSource.createTask(task)
            .fold(
                onSuccess = { Result.success(Unit) },
                onFailure = { Result.failure(TaskCreationException("Failed to create task: ${it.message}")) }
            )
    }


    override fun editTask(task: Task): Result<Unit> {
        return taskDataSource.editTask(task).fold(
            onSuccess = { Result.success(Unit) },
            onFailure = { Result.failure(TaskEditException("Failed to edit task: ${it.message}")) }
        )
    }

    override fun deleteTask(id: UUID): Result<Unit> {
        return Result.success(Unit)
    }

    override fun getAllTasks(): Result<List<Task>> {
        TODO("Not yet implemented")
    }

    override fun getTask(id: UUID): Result<Task> {
        TODO("Not yet implemented")
    }
}