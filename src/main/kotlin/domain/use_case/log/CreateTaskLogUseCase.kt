package domain.use_case.log

import domain.model.Task
import domain.model.TaskLog
import org.example.domain.repository.LogRepository
import java.time.LocalDateTime
import java.util.*

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