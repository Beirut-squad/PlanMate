package org.example.logic.use_cases.log

import org.example.logic.repositories.log_repository.LogRepository
import org.example.models.Project
import org.example.models.ProjectLog
import java.util.UUID

class LogProjectUseCase(
    private val logRepository: LogRepository
) {
    fun createProjectLog(userId: UUID, previousProject: Project?, currentProject: Project?): Result<Unit> {
        TODO()
    }

    fun getProjectLogsByProjectId(projectId: UUID): List<ProjectLog> {
        TODO()
    }

    fun getAllProjectLogs(): List<ProjectLog> {
        TODO()
    }
}