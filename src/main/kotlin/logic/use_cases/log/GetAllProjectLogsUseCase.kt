package logic.use_cases.log

import org.example.logic.repositories.log_repository.LogRepository
import org.example.models.ProjectLog

class GetAllProjectLogsUseCase(
    private val logRepository: LogRepository
) {
    fun getAllProjectLogs(): Result<List<ProjectLog>> {
        TODO()
    }
}