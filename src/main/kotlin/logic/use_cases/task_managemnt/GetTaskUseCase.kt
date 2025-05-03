package org.example.logic.use_cases.task_managemnt

import logic.exceptions.task_management_exception.GetTaskException
import org.example.logic.repositories.task_repository.TaskRepository
import org.example.logic.use_cases.log.GetTaskLogsByTaskIdUseCase
import org.example.models.Task
import java.util.UUID

class GetTaskUseCase(
    private val taskRepository: TaskRepository,
    private val getTaskLogsByTaskIdUseCase: GetTaskLogsByTaskIdUseCase
) {
    fun getTask(taskId: UUID): Result<Task> {
        val result = taskRepository.getTask(taskId)
        if (result.isFailure) {
            throw GetTaskException("Failed to retrieve task")
        }
        getTaskLogsByTaskIdUseCase.getTaskLogsByTaskId(taskId)

        return result
    }
}