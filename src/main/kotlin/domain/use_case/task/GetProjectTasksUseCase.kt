package domain.use_case.task

import data.csv.model.Task
import org.example.domain.repository.TaskRepository
import java.util.*

class GetProjectTasksUseCase(
    private val taskRepository: TaskRepository,
) {
    suspend fun getTasksForProject(projectId: UUID): List<Task> {
        return taskRepository.getAllTasksForProject(projectId)

    }
}