package logic.use_cases.log

import org.example.logic.repositories.log_repository.LogRepository
import org.example.models.ProjectLog
import java.util.UUID

class GetUserProjectLogsUseCase(
    private val logRepository: LogRepository
) {
    fun getUserProjectLogs(userId: UUID): Result<List<ProjectLog>> {
        TODO()
    }
}