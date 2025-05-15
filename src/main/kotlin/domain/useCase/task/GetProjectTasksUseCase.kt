package domain.useCase.task

import domain.model.Task
import domain.repository.TaskRepository
import java.util.*

class GetProjectTasksUseCase(
    private val taskRepository: TaskRepository,
) {
    suspend fun getProjectTasks(projectId: UUID): List<Task> {
        return taskRepository.getAllProjectTasks(projectId)

    }
}