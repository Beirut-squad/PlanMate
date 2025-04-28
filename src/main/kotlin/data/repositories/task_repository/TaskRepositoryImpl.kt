package org.example.data.repositories.task_repository

import org.example.data.datasource.log_data_source.LogDataSource
import org.example.data.datasource.task_data_source.TaskDataSource
import org.example.logic.repositories.task_repository.TaskRepository
import org.example.models.Task

class TaskRepositoryImpl (
    taskDataSource: TaskDataSource,
    logDataSource: LogDataSource
) :TaskRepository {
    override fun createTask(task: Task) {
        TODO("Not yet implemented")
    }

    override fun editTask(task: Task) {
        TODO("Not yet implemented")
    }

    override fun deleteTask(task: Task) {
        TODO("Not yet implemented")
    }

    override fun fetchAllTasks(): List<Task> {
        TODO("Not yet implemented")
    }

    override fun fetchTask(): Task {
        TODO("Not yet implemented")
    }
}