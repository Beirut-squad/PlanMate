package domain.useCase.project

import ui.common.exception.EmptyStateNameException
import domain.model.Project
import domain.model.TaskState
import domain.repository.ProjectRepository
import domain.useCase.log.CreateProjectLogUseCase
import java.util.*

class EditProjectStateUseCase(
    private val repository: ProjectRepository,
    private val logUseCase: CreateProjectLogUseCase
) {
    suspend fun editStateToProject(currentUserID: UUID, project: Project, taskState: TaskState): Project {
        if (taskState.name.isBlank()) {
            throw EmptyStateNameException()
        }
        return repository.editStateToProject(project.id, taskState).also { updatedProject ->
            logUseCase.createProjectLog(userId = currentUserID, previousProject = project, currentProject = updatedProject)
        }
    }
}