package org.example.logic.use_case.project_manegment

import org.example.logic.exceptions.BlankFieldsException
import org.example.logic.repositories.project_repository.ProjectRepository
import org.example.models.Project
import java.time.LocalDateTime
import java.util.*


class EditProjectUseCase(
    private val projectRepository: ProjectRepository,
    //private val logUseCase: CreateProjectLogUseCase
) {
    fun editProject(
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

//            logUseCase.createProjectLog(
//                previousProject = newProject,
//                currentProject = newProject,
//                userId = creatorUserID
//            )
            projectRepository.editProject(newProject)
        }
    }
}
