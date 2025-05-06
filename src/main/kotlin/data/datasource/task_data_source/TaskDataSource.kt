package org.example.data.datasource.task_data_source

import org.example.models.Task
import java.util.*

interface TaskDataSource {
  //  abstract val documents: Any

    suspend fun createTask(task: Task) : Result<Unit>
    suspend fun editTask(task: Task) : Result<Unit>
    suspend fun deleteTask(id:UUID) : Result<Unit>
    suspend fun getAllTasks() : Result<List<Task>>
    suspend fun getTask(id: UUID) : Result<Task>
}