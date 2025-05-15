package domain.useCase.project

import ui.common.exception.EmptyStateNameException
import domain.model.Project
import domain.model.TaskState
import domain.useCase.log.CreateProjectLogUseCase
import domain.repository.ProjectRepository
import java.util.*

class DeleteProjectStateUseCase(
    private val repository: ProjectRepository,
    private val logUseCase: CreateProjectLogUseCase

) {
    suspend fun removeStateFromProject(currentUserID: UUID, project: Project, taskState: TaskState): Project {
        if (taskState.name.isBlank()) {
            throw EmptyStateNameException()
        }
        return repository.deleteStateFromProject(project.id, taskState).also { updatedProject ->
            logUseCase.createProjectLog(userId = currentUserID, previousProject = project, currentProject = updatedProject)
        }
    }
}