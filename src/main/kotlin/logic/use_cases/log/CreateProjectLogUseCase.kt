package logic.use_cases.log

import org.example.logic.exceptions.log_exceptions.InvalidCreateProjectLogException
import org.example.logic.repositories.log_repository.LogRepository
import org.example.models.Project
import org.example.models.ProjectLog
import java.time.LocalDateTime
import java.util.UUID

class CreateProjectLogUseCase(
    private val logRepository: LogRepository
) {
    fun createProjectLog(userId: UUID, previousProject: Project?, currentProject: Project?) {
        if (previousProject == null && currentProject == null)
            throw InvalidCreateProjectLogException()

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