package org.example.logic.repositories.task_repository

import org.example.models.Log
import org.example.models.Task
import java.util.*

interface TaskRepository {
    fun createTask(task: Task, log: Log)  : Result<String>
    fun editTask(task: Task, log: Log) : Result<String>
    fun deleteTask(id:UUID) : Result<String>
    fun getAllTasks() : Result<List<Task>>
    fun getTask(id: UUID) : Result<Task>
}