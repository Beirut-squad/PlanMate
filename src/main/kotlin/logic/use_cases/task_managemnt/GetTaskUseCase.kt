package org.example.logic.use_cases.task_managemnt

import org.example.logic.exceptions.task_managment_exception.GetTaskException
import org.example.logic.repositories.task_repository.TaskRepository
import org.example.logic.use_cases.log.GetTaskLogsByTaskIdUseCase
import org.example.models.Task
import java.util.UUID

class GetTaskUseCase(
    private val taskRepository: TaskRepository,
    private val getTaskLogsByTaskIdUseCase: GetTaskLogsByTaskIdUseCase
) {
    fun getTask(TaskId: UUID): Result<Task> {
        val result = taskRepository.getTask(TaskId)
        if (result.isFailure) {
            throw GetTaskException("Failed to retrieve task")
        }
        getTaskLogsByTaskIdUseCase.getTaskLogsByTaskId(TaskId)

        return result
    }
}