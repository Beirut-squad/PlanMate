package domain.use_case.project

import data.csv.model.Project
import domain.use_case.log.CreateProjectLogUseCase
import org.example.domain.repository.ProjectRepository
import java.util.*

class DeleteProjectUseCase(
    private val projectRepository: ProjectRepository,
    private val logUseCase: CreateProjectLogUseCase
) {
    suspend fun deleteProject(creatorUserID: UUID, project: Project) {
        projectRepository.deleteProject(project.id)
        logUseCase.createProjectLog(creatorUserID, project, null)
    }
}