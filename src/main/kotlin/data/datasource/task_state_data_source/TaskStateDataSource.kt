package org.example.data.datasource.task_state_data_source

import org.example.models.TaskState

interface TaskStateDataSource {
    fun createTaskState(taskState: TaskState)
    fun editTaskState(taskState: TaskState)
    fun deleteTaskState(taskState: TaskState)
}