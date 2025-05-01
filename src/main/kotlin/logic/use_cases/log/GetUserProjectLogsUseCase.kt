package logic.use_cases.log

import org.example.logic.repositories.log_repository.LogRepository
import org.example.models.ProjectLog
import java.util.UUID

class GetUserProjectLogsUseCase(
    private val logRepository: LogRepository
) {
    fun getUserProjectLogs(userId: UUID): Result<List<ProjectLog>> {
        return logRepository.getAllProjectLogs().fold(
            onSuccess = { projectLogs ->
                Result.success(
                    projectLogs.filter { log ->
                        log.userId == userId
                    }
                )
            },
            onFailure = { Result.failure(it) }
        )
    }
}