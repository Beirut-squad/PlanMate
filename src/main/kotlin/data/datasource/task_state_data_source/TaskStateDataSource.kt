package data.datasource.task_state_data_source

import org.example.models.TaskState
import java.util.UUID

interface TaskStateDataSource {
    fun createTaskState(taskState: TaskState): Result<TaskState>
    fun editTaskState(taskState: TaskState): Result<TaskState>
    fun deleteTaskState(id: UUID): Result<Unit>
}