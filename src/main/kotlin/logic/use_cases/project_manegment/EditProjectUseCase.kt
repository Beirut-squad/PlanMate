package org.example.logic.use_cases.project_manegment

import logic.use_cases.log.CreateProjectLogUseCase
import org.example.logic.exceptions.project_magement_exceptions.BlankFieldsException
import org.example.logic.repositories.project_repository.ProjectRepository
import org.example.models.Project
import java.util.*


class EditProjectUseCase(
    private val projectRepository: ProjectRepository,
    private val logUseCase: CreateProjectLogUseCase
) {
    suspend fun editProject(
        newProject: Project,
        newName: String?,
        newDescription: String?,
        creatorUserID: UUID
    ) {
        if (newName == null && newDescription == null) {
            throw BlankFieldsException("unacceptable blank")
        } else {

            newProject.name = if (newName.isNullOrBlank()) newProject.name else newName
            newProject.description = if (newDescription.isNullOrBlank()) newProject.description else newDescription

            projectRepository.editProject(newProject)
            logUseCase.createProjectLog(
                previousProject = newProject,
                currentProject = newProject,
                userId = creatorUserID
            )
        }
    }
}
