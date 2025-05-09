package domain.use_case.log

import domain.model.TaskLog
import org.example.domain.repository.LogRepository

class GetAllTaskLogsUseCase(
    private val logRepository: LogRepository
) {
    fun getAllTaskLogs(): List<TaskLog> {
        return logRepository.getAllTaskLogs()
    }
}