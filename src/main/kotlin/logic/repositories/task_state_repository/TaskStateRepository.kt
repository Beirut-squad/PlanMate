package logic.repositories.task_state_repository

import org.example.models.TaskState
import java.util.UUID

interface TaskStateRepository {
    fun createTaskState(taskState: TaskState): Result<TaskState>
    fun editTaskState(taskState: TaskState): Result<TaskState>
    fun deleteTaskState(id: UUID): Result<Unit>
}