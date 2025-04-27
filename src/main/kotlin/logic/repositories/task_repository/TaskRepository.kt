package org.example.logic.repositories.task_repository

import org.example.models.Task

interface TaskRepository {
    fun createTask(task: Task)
    fun editTask(task: Task)
    fun deleteTask(task: Task)
}