package org.example.data.datasource.task_data_source

import org.example.models.Task
import java.util.*

interface TaskDataSource {
    suspend fun createTask(task: Task)
    suspend fun editTask(task: Task)
    suspend fun deleteTask(id: UUID)
    suspend fun getAllTasks(): List<Task>
    suspend fun getTask(id: UUID): Task
    suspend fun getTaskByStateIdAndProjectId(projectId: UUID, stateId: UUID): List<Task>
    suspend fun getAllTasksForProject(projectId: UUID): List<Task>
}

