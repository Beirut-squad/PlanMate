package org.example.data.repositories.task_repository

import org.example.data.datasource.log_data_source.LogDataSource
import org.example.data.datasource.task_data_source.TaskDataSource
import org.example.logic.exceptions.TaskCreationException
import org.example.logic.repositories.task_repository.TaskRepository
import org.example.models.Task
import java.util.*

class TaskRepositoryImpl(
    private val taskDataSource: TaskDataSource,
    private val logDataSource: LogDataSource
) : TaskRepository {
    override fun createTask(task: Task): Result<String> {
        return  Result.success("Task created successfully")
    }


    override fun editTask(task: Task): Result<String> {
        TODO("Not yet implemented")
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