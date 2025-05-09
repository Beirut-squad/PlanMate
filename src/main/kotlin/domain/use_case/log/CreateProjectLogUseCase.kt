package domain.use_case.log

import domain.model.Project
import domain.model.ProjectLog
import org.example.domain.repository.LogRepository
import java.time.LocalDateTime
import java.util.*

class CreateProjectLogUseCase(
    private val logRepository: LogRepository
) {
    fun createProjectLog(userId: UUID, previousProject: Project?, currentProject: Project?) {
        if (previousProject == null && currentProject == null)
            throw IllegalArgumentException("Both previous and current projects cannot be null")

        val entityId = listOfNotNull(currentProject?.id, previousProject?.id).first()

        return logRepository.saveProjectLog(
            ProjectLog(
                id = UUID.randomUUID(),
                userId = userId,
                entityId = entityId,
                previousEntity = previousProject,
                currentEntity = currentProject,
                createdAt = LocalDateTime.now()
            )
        )
    }
}