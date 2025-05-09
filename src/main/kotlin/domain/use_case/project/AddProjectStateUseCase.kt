package domain.use_case.project

import org.example.data.model.Project
import org.example.data.model.State
import domain.exception.project.BlankFieldsException
import domain.use_case.log.CreateProjectLogUseCase
import org.example.domain.repository.ProjectRepository
import java.util.*

class AddProjectStateUseCase(
    private val repository: ProjectRepository,
    private val logUseCase: CreateProjectLogUseCase
) {
    suspend fun addStateToProject(currentUserID: UUID, project: Project, state: State): Project {
        if (state.name.isBlank()) {
            throw BlankFieldsException("State name is required.")
        }
        return repository.addStateToProject(project.id, state).also { updatedProject ->
            logUseCase.createProjectLog(userId = currentUserID, previousProject = project, currentProject = updatedProject)
        }
    }
}
