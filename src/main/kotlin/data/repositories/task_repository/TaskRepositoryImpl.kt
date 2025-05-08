package org.example.data.repositories.task_repository

import org.example.data.datasource.task_data_source.TaskDataSource
import org.example.logic.repositories.task_repository.TaskRepository
import org.example.models.Task
import java.util.*

class TaskRepositoryImpl(
    private val taskDataSource: TaskDataSource,
) : TaskRepository {
    override suspend fun createTask(task: Task) {
        return taskDataSource.createTask(task)
    }

    override suspend fun editTask(task: Task) {
        return taskDataSource.editTask(task)
    }

    override suspend fun deleteTask(id: UUID) {
        return taskDataSource.deleteTask(id)
    }

    override suspend fun getAllTasks(): List<Task> {
        return taskDataSource.getAllTasks()
    }

    override suspend fun getTask(id: UUID): Task {
        return taskDataSource.getTask(id)
    }

    override suspend fun getTaskByStateIdAndProjectId(projectId: UUID, stateId: UUID): List<Task> {
        return taskDataSource.getTaskByStateIdAndProjectId(projectId, stateId)
    }

    override suspend fun getAllTasksForProject(projectId: UUID): List<Task> {
        return taskDataSource.getAllTasksForProject(projectId)
    }
}