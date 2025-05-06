package org.example.data.datasource.task_data_source

import org.example.models.Task
import java.util.*

interface TaskDataSource {
  //  abstract val documents: Any

    suspend fun createTask(task: Task)
    suspend fun editTask(task: Task)
    suspend fun deleteTask(id:UUID)
    suspend fun getAllTasks() : List<Task>
    suspend fun getTask(id: UUID) : Task
}