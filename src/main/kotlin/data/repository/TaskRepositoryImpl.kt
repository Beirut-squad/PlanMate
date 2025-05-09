package org.example.data.repository

import data.datasource.task.TaskDataSource
import domain.model.Task
import org.example.domain.repository.TaskRepository
import java.util.*

class TaskRepositoryImpl(
    private val taskDataSource: TaskDataSource,
) : TaskRepository {
    override suspend fun createTask(task: Task) {
        return this.taskDataSource.createTask(task)
    }

    override suspend fun editTask(task: Task) {
        return this.taskDataSource.editTask(task)
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