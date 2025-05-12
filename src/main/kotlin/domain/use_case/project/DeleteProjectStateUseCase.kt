package domain.use_case.project

import domain.exception.EmptyStateNameException
import domain.model.Project
import domain.model.State
import domain.use_case.log.CreateProjectLogUseCase
import domain.repository.ProjectRepository
import java.util.*

class DeleteProjectStateUseCase(
    private val repository: ProjectRepository,
    private val logUseCase: CreateProjectLogUseCase

) {
    suspend fun removeStateFromProject(currentUserID: UUID, project: Project, state: State): Project {
        if (state.name.isBlank()) {
            throw EmptyStateNameException()
        }
        return repository.removeStateFromProject(project.id, state).also { updatedProject ->
            logUseCase.createProjectLog(userId = currentUserID, previousProject = project, currentProject = updatedProject)
        }
    }
}