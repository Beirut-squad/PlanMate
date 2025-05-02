package org.example.logic.use_cases.task_managemnt

import org.example.logic.exceptions.task_managment_exception.GetAllTasksException
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