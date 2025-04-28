package org.example.logic.repositories.task_repository

import org.example.models.Task
import java.util.*

interface TaskRepository {
    fun createTask(task: Task)  : Result<String>
    fun editTask(task: Task) : Result<String>
    fun deleteTask(id:UUID) : Result<String>
    fun getAllTasks() : Result<List<Task>>
    fun getTask(id: UUID) : Result<Task>
}