package domain.use_case.project

import domain.model.Project
import domain.model.State
import domain.use_case.authentication.GetCurrentUserUseCase
import domain.use_case.log.CreateProjectLogUseCase
import domain.exception.EmptyProjectDescriptionException
import domain.exception.EmptyProjectTitleException
import domain.repository.ProjectRepository
import java.time.LocalDateTime
import java.util.*

class CreateProjectUseCase(
    private val projectRepository: ProjectRepository,
    private val logUseCase: CreateProjectLogUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) {
    suspend fun createProject(name: String, description: String, stateNames: List<String>) {
        val creatorUserID = getCurrentUserUseCase.getCurrentUser().id
        when {
            name.isBlank() -> throw EmptyProjectTitleException()
            description.isBlank() -> throw EmptyProjectDescriptionException()
        }
        val project = buildProject(creatorUserID, name, description, stateNames)
        projectRepository.createProject(project)
        logUseCase.createProjectLog(creatorUserID, previousProject = null, currentProject = project)
    }

    private fun buildProject(creatorUserID: UUID, name: String, description: String, stateNames: List<String>)
            : Project {
        return Project(
            id = UUID.randomUUID(),
            title = name,
            description = description,
            states = stateNames.map { State(id = UUID.randomUUID(), name = it) },
            creatorUserID = creatorUserID,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            users = emptyList()
        )
    }
}