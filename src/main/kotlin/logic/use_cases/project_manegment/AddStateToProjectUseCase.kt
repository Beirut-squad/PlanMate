package org.example.logic.use_cases.project_manegment

import logic.exceptions.task_management_exception.TaskCreationException
import logic.use_cases.log.CreateProjectLogUseCase
import org.example.logic.exceptions.project_magement_exceptions.BlankFieldsException
import org.example.logic.repositories.log_repository.LogRepository
import org.example.logic.repositories.project_repository.ProjectRepository
import org.example.models.Project
import org.example.models.State
import org.example.models.Task
import java.util.*

class AddStateToProjectUseCase(
    private val repository: ProjectRepository,
    private val logUseCase: CreateProjectLogUseCase
) {
    fun addStateToProject(currentUserID: UUID, project: Project, state: State): Result<Project> {
        if (state.name.isBlank()) {
            return Result.failure(BlankFieldsException("State name is required."))
        }
        return repository.addStateToProject(project.id, state).onSuccess { updatedProject ->
            logUseCase.createProjectLog(userId = currentUserID, previousProject = project, currentProject = updatedProject)
        }
    }
}
