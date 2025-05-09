package domain.use_case.project

import domain.exception.project.BlankFieldsException
import org.example.data.model.Project
import org.example.data.model.State
import org.example.domain.repository.ProjectRepository
import domain.use_case.log.CreateProjectLogUseCase
import java.util.*

class EditProjectStateUseCase(
    private val repository: ProjectRepository,
    private val logUseCase: CreateProjectLogUseCase
) {
    suspend fun editStateToProject(currentUserID: UUID, project: Project, state: State): Project {
        if (state.name.isBlank()) {
            throw BlankFieldsException("State name is required.")
        }
        return repository.editStateToProject(project.id, state).also { updatedProject ->
            logUseCase.createProjectLog(userId = currentUserID, previousProject = project, currentProject = updatedProject)
        }
    }
}