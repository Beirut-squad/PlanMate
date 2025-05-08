package org.example.logic.use_cases.task_managemnt

import org.example.logic.repositories.task_repository.TaskRepository
import org.example.models.Task
import java.util.*

class GetTaskByStateIdAndProjectId(
    private val taskRepository: TaskRepository,
) {
    suspend fun getTaskByStateIdAndProjectId(projectId: UUID, stateId: UUID): List<Task> {
        return taskRepository.getTaskByStateIdAndProjectId(projectId ,stateId)

    }

}