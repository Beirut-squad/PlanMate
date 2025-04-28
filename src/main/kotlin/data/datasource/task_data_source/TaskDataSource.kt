package org.example.data.datasource.task_data_source

import org.example.models.Task
import java.util.*

interface TaskDataSource {
    fun createTask(task: Task) : Result<String>
    fun editTask(task: Task) : Result<String>
    fun deleteTask(id:UUID) : Result<String>
    fun getAllTasks() : Result<List<Task>>
    fun getTask(id: UUID) : Result<Task>
}