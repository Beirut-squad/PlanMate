package domain.use_case.log

import domain.model.TaskLog
import domain.repository.LogRepository

class GetAllTaskLogsUseCase(
    private val logRepository: LogRepository
) {
    suspend fun getAllTaskLogs(): List<TaskLog> {
        return logRepository.getAllTaskLogs()
    }
}