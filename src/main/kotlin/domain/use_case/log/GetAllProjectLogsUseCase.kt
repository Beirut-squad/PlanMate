package domain.use_case.log

import domain.model.ProjectLog
import domain.repository.LogRepository

class GetAllProjectLogsUseCase(
    private val logRepository: LogRepository
) {
    suspend fun getAllProjectLogs(): List<ProjectLog> {
        return logRepository.getAllProjectLogs()
    }
}