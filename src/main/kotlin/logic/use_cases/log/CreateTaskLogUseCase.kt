package logic.use_cases.log

import org.example.logic.repositories.log_repository.LogRepository
import org.example.models.Task
import java.util.UUID

class CreateTaskLogUseCase(
    private val logRepository: LogRepository
) {

    fun createTaskLog(previousTask: Task?, currentTask: Task?,userId : UUID): Result<Unit> {
        TODO()
    }

}