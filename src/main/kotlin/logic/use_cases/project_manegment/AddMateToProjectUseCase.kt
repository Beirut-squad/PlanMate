package org.example.logic.use_cases.project_manegment

import logic.use_cases.log.CreateProjectLogUseCase
import org.example.logic.exceptions.authentication_exceptions.NoLoggedInUserException
import org.example.logic.repositories.project_repository.ProjectRepository
import org.example.logic.use_cases.authentication.GetCurrentLoggedInUserUseCase
import org.example.models.User
import java.security.PrivateKey
import java.util.*

class AddMateToProjectUseCase(
    private val repository: ProjectRepository,
    private val getProjectByIdUseCase: GetProjectByIdUseCase,
    private val getCurrentLoggedInUserUseCase: GetCurrentLoggedInUserUseCase,
    private val logUseCase: CreateProjectLogUseCase,
) {
    suspend fun addMateToProject(projectId: UUID, user: User) {
        val previousProject = getProjectByIdUseCase.getProjectById(projectId)
        val creatorUserID = getCurrentLoggedInUserUseCase.getCurrentUser()?.id ?: throw NoLoggedInUserException()
        repository.addMateToProject(projectId, user).also { updatedProject ->
            logUseCase.createProjectLog(
                creatorUserID,
                previousProject = previousProject,
                currentProject = updatedProject
            )
        }
    }
}