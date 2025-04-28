package org.example.data.datasource.task_data_source

import org.example.models.Task
import java.util.*

interface TaskDataSource {
    fun createTask(task: Task)
    fun editTask(task: Task)
    fun deleteTask(id:UUID)
    fun getAllTasks() :List<Task>
    fun getTask(id: UUID) :Task
}