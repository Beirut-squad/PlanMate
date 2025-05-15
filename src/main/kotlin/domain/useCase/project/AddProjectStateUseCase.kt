package domain.useCase.project

import ui.common.exception.EmptyStateNameException
import domain.model.TaskState
import domain.model.Project
import domain.useCase.log.CreateProjectLogUseCase
import domain.repository.ProjectRepository
import java.util.*

class AddProjectStateUseCase(
    private val repository: ProjectRepository,
    private val logUseCase: CreateProjectLogUseCase
) {
    suspend fun addStateToProject(currentUserID: UUID, project: Project, taskState: TaskState): Project {
        if (taskState.name.isBlank()) {
            throw EmptyStateNameException()
        }
        return repository.addStateToProject(project.id, taskState).also { updatedProject ->
            logUseCase.createProjectLog(
                userId = currentUserID,
                previousProject = project,
                currentProject = updatedProject
            )
        }
    }
}
