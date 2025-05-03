package org.example.logic.use_cases.task_managemnt

import logic.exceptions.task_management_exception.GetAllTasksException
import org.example.logic.repositories.task_repository.TaskRepository
import org.example.logic.use_cases.log.GetUserTaskLogsUseCase
import org.example.models.Task
import java.util.UUID

class GetAllTasksUseCase(
    private val taskRepository: TaskRepository,
    private val getUserTaskLogsUseCase : GetUserTaskLogsUseCase,
) {
    fun getAllTask(userId : UUID): Result<List<Task>>{
        val result = taskRepository.getAllTasks()
        if (result.isFailure){
            throw GetAllTasksException("Failed to retrieve tasks")
        }
        getUserTaskLogsUseCase.getUserTaskLogs(userId)
        return result
    }
}