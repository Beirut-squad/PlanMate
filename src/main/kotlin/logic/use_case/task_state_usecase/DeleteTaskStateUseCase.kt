package org.example.logic.use_case.task_state_usecase

import logic.repositories.task_state_repository.TaskStateRepository
import java.util.UUID

class DeleteTaskStateUseCase(
    private val taskStateRepository: TaskStateRepository
) {
    fun deleteTaskState(id: UUID): Result<Unit> {
        return taskStateRepository.deleteTaskState(id)
    }

}