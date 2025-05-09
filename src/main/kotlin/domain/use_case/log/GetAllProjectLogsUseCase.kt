package domain.use_case.log

import org.example.data.model.ProjectLog
import org.example.domain.repository.LogRepository

class GetAllProjectLogsUseCase(
    private val logRepository: LogRepository
) {
    fun getAllProjectLogs(): List<ProjectLog> {
        return logRepository.getAllProjectLogs()
    }
}