package domain.useCase.project

import domain.model.Project
import domain.useCase.log.CreateProjectLogUseCase
import domain.repository.ProjectRepository
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