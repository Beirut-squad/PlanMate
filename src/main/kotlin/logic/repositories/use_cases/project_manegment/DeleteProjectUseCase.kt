package org.example.logic.repositories.use_cases.project_manegment

import logic.use_cases.log.CreateProjectLogUseCase
import org.example.logic.repositories.project_repository.ProjectRepository
import org.example.models.Project
import java.util.*

class DeleteProjectUseCase(
    private val projectRepository: ProjectRepository,
    private val logUseCase: CreateProjectLogUseCase
) {
    fun deleteProject(creatorUserID: UUID, project: Project) {
        logUseCase.createProjectLog(creatorUserID, project, project)
        projectRepository.deleteProject(project.id)
    }

}