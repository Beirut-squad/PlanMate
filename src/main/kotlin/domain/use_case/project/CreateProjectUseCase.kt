package domain.use_case.project

import org.example.data.model.Project
import org.example.data.model.State
import domain.exception.project.BlankFieldsException
import domain.use_case.authentication.GetCurrentUserUseCase
import domain.use_case.log.CreateProjectLogUseCase
import org.example.domain.repository.ProjectRepository
import java.time.LocalDateTime
import java.util.*

class CreateProjectUseCase(
    private val projectRepository: ProjectRepository,
    private val logUseCase: CreateProjectLogUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase
) {
    suspend fun createProject(name: String, description: String, stateNames: List<String>) {
        val creatorUserID = getCurrentUserUseCase.getCurrentUser()?.id
            ?: throw IllegalStateException("User is not logged in")
        if (name.isBlank() || description.isBlank()) {
            throw BlankFieldsException("You should write a valid input as a string.")
        } else {
            val project = buildProject(creatorUserID, name, description, stateNames)
            projectRepository.createProject(project)
            logUseCase.createProjectLog(creatorUserID, previousProject = null, currentProject = project)
        }
    }

    private fun buildProject(creatorUserID: UUID, name: String, description: String, stateNames: List<String>)
            : Project {
        return Project(
            id = UUID.randomUUID(),
            title = name,
            description = description,
            state = stateNames.map { State(id = UUID.randomUUID(), name = it) },
            creatorUserID = creatorUserID,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            users = emptyList()
        )
    }
}