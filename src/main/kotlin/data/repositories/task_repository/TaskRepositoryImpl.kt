package org.example.data.repositories.task_repository

import org.example.data.datasource.log_data_source.LogDataSource
import org.example.data.datasource.task_data_source.TaskDataSource
import org.example.logic.repositories.task_repository.TaskRepository
import org.example.models.Task
import java.util.*

class TaskRepositoryImpl (
    private val taskDataSource: TaskDataSource,
    private val logDataSource: LogDataSource
) :TaskRepository {
    override fun createTask(task: Task) {
        TODO("Not yet implemented")
    }

    override fun editTask(task: Task) {
        TODO("Not yet implemented")
    }

    override fun deleteTask(id: UUID) {
        TODO("Not yet implemented")
    }

    override fun getAllTasks(): List<Task> {
        TODO("Not yet implemented")
    }

    override fun getTask(id: UUID): Task {
        TODO("Not yet implemented")
    }
}