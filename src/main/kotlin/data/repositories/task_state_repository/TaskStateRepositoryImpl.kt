package org.example.data.repositories.task_state_repository

import data.datasource.task_state_data_source.TaskStateDataSource
import logic.repositories.task_state_repository.TaskStateRepository
import org.example.models.TaskState
import java.util.UUID


class TaskStateRepositoryImpl(
        private val taskStateDataSource: TaskStateDataSource
) : TaskStateRepository {
    override fun createTaskState(taskState: TaskState): Result<TaskState> {
        return taskStateDataSource.createTaskState(taskState)
    }

    override fun editTaskState(taskState: TaskState): Result<TaskState> {
       return taskStateDataSource.editTaskState(taskState)
    }

    override fun deleteTaskState(id: UUID): Result<Unit> {
        return taskStateDataSource.deleteTaskState(id)
    }
}