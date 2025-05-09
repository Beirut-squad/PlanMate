package domain.use_case.task

import data.csv.model.Task
import org.example.domain.repository.TaskRepository
import java.util.*

class GetTaskByStateIdAndProjectId(
    private val taskRepository: TaskRepository,
) {
    suspend fun getTaskByStateIdAndProjectId(projectId: UUID, stateId: UUID): List<Task> {
        return taskRepository.getTaskByStateIdAndProjectId(projectId ,stateId)

    }

}