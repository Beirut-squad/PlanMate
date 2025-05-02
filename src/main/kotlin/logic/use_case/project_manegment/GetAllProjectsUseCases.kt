package org.example.logic.use_case.project_manegment

import org.example.logic.repositories.project_repository.ProjectRepository
import org.example.models.Project

class GetAllProjectsUseCases(
    private val projectRepository: ProjectRepository,
   // private val logUseCase: CreateProjectLogUseCase
) {
    fun getAllProjects(): Result<List<Project>> {
        return projectRepository.getAllProjects()
    }
}