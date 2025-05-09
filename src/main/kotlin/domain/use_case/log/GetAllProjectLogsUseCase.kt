package domain.use_case.log

import domain.model.ProjectLog
import org.example.domain.repository.LogRepository

class GetAllProjectLogsUseCase(
    private val logRepository: LogRepository
) {
    suspend fun getAllProjectLogs(): List<ProjectLog> {
        return logRepository.getAllProjectLogs()
    }
}