package org.example.logic.repositories.task_repository

import org.example.models.Task
import java.util.*

interface TaskRepository {
    fun createTask(task: Task)
    fun editTask(task: Task)
    fun deleteTask(task: Task)
    fun fetchAllTasks() :List<Task>
    fun fetchTask(id: UUID) :Task
}