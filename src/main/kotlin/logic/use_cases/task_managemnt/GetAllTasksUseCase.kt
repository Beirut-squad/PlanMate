package org.example.logic.use_cases.task_managemnt

import org.example.logic.repositories.task_repository.TaskRepository
import org.example.logic.use_cases.log.GetUserTaskLogsUseCase
import org.example.models.Task
import java.util.*

class GetAllTasksUseCase(
    private val taskRepository: TaskRepository,
    private val getUserTaskLogsUseCase : GetUserTaskLogsUseCase,
) {
    suspend fun getAllTask(userId: UUID): List<Task> {
        val result = taskRepository.getAllTasks()

        getUserTaskLogsUseCase.getUserTaskLogs(userId)
        return result
    }
}