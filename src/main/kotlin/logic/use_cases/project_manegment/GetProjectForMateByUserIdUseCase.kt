package org.example.logic.use_cases.project_manegment

import org.example.logic.repositories.project_repository.ProjectRepository
import org.example.models.Project
import java.util.*
import kotlin.collections.List

class GetProjectForMateByUserIdUseCase(
    private val repository: ProjectRepository,
) {
    fun getProjectForMateByUserId(userId: UUID): List<Project> {
        return repository.getAllProjects().filter {
            it.users.equals(userId)
        }
    }
}