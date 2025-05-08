package org.example.logic.use_cases.project_manegment

import logic.use_cases.log.CreateProjectLogUseCase
import org.example.logic.exceptions.project_magement_exceptions.BlankFieldsException
import org.example.logic.repositories.project_repository.ProjectRepository
import org.example.models.Project
import org.example.models.State
import java.util.*

class RemoveStateFromProjectUseCase(
    private val repository: ProjectRepository,
    private val logUseCase: CreateProjectLogUseCase

) {
    fun removeStateFromProject(currentUserID: UUID, project: Project, state: State): Project {
        if (state.name.isBlank()) {
            throw BlankFieldsException("State name is required.")
        }
        return repository.removeStateFromProject(project.id, state).also { updatedProject ->
            logUseCase.createProjectLog(userId = currentUserID, previousProject = project, currentProject = updatedProject)
        }
    }
}