package org.example.logic.use_cases.project_manegment

import org.example.logic.repositories.project_repository.ProjectRepository
import org.example.models.User
import java.util.*

class AddMateToProjectUseCase(
    private val repository: ProjectRepository
) {
    fun addMateToProject(projectId: UUID, user: User){
        repository.addMateToProject(projectId, user)
    }
}