package org.example.logic.repositories.use_case.project_manegment

import org.example.logic.exceptions.ProjectNotDeletedException
import org.example.logic.repositories.project_repository.ProjectRepository
import org.example.logic.repositories.use_case.log.CreateProjectLogUseCase
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