package org.example.data.repositories.task_repository

import org.example.data.datasource.log_data_source.LogDataSource
import org.example.data.datasource.task_data_source.TaskDataSource
import org.example.logic.exceptions.TaskCreationException
import org.example.logic.exceptions.TaskEditException
import org.example.logic.repositories.task_repository.TaskRepository
import org.example.models.Log
import org.example.models.Task
import java.util.*

class TaskRepositoryImpl(
    private val taskDataSource: TaskDataSource,
    private val logDataSource: LogDataSource
) : TaskRepository {
    override fun createTask(task: Task, log: Log): Result<String> {
        return taskDataSource.createTask(task)
            .fold(
                onSuccess = {
                    logDataSource.createLog(log)
                    Result.success("Task created successfully")
                },
                onFailure = { Result.failure(TaskCreationException("Failed to create task: ${it.message}")) }
            )

    }


    override fun editTask(task: Task, log: Log): Result<String> {
        return taskDataSource.editTask(task).fold(
            onSuccess = { Result.success("Task edited successfully") },
            onFailure = { Result.failure(TaskEditException("Failed to edit task: ${it.message}")) }
        )
    }

    override fun deleteTask(id: UUID): Result<String> {
        TODO("Not yet implemented")
    }

    override fun getAllTasks(): Result<List<Task>> {
        TODO("Not yet implemented")
    }

    override fun getTask(id: UUID): Result<Task> {
        TODO("Not yet implemented")
    }
}