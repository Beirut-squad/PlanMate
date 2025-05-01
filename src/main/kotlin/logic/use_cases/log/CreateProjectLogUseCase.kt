package logic.use_cases.log

import org.example.logic.repositories.log_repository.LogRepository
import org.example.models.Project
import org.example.models.ProjectLog
import java.time.LocalDateTime
import java.util.UUID

class CreateProjectLogUseCase(
    private val logRepository: LogRepository
) {
    fun createProjectLog(userId: UUID, previousProject: Project?, currentProject: Project?): Result<Unit> {
        if (previousProject == null && currentProject == null) return Result.failure(
            IllegalArgumentException("Both previous and current projects cannot be null")
        )

        val entityId = currentProject?.id ?: previousProject!!.id

        return logRepository.saveProjectLog(
            ProjectLog(
                id = UUID.randomUUID(),
                userId = userId,
                entityId = entityId,
                previousEntity = previousProject,
                currentEntity = currentProject,
                createdAt = LocalDateTime.now()
            )
        ).fold(
            onSuccess = { Result.success(Unit) },
            onFailure = { Result.failure(it) }
        )
    }
}