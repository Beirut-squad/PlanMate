package org.example.logic.use_cases.log

import org.example.logic.repositories.log_repository.LogRepository
import org.example.models.ProjectLog
import org.example.models.TaskLog

class GetAllProjectLogsUseCase(
    private val logRepository: LogRepository
) {
    suspend fun getAllProjectLogs(): List<ProjectLog> {
        return logRepository.getAllProjectLogs()
    }
}