package org.example.logic.repositories.task_state_repository

import org.example.models.TaskState

interface TaskStateRepository {
    fun createTaskState(taskState: TaskState)
    fun editTaskState(taskState: TaskState)
    fun deleteTaskState(taskState: TaskState)
}