package org.example.logic.use_case.task_state_usecase

import logic.repositories.task_state_repository.TaskStateRepository
import org.example.models.TaskState
import java.util.*

class CreateTaskStateUseCase(
    private val taskStateRepository: TaskStateRepository
) {
    fun createTaskState(name: String): Result<TaskState> {
        if (name.isBlank()) return Result.failure(IllegalArgumentException("Create failed : name is Blank !!"))
        val newTaskState = TaskState(id = UUID.randomUUID(), name = name)
        return taskStateRepository.createTaskState(newTaskState)
    }

}