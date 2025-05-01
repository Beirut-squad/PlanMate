package logic.use_cases.log

import org.example.logic.repositories.log_repository.LogRepository
import org.example.models.ProjectLog
import java.util.UUID

class GetProjectLogsByProjectIdUseCase(
    private val logRepository: LogRepository
) {
    fun getProjectLogsByProjectId(projectId: UUID): Result<List<ProjectLog>> {
        TODO()
    }
}