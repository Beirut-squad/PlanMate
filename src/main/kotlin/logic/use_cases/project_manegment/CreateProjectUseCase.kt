package org.example.logic.use_cases.project_manegment

import logic.use_cases.log.CreateProjectLogUseCase
import org.example.logic.exceptions.project_magement_exceptions.BlankFieldsException
import org.example.logic.repositories.project_repository.ProjectRepository
import org.example.logic.use_cases.authentication.GetCurrentLoggedInUserUseCase
import org.example.models.Project
import org.example.models.State
import java.time.LocalDateTime
import java.util.*

class CreateProjectUseCase(
    private val projectRepository: ProjectRepository,
    private val logUseCase: CreateProjectLogUseCase,
    private val getCurrentLoggedInUserUseCase: GetCurrentLoggedInUserUseCase
) {
    fun createProject(name: String, description: String, stateNames: List<String>) {
        val creatorUserID = getCurrentLoggedInUserUseCase.getCurrentUser().getOrThrow()?.id
            ?: throw IllegalStateException("User is not logged in")
        if (name.isBlank() || description.isBlank()) {
            throw BlankFieldsException("You should write a valid input as a string.")
        } else {
            val project = buildProject(creatorUserID, name, description, stateNames)
            logUseCase.createProjectLog(creatorUserID, previousProject = null, currentProject = project)
            projectRepository.createProject(project)
        }
    }

    private fun buildProject(creatorUserID: UUID, name: String, description: String, stateNames: List<String>)
            : Project {
        return Project(
            id = UUID.randomUUID(),
            name = name,
            description = description,
            state = stateNames.map { State(id = UUID.randomUUID(), name = it) },
            creatorUserID = creatorUserID,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now(),
            users = emptyList()
        )
    }
}