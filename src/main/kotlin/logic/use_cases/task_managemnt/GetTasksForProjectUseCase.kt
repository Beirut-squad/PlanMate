package org.example.logic.use_cases.task_managemnt

import org.example.logic.repositories.task_repository.TaskRepository
import org.example.models.Task
import java.util.UUID

class GetTasksForProjectUseCase(
    private val taskRepository: TaskRepository,
) {
    suspend fun getTasksForProject(projectId: UUID): List<Task> {
        return taskRepository.getAllTasksForProject(projectId)

    }
}