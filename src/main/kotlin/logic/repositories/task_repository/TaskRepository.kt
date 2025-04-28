package org.example.logic.repositories.task_repository

import org.example.models.Task
import java.util.*

interface TaskRepository {
    fun createTask(task: Task)
    fun editTask(task: Task)
    fun deleteTask(id:UUID)
    fun getAllTasks() :List<Task>
    fun getTask(id: UUID) :Task
}