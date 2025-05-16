package domain.useCase.task

import domain.model.Task
import domain.repository.TaskRepository
import java.util.*

class GetTaskByStateIdAndProjectId(
    private val taskRepository: TaskRepository,
) {
    suspend fun getTaskByStateIdAndProjectId(projectId: UUID, stateId: UUID): List<Task> {
        return taskRepository.getTaskByStateIdAndProjectId(projectId ,stateId)

    }

}