package org.example.logic.use_cases.project_manegment

import org.example.logic.repositories.project_repository.ProjectRepository
import org.example.models.Project

class GetAllProjectsUseCases(
    private val projectRepository: ProjectRepository
) {
    suspend fun getAllProjects(): List<Project> {
        return projectRepository.getAllProjects()
    }
}