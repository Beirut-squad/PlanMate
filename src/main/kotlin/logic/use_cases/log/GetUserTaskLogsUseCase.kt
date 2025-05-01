package org.example.logic.use_cases.log

import org.example.logic.repositories.log_repository.LogRepository
import org.example.models.TaskLog

class GetUserTaskLogsUseCase(
    private val logRepository: LogRepository
) {
    fun getAllTaskLogs(): List<TaskLog> {
        TODO()
    }
}