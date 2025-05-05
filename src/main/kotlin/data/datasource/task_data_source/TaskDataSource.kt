package org.example.data.datasource.task_data_source

import org.example.models.Task
import java.util.*

interface TaskDataSource {
    fun createTask(task: Task) : Result<Unit>
    fun editTask(task: Task) : Result<Unit>
    fun deleteTask(id:UUID) : Result<Unit>
    fun getAllTasks() : Result<List<Task>>
    fun getTask(id: UUID) : Result<Task>
    fun getTaskByStateIdAndProjectId(projectId: UUID, stateId:UUID) : Result<List<Task>>
}

