package domain.use_case.project

import ui.common.exception.EmptyStateNameException
import domain.model.State
import domain.model.Project
import domain.use_case.log.CreateProjectLogUseCase
import domain.repository.ProjectRepository
import java.util.*

class AddProjectStateUseCase(
    private val repository: ProjectRepository,
    private val logUseCase: CreateProjectLogUseCase
) {
    suspend fun addStateToProject(currentUserID: UUID, project: Project, state: State): Project {
        if (state.name.isBlank()) {
            throw EmptyStateNameException()
        }
        return repository.addStateToProject(project.id, state).also { updatedProject ->
            logUseCase.createProjectLog(
                userId = currentUserID,
                previousProject = project,
                currentProject = updatedProject
            )
        }
    }
}
