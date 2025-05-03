package org.example.logic.use_case.task_state_usecase

import logic.repositories.task_state_repository.TaskStateRepository
import org.example.models.TaskState

class EditTaskStateUseCase(
    private val taskStateRepository: TaskStateRepository
) {
    fun editTaskState(taskState : TaskState, newName: String): Result<TaskState>{
        if (newName.isBlank()) return Result.failure(IllegalArgumentException("Edit failed : name is Blank !!"))
        val updateTaskState = TaskState(taskState.id, newName)
        return taskStateRepository.editTaskState(updateTaskState)
    }
}