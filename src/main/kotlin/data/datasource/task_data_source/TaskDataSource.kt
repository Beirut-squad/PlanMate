package org.example.data.datasource.task_data_source

import org.example.models.Task

interface TaskDataSource {
    fun createTask(task: Task)
    fun editTask(task: Task)
    fun deleteTask(task: Task)
}