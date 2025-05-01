package org.example.logic.use_cases.log

import org.example.logic.repositories.log_repository.LogRepository
import org.example.models.TaskLog
import java.util.UUID

class GetUserTaskLogsUseCase(
    private val logRepository: LogRepository
) {
    fun getUserTaskLogs(userId: UUID): Result<List<TaskLog>> {
        TODO()
    }
}