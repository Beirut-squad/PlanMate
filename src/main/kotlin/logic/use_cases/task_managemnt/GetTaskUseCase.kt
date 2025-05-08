package org.example.logic.use_cases.task_managemnt

import org.example.logic.repositories.task_repository.TaskRepository
import org.example.logic.use_cases.log.GetTaskLogsByTaskIdUseCase
import org.example.models.Task
import java.util.*

class GetTaskUseCase(
    private val taskRepository: TaskRepository,
    private val getTaskLogsByTaskIdUseCase: GetTaskLogsByTaskIdUseCase
) {
    suspend fun getTask(taskId: UUID): Task {
        val result = taskRepository.getTask(taskId)
        getTaskLogsByTaskIdUseCase.getTaskLogsByTaskId(taskId)

        return result
    }
}