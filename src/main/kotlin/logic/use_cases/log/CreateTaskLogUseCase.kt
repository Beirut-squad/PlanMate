package logic.use_cases.log

import org.example.logic.repositories.log_repository.LogRepository
import org.example.models.Task
import org.example.models.TaskLog
import java.time.LocalDateTime
import java.util.UUID

class CreateTaskLogUseCase(
    private val logRepository: LogRepository
) {
    suspend fun createTaskLog(userId: UUID, previousTask: Task?, currentTask: Task?) {
        if (previousTask == null && currentTask == null)
            throw IllegalArgumentException("Both previousTask and currentTask cannot be null")

        val entityId = listOfNotNull(currentTask?.id, previousTask?.id).first()

        logRepository.saveTaskLog(
            TaskLog(
                id = UUID.randomUUID(),
                userId = userId,
                entityId = entityId,
                previousEntity = previousTask,
                currentEntity = currentTask,
                createdAt = LocalDateTime.now()
            )
        )
    }
}