package org.example.logic.use_cases.project_manegment

import logic.use_cases.log.CreateProjectLogUseCase
import org.example.logic.repositories.project_repository.ProjectRepository
import org.example.models.Project
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