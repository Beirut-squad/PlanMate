package domain.use_case.project

import domain.model.User
import domain.use_case.authentication.GetCurrentUserUseCase
import domain.use_case.log.CreateProjectLogUseCase
import org.example.domain.repository.ProjectRepository
import java.util.*

class AddProjectMateUseCase(
    private val repository: ProjectRepository,
    private val getProjectByIdUseCase: GetProjectByIdUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val logUseCase: CreateProjectLogUseCase,
) {
    suspend fun addMateToProject(projectId: UUID, user: User) {
        val previousProject = getProjectByIdUseCase.getProjectById(projectId)
        val creatorUserID = getCurrentUserUseCase.getCurrentUser().id
        repository.addMateToProject(projectId, user).also { updatedProject ->
            logUseCase.createProjectLog(
                creatorUserID,
                previousProject = previousProject,
                currentProject = updatedProject
            )
        }
    }
}